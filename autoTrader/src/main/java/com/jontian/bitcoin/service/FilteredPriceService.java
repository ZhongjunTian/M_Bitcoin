package com.jontian.bitcoin.service;

import com.jontian.bitcoin.entity.LimitedConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by zhongjun on 10/23/16.
 */
public class FilteredPriceService extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(FilteredPriceService.class);
    private LimitedConcurrentLinkedQueue queue;
    private PriceService priceService = new PriceService();
    private double buyPrice;
    private double sellPrice;
    private double diff;
    private int increasingCounter;
    private int decreasingCounter;
    private double avgPrice;
    private int updateInterval = 6000;

    private FilteredPriceService() {
    }

    public FilteredPriceService(int maxSize, int updateInterval) {
        queue = new LimitedConcurrentLinkedQueue(maxSize);
        this.updateInterval = updateInterval;
        BigDecimal sPrice = priceService.getSellPrice();
        BigDecimal bPrice = priceService.getBuyPrice();
        sellPrice = sPrice.doubleValue();
        buyPrice = bPrice.doubleValue();
        for (int i = 0; i < maxSize; i++) {
            queue.add((sellPrice + buyPrice) / 2);
        }
        updateFilterData();
    }

    public void run() {
        while (true) {
            updateFilterData();
            Util.safeSleep(updateInterval);
        }
    }

    public void updateFilterData() {
        //update queue
        BigDecimal sPrice = priceService.getSellPrice();
        BigDecimal bPrice = priceService.getBuyPrice();
        sellPrice = sPrice.doubleValue();
        buyPrice = bPrice.doubleValue();
        queue.add((sellPrice + buyPrice) / 2);
        diff = sellPrice - buyPrice;
        //update counter
        double avgPrice = queue.getAveragePrice();
        updateCounter(avgPrice);
        this.avgPrice = avgPrice;
        //logger.debug("Price:" + Util.toString(buyPrice) + "  " + Util.toString(sellPrice) + " avg: " + Util.toString(avgPrice)
         //       + " In " + increasingCounter + " De " + decreasingCounter + " diff " + Util.toString(diff));

    }

    public void updateCounter(double avgPrice) {
        if (avgPrice >= this.avgPrice) {
            increasingCounter++;
        } else {
            increasingCounter = 0;
        }

        if (avgPrice <= this.avgPrice) {
            decreasingCounter++;
        } else {
            decreasingCounter = 0;
        }
    }

    public double getFilteredPrice() {
        return avgPrice;
    }

    public int getIncreasingCounter() {
        return increasingCounter;
    }

    public int getDecreasingCounter() {
        return decreasingCounter;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public double getDiff() {
        return diff;
    }
}
