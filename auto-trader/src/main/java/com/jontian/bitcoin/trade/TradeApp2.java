package com.jontian.bitcoin.trade;

import com.jontian.bitcoin.trade.service.BuySellService;
import com.jontian.bitcoin.trade.service.Util;
import com.jontian.bitcoin.trade.service.FilteredPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.math.BigDecimal;

/**
 * Created by zhongjun on 10/30/16.
 */
@SpringBootApplication(scanBasePackages = "con.jontian.bitcoin.trade")
@PropertySource("trade.properties")
public class TradeApp2 {
    public static final Logger logger = LoggerFactory.getLogger(TradeApp2.class);
    public static final BigDecimal amount = BigDecimal.valueOf(0.03);

    public static void main(String args[]) {
        BuySellService buySellService = BuySellService.getInstance();

        FilteredPriceService filteredPriceService = new FilteredPriceService(60, 1000);
        filteredPriceService.start();
        while (true) {
            //if price increasing, buy then sell
            buySellService.changeBtcBalanceTo(amount);
            if (filteredPriceService.getDiff() > 0.15
                    && filteredPriceService.getIncreasingCounter() > 30) {

                boolean bought = buySellService.tryBuy(amount, true, () -> {
                    boolean teminate1 = filteredPriceService.getIncreasingCounter() < 30;
                    boolean teminate2 = filteredPriceService.getDiff()<0.1;
                    boolean terminate = teminate1 || teminate2;
                    logger.debug("waiting");
                    if (terminate)
                        logger.debug("terminate because of in "+teminate1+" diff "+teminate2);
                    return terminate;
                });

                if (bought) {
                    int in = filteredPriceService.getIncreasingCounter();
                    int de = filteredPriceService.getDecreasingCounter();
                    double diff = filteredPriceService.getDiff();
                    logger.info("in:" + in + " de:" + de + " diff:" + diff);
                    buySellService.changeBtcBalanceTo(amount);
                     in = filteredPriceService.getIncreasingCounter();
                     de = filteredPriceService.getDecreasingCounter();
                     diff = filteredPriceService.getDiff();
                    logger.info("in:" + in + " de:" + de + " diff:" + diff+"-------------------");
                }
            }
            //if price decreasing, sell then buy
            if (filteredPriceService.getDiff() > 0.15
                    && filteredPriceService.getDecreasingCounter() > 30) {
                boolean sold = buySellService.trySell(amount, true, () -> {
                    boolean teminate1 = filteredPriceService.getDecreasingCounter() < 30;
                    boolean teminate2 = filteredPriceService.getDiff()<0.1;
                    boolean terminate = teminate1 || teminate2;
                    logger.debug("waiting");
                    if (terminate)
                        logger.debug("terminate because of in "+teminate1+" diff "+teminate2);
                    return terminate;
                });
                if (sold) {
                    int in = filteredPriceService.getIncreasingCounter();
                    int de = filteredPriceService.getDecreasingCounter();
                    double diff = filteredPriceService.getDiff();
                    logger.info("in:" + in + " de:" + de + " diff:" + diff);
                    buySellService.changeBtcBalanceTo(amount);
                     in = filteredPriceService.getIncreasingCounter();
                     de = filteredPriceService.getDecreasingCounter();
                     diff = filteredPriceService.getDiff();
                    logger.info("in:" + in + " de:" + de + " diff:" + diff+"-------------------");
                }
            }
            Util.safeSleep(1000);
        }
//        while(true){
//            int inCounter = filteredPriceService.getIncreasingCounter();
//            int deCounter = filteredPriceService.getDecreasingCounter();
//            double buyPrice = filteredPriceService.getBuyPrice();
//            double sellPrice = filteredPriceService.getSellPrice();
//            double filteredPrice = filteredPriceService.getFilteredPrice();
//
//
//            logger.debug("Price:"+Util.toString(buyPrice)+"  "+Util.toString(sellPrice) +" avg: "+Util.toString(filteredPrice)
//                    +" In "+inCounter+" De "+deCounter+" diff " +Util.toString(sellPrice-buyPrice));
//
//            Util.safeSleep(1000);
//        }
    }
}
