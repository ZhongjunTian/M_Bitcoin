package com.jontian.bitcoin.trade.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by zhongjun on 10/24/16.
 */
public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static void safeSleep(int miniSeconds) {
        try {
            Thread.sleep(miniSeconds);
        } catch (InterruptedException e1) {
            logger.error(e1.getMessage());
        }
    }

    public static String toString(BigDecimal balance) {
        return String.format("%5.2f", balance);
    }

    public static String toString(double balance) {
        return String.format("%5.3f", balance);
    }
}
