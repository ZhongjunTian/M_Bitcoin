package com.jontian.bitcoin.price.service;

import com.jontian.bitcoin.price.dao.Price;
import com.jontian.bitcoin.price.dao.PriceRepository;
import com.jontian.bitcoin.price.service.backup.OkcoinMarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * Created by zhongjun on 10/22/16.
 */
@Component
public class ScheduledPriceCrawler {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledPriceCrawler.class);
    @Autowired
    PriceRepository priceRepository;
    private OkcoinMarket market = new OkcoinMarket();
    private volatile int count = 0;

    @Scheduled(fixedRate = 3000)
    public void scheduledTask(){
        try {

            long t1 = System.currentTimeMillis();
            Price price = market.getCurrentPrice();
            priceRepository.save(price);
            long t2 = System.currentTimeMillis();
            logger.info((t2-t1)+"ms "+count+++" "+price.toString()+" Diff: $"+(price.getBuy()-price.getSell()));
        } catch (Exception e){
            logger.error(e.toString()+" | "+e.getMessage());
        }
    }

}
