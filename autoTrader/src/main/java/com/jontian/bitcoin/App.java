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
        double profit = 0;
        double buyPrice = 0;
        while (true) {
            int inCounter = filteredPriceService.getIncreasingCounter();
            int deCounter = filteredPriceService.getDecreasingCounter();
            if (empty) {
                if (deCounter >= 70) { // tryBuy after filtered price decreased a lot
                    //buySellService.changeBtcBalanceTo(BigDecimal.valueOf(0.05));
                    buyPrice = filteredPriceService.getBuyPrice();
                    logger.info("buy at "+buyPrice);
                    empty = false;
                }
            } else {
                if (inCounter >= 70) { // trySell after filtered price increased a lot
                    //buySellService.changeBtcBalanceTo(BigDecimal.valueOf(0));
                    double sellPrice = filteredPriceService.getSellPrice();
                    logger.info("Sell at "+ sellPrice);
                    profit += (sellPrice - buyPrice);
                    empty = true;
                }
            }
            double b = filteredPriceService.getBuyPrice();
            double s = filteredPriceService.getSellPrice();
            double filteredPrice = filteredPriceService.getFilteredPrice();
            logger.debug("Price:" + Util.toString(b) + "  " + Util.toString(s) + " avg: " + Util.toString(filteredPrice)
                    + " In " + inCounter + " De " + deCounter + " diff " + Util.toString(s - b)+" profit: "+profit);

            Util.safeSleep(5800);
        }
    }
}
