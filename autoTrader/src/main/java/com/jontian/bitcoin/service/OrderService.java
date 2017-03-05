package com.jontian.bitcoin.service;

import com.jontian.bitcoin.entity.Account;
import com.jontian.bitcoin.entity.LimitOrder;
import com.jontian.bitcoin.entity.Order;
import com.jontian.bitcoin.market.CoinbaseExchange;
import com.jontian.bitcoin.market.CoinbaseExchangeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

/**
 * Created by zhongjun on 10/23/16.
 */
public class OrderService {
    public static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    public CoinbaseExchange exchange;

    public OrderService() {
        exchange = CoinbaseExchangeFactory.createCoinbaseExchange();
    }

    @Deprecated
    public boolean isOrderDone(String id) {
        Order remoteOrder = getOrder(id);
        String status = remoteOrder.getStatus();
        if (status == null) {
            logger.error("status is null order:" + id);
            return false;
        }
        return status.equals("done");
    }

    @Deprecated//NOT TESTED
    public BigDecimal orderFilledSize(String id) {
        Order remoteOrder = getOrder(id);
        BigDecimal size = remoteOrder.getFilled_size();
        if (size == null) {
            return BigDecimal.ZERO;
        }
        return size;
    }

    public void cancelAllOrders() {
        while (true)
            try {
                Order[] orders = new Order[0];
                orders = exchange.getOpenOrders();
                for (Order od : orders)
                    exchange.cancelOrder(od.getId());
                return;
            } catch (Exception e) {
                logger.info(e.getMessage());
                Util.safeSleep(1000);
            }
    }

    public boolean tryCancelOrder(String id) {
        String response = cancelOrder(id);
        if (response.contains("done")) {
            return false;
        } else if (response.contains(id) || response.contains("NotFound")) {
            return true;
        } else {
            throw new IllegalStateException("unable to recognize reponse: " + response);
        }
    }

    private String cancelOrder(String id) {
        while (true)
            try {
                return exchange.cancelOrder(id);
            } catch (Exception e) {
                logger.info(e.getMessage());
                Util.safeSleep(1000);
            }
    }

    public Order createSellOrder(BigDecimal price, BigDecimal size) {
        LimitOrder sellOrder = new LimitOrder(price, size, "sell");
        return createOrder(sellOrder);
    }

    public Order createBuyOrder(BigDecimal price, BigDecimal size) {
        LimitOrder sellOrder = new LimitOrder(price, size, "buy");
        return createOrder(sellOrder);
    }

    public Order getOrder(String id) {
        while (true)
            try {
                Order order = exchange.getOrder(id);
                return order;
            } catch (Exception e) {
                logger.info(e.getMessage());
                Util.safeSleep(1000);
            }
    }

    private Order createOrder(LimitOrder o) {
        while (true)
            try {
                Order order = exchange.createOrder(o);
                return order;
            } catch (Exception e) {
                logger.info(e.getMessage());
                Util.safeSleep(1000);
            }
    }

    public Account[] getAccounts() {
        while (true)
            try {
                Account[] accounts = exchange.getAccounts();
                return accounts;
            } catch (Exception e) {
                logger.info(e.getMessage());
                Util.safeSleep(1000);
            }
    }
}
