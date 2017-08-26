package com.jontian.bitcoin.trade.service;

import com.jontian.bitcoin.price.service.CoinbasePriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by zhongjun on 10/24/16.
 */
public class PriceService {
    private static final Logger logger = LoggerFactory.getLogger(PriceService.class);
    private CoinbasePriceService coinbase = new CoinbasePriceService();

    public BigDecimal getBuyPrice() {
        while (true)
            try {
                BigDecimal p = coinbase.getSafeBuyPrice();
                return p;
            } catch (IOException e) {
                logger.error(e.getMessage());
                Util.safeSleep(1000);
            }
    }

    public BigDecimal getSellPrice() {
        while (true)
            try {
                BigDecimal p = coinbase.getSafeSellPrice();
                return p;
            } catch (IOException e) {
                logger.error(e.getMessage());
                Util.safeSleep(1000);
            }
    }

}
