package com.jontian.bitcoin.service;

import com.jontian.bitcoin.entity.Account;
import com.jontian.bitcoin.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.function.Supplier;

/**
 * Created by zhongjun on 10/22/16.
 */
public enum BuySellService {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(BuySellService.class);
    private static Object lock = new Object();
    protected OrderService orderService = new OrderService();
    protected PriceService priceService = new PriceService();
    protected volatile BigDecimal currentBtcBalance;
    protected volatile BigDecimal currentUsdBalance;
    private static final double MIN_BTC_SIZE = 0.01;
    private boolean terminateBuy;
    private boolean terminateSell;

    private BuySellService() {
    }

    public static BuySellService getInstance() {
        return INSTANCE;
    }

    /*
        Should be synchronized
     */
    //TODO NOT SUPPORT MULTIPLE STRATEGY
    public synchronized void changeBtcBalanceTo(BigDecimal size) {
        synchronized (lock) {
            long t0 = System.currentTimeMillis();
            updateCurrentBalance();
            BigDecimal goalBtcBalance = size;
            BigDecimal startUsdBalance = currentUsdBalance;
            BigDecimal startBtcBalance = currentBtcBalance;
            //double startBalance = getCombinedBalance();
            while (true) {
                if (tryChangeBalanceTo(goalBtcBalance, false))
                    break;
            }
            long t1 = System.currentTimeMillis();
            return;
        }
    }

    /*
        return:
     */
    public boolean tryChangeBalanceTo(BigDecimal goalBtcBalance, boolean patialDoneReturnFlag) {
        updateCurrentBalance();
        logger.debug("Target btc: " + goalBtcBalance + " Current balance Btc: " + currentBtcBalance + " Usd: " + currentUsdBalance);
        BigDecimal buySize = goalBtcBalance.subtract(currentBtcBalance);
        BigDecimal sellSize = currentBtcBalance.subtract(goalBtcBalance);
        if (buySize.doubleValue() > 0
                && buySize.doubleValue() >= MIN_BTC_SIZE) {
            return this.tryBuy(buySize, patialDoneReturnFlag);//TODO should return the price
        } else if (sellSize.doubleValue() > 0
                && sellSize.doubleValue() >= MIN_BTC_SIZE) {
            return this.trySell(sellSize, patialDoneReturnFlag);
        } else {
            return true;
        }
    }

    @Deprecated
    private double getCombinedBalance() {
        updateCurrentBalance();
        BigDecimal btcPrice = priceService.getSellPrice();
        return currentUsdBalance.doubleValue() + currentBtcBalance.doubleValue() * btcPrice.doubleValue();
    }

    protected void updateCurrentBalance() {
        Account[] accounts = orderService.getAccounts();
        Account btc = null;
        Account usd = null;
        for (Account account : accounts) {
            if (account.getCurrency().equals("USD"))
                usd = account;
            if (account.getCurrency().equals("BTC"))
                btc = account;
        }
        btc.getBalance();
        currentBtcBalance = btc.getBalance();
        currentUsdBalance = usd.getBalance();
    }

    public boolean tryBuy(BigDecimal size, boolean partialDoneReturnFlag) {
        return tryBuy(size,partialDoneReturnFlag,()->false);
    }
    /*
        try to buy until price change
     */
    public boolean tryBuy(BigDecimal size, boolean partialDoneReturnFlag, Supplier<Boolean> terminate) {
        updateCurrentBalance();
        BigDecimal startBalance = currentBtcBalance;
        BigDecimal goalBalance = currentBtcBalance.add(size);

        BigDecimal myBuyPrice = priceService.getBuyPrice();
        logger.debug("Buying: Going to tryBuy " + size + " BTC at $" + myBuyPrice);
        Order order = orderService.createBuyOrder(myBuyPrice, size);
        while (goalBalance.compareTo(currentBtcBalance) != 0) {
            if ( priceService.getBuyPrice().compareTo(myBuyPrice) != 0
                    || terminate.get() == true) {
                //price changed, cancel and re-tryBuy
                boolean cancelled = orderService.tryCancelOrder(order.getId());
                logger.debug("Buying: Order cancelled because of price change " + order.getId());
                updateCurrentBalance();
                break;
            } else { //wait
                logger.debug("Buying: Waiting for order " + order.getId() + " $" + Util.toString(myBuyPrice));
                Util.safeSleep(1000);
                updateCurrentBalance();
            }
        }
        // balance not changed => false
        // goal achieved  => true
        // balance changed
        if (currentBtcBalance.compareTo(startBalance) == 0) {
            return false;
        } else if (currentBtcBalance.compareTo(goalBalance) == 0) {
            logger.info("Bought " + Util.toString(currentBtcBalance.subtract(startBalance)) + " at + $" + myBuyPrice);
            return true;
        } else {
            logger.info("Bought " + Util.toString(currentBtcBalance.subtract(startBalance)) + " at + $" + myBuyPrice);
            return partialDoneReturnFlag;
        }
    }

    public boolean trySell(BigDecimal size, boolean partialDoneReturnFlag) {
        return trySell(size,partialDoneReturnFlag,()->false);
    }
    /*
        try to sell until price change
     */
    public boolean trySell(BigDecimal size, boolean partialDoneReturnFlag, Supplier<Boolean> terminate) {
        updateCurrentBalance();
        BigDecimal startBalance = currentBtcBalance;
        BigDecimal goalBalance = currentBtcBalance.subtract(size);

        BigDecimal mySellPrice = priceService.getSellPrice();
        logger.debug("Selling: Going to trySell " + size + " BTC at trySell:$" + mySellPrice);
        Order order = orderService.createSellOrder(mySellPrice, size);
        while (goalBalance.compareTo(currentBtcBalance) != 0) {
            if ((priceService.getSellPrice()).compareTo(mySellPrice) != 0
                    || terminate.get() == true) {
                //price changed, cancel and re-trySell
                boolean cancelled = orderService.tryCancelOrder(order.getId());
                logger.debug("Selling: Order cancelled because of price change " + order.getId() + " $" + Util.toString(mySellPrice));
                updateCurrentBalance();
                break;
            } else { //wait
                logger.debug("Selling: Waiting for order " + order.getId()+ " $" + Util.toString(mySellPrice));
                Util.safeSleep(1000);
                updateCurrentBalance();
            }
        }

        // balance not changed => false
        // goal achieved  => true
        // balance changed
        if (currentBtcBalance.compareTo(startBalance) == 0) {
            return false;
        } else if (currentBtcBalance.compareTo(goalBalance) == 0) {
            logger.info("Sold " + Util.toString(startBalance.subtract(currentBtcBalance)) + " at - $" + mySellPrice);
            return true;
        } else {
            logger.info("Sold " + Util.toString(startBalance.subtract(currentBtcBalance)) + " at - $" + mySellPrice);
            return partialDoneReturnFlag;
        }
    }
}
