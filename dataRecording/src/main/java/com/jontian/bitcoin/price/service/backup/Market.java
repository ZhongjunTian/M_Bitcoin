package com.jontian.bitcoin.price.service.backup;

import com.jontian.bitcoin.price.dao.Price;
import com.jontian.bitcoin.price.util.HttpUtil;
import com.jontian.bitcoin.price.util.PriceParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by zhongjun on 9/14/16.
 */
public abstract class Market {
    public abstract String getUrl();
    public abstract Price parsePrice(String json);

    private Logger logger = LoggerFactory.getLogger(Market.class);
    public static volatile double RATE = 6.67;
    private static String URL = "http://api.fixer.io/latest?base=USD&symbols=CNY";
    //@Scheduled(fixedRate = 600000L)
    public void runUpdatingCurrencyRate() {
        try {
            String json = HttpUtil.getResponse(URL,1000);
            double rate = PriceParseUtil.getDouble(json, "rates.CNY");
            if(rate != 0)
                RATE = rate;
            logger.info("USD to CNY = " + RATE);
        } catch (IOException e) {
            logger.error(e.toString()+" | "+e.getMessage());
        }
    }


}
