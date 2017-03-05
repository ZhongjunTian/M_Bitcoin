package com.jontian.bitcoin;

import com.jontian.bitcoin.service.BuySellService;
import com.jontian.bitcoin.service.FilteredPriceService;
import com.jontian.bitcoin.service.PriceService;
import com.jontian.bitcoin.service.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by zhongjun on 10/23/16.
 */
public class App {
    public static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String args[]) {
        BuySellService buySellService = BuySellService.getInstance();
        PriceService priceService = new PriceService();
        FilteredPriceService filteredPriceService = new FilteredPriceService(400, 6000);
        filteredPriceService.start();
        boolean empty = true;
        logger.info("Strat to work");
        while (true) {
            int inCounter = filteredPriceService.getIncreasingCounter();
            int deCounter = filteredPriceService.getDecreasingCounter();
            if (empty) {
                if (deCounter >= 70) { // tryBuy after filtered price decreased a lot
                    buySellService.changeBtcBalanceTo(BigDecimal.valueOf(0.05));
                    empty = false;
                }
            } else {
                if (inCounter >= 70) { // trySell after filtered price increased a lot
                    buySellService.changeBtcBalanceTo(BigDecimal.valueOf(0));
                    empty = true;
                }
            }
            double buyPrice = filteredPriceService.getBuyPrice();
            double sellPrice = filteredPriceService.getSellPrice();
            double filteredPrice = filteredPriceService.getFilteredPrice();
            logger.debug("Price:" + Util.toString(buyPrice) + "  " + Util.toString(sellPrice) + " avg: " + Util.toString(filteredPrice)
                    + " In " + inCounter + " De " + deCounter + " diff " + Util.toString(sellPrice - buyPrice));

            Util.safeSleep(5800);
        }
    }
}
