package com.jontian.bitcoin.service;

import com.jontian.bitcoin.entity.*;
import com.jontian.bitcoin.market.CoinbaseExchange;
import com.jontian.bitcoin.market.CoinbaseExchangeFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.TestCase.fail;

/**
 * Created by Ishmael (sakamura@gmail.com) on 6/18/2016.
 */
public class OrderTests {
    static CoinbaseExchange exchange;
    static BuySellService buySellService;
    static OrderService orderService;
    BigDecimal small_size_00_1 = BigDecimal.valueOf(0.01);
    BigDecimal small_size_0 = BigDecimal.valueOf(0);

    @BeforeClass
    public static void oneTimeSetup() {
        exchange = CoinbaseExchangeFactory.createCoinbaseExchange();
        buySellService = BuySellService.getInstance();
        orderService = new OrderService();
    }

    @AfterClass
    public static void oneTimeTearDown() {
        System.out.println("Clean up | Order Tests");
    }

    @Test
    public void buyAll(){
        buySellService.updateCurrentBalance();
        BigDecimal usd = buySellService.currentUsdBalance;
        BigDecimal buy = buySellService.priceService.getBuyPrice();
        BigDecimal t = BigDecimal.valueOf((usd.doubleValue())/buy.doubleValue());
        buySellService.changeBtcBalanceTo(BigDecimal.valueOf(0.08));

    }
//    @Test
    public void testBuy() {
        buySellService.changeBtcBalanceTo(BigDecimal.valueOf(0.03));
    }

//    @Test
    public void testSell() {
        buySellService.changeBtcBalanceTo(small_size_0);
    }

//    @Test
    public void testCancelledOrder() {
        buySellService.changeBtcBalanceTo(BigDecimal.valueOf(0.03));
    }

    //@Test
    public void testFilledOrder() {
        Order o1 = orderService.getOrder("dfd05e50-98e1-438c-9eb9-aad7e4d5860a");
    }

    //@Test
    public void getOrderStatus() throws Exception {
        Order order = exchange.createOrder(
                new LimitOrder(
                        new BigDecimal(600), small_size_00_1, "tryBuy"));
        orderService.tryCancelOrder(order.getId());
        System.out.println();
    }

    //@Test
    public void simpleMarketOrderTest() {
        try {
            Order[] orders;
            Product[] products = exchange.getProducts();
            Account[] accounts = exchange.getAccounts();
            exchange.cancellAllOrders();
            orders = exchange.getOpenOrders();
            Assert.assertEquals(orders.length, 0);

            Order order = exchange.createOrder(
                    new LimitOrder(
                            new BigDecimal(600), small_size_00_1, "tryBuy"));

            Order o1 = exchange.getOrder(order.getId());
            exchange.cancelOrder(order.getId());
            Assert.assertEquals(o1.getId(), order.getId());

            exchange.createOrder(
                    new LimitOrder(
                            new BigDecimal(800), small_size_00_1, "trySell"));

            orders = exchange.getOpenOrders();
            for (Order od : orders)
                exchange.cancelOrder(od.getId());

        } catch (Exception e) {
            e.printStackTrace();
            fail("simpleMarketOrderTest FAIL");
        }
    }
}
