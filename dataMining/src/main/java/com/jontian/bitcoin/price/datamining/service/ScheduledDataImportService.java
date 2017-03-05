package com.jontian.bitcoin.price.datamining.service;

import com.jontian.bitcoin.price.datamining.dao.HistoryPrice;
import com.jontian.bitcoin.price.datamining.dao.HistoryPriceRepository;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhongjun on 10/16/16.
 */
/*
Data is from
https://api.gdax.com/products/BTC-USD/candles?start=2016-09-28T21:00:00&end=2016-09-28T22:00:00&granularity=60
[
  [
    1475099940, //  time bucket start time
    605.01,     //low lowest price during the bucket interval
    605.01,     //high highest price during the bucket interval
    605.01,     //open opening price (first trade) in the bucket interval
    605.01,     //close closing price (last trade) in the bucket interval
    1.20063802  //volume volume of trading activity during the bucket interval
  ],
 */
@Service
public class ScheduledDataImportService {
    protected static final Logger logger = LoggerFactory.getLogger(ScheduledDataImportService.class);
    public static final String TIME_INTERVAL = "60";
    private final String startDateStr = "2016-10-25T03:00:00Z";
    private final String endDateStr = "2016-10-26T03:00:00Z";
    private final String baseUrl = "https://api.gdax.com/products/BTC-USD/candles";
    @Autowired
    private HistoryPriceRepository historyPriceRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
    private Date startDate;
    private Date endDate;
    private volatile int hours = 0;
    private boolean firstRun = true;
    private String targetStartDate;
    private String targetEndDate;

    //https://api.gdax.com/products/BTC-USD/candles?start=2016-09-28T21:00:00&end=2016-09-28T22:00:00&granularity=60
    //147666222 = 10 16 2016
    public ScheduledDataImportService() throws ParseException {
        //        TimeZone tz = TimeZone.getTimeZone("UTC");
        //        df.setTimeZone(tz);
        startDate = df.parse(startDateStr);
        endDate = df.parse(endDateStr);
        logger.info("Start from "+startDate.toString());
        logger.info("End to "+endDate.toString());
        //        String nowAsISO = df.format(new Date());
    }
    @Scheduled(fixedDelay = 1000L)
    public void dataImport() throws URISyntaxException {
        if(firstRun){
            firstRun = false;
            historyPriceRepository.deleteAll();
        }

        if(updateTargetDateUntilEndDate() == false){
            logger.info("end date");
            System.exit(0);
        }

        String url = createUrl(targetStartDate, targetEndDate);
        logger.info(url.replace(baseUrl,""));
        Double[][] data =  restTemplate.getForObject(url,Double[][].class);
        for(Double[] d:data){
            HistoryPrice price = new HistoryPrice();
            long time = (int)(double)d[0];
            price.setTime(time);
            price.setLowest(d[1]);
            price.setHighest(d[2]);
            price.setOpen(d[3]);
            price.setClose(d[4]);
            price.setVolume(d[5]);
            historyPriceRepository.save(price);
        }
    }

    private boolean updateTargetDateUntilEndDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.HOUR,hours);
        Date s = c.getTime();
        targetStartDate = df.format(s);
        c.add(Calendar.HOUR,1);
        Date e = c.getTime();
        targetEndDate = df.format(e);
        hours++;
        if(e.after(endDate)){
            return false;
        }
        return true;
    }

    private String createUrl(String targetStartDate, String targetEndDate) throws URISyntaxException {
        URIBuilder uri = new URIBuilder(baseUrl);
        uri.addParameter("start",targetStartDate);
        uri.addParameter("end",targetEndDate);
        uri.addParameter("granularity", TIME_INTERVAL);
        String url = uri.build().toString();
        url = url.replaceAll("%3A",":");
        return url;
    }
}
