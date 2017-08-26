package com.jontian.bitcoin.price.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhongjun on 9/22/16.
 */
public class PriceBuilder {
    private ArrayList<Order> sellOrders = new ArrayList<>();
    private ArrayList<Order> buyOrders = new ArrayList<>();
    private long time;

    public boolean addSell(double price, double amount) {
        return sellOrders.add(new Order(price, amount));
    }

    public boolean addBuy(double price, double amount) {
        return buyOrders.add(new Order(price, amount));
    }


    public Price build() {
        sort(buyOrders, true);
        sort(sellOrders, false);
        double buy = buyOrders.get(0).getPrice();
        double buy1 = calculateAvgPriceByAmount(buyOrders, 1, true);
        double buy10 = calculateAvgPriceByAmount(buyOrders, 10, true);
        double[] t0 = calculateWeightedPrice(buyOrders);
        double buyWeighted = t0[0];
        double buyAmount = t0[1];
        double sell = sellOrders.get(0).getPrice();
        double sell1 = calculateAvgPriceByAmount(sellOrders, 1, false);
        double sell10 = calculateAvgPriceByAmount(sellOrders, 10, false);
        double[] t1 = calculateWeightedPrice(sellOrders);
        double sellWeighted = t1[0];
        double sellAmount = t1[1];
        Price price = new Price();
        price.setBuy(buy);
        price.setBuyAmount(buyAmount);
        price.setSell(sell);
        price.setSellAmount(sellAmount);

        return price;
    }

    /*
        Input: orders    Output:double[WeightedAvgPrice, totalAmount]
     */
    private double[] calculateWeightedPrice(List<Order> orders) {
        double totalPrice = 0;
        double totalAmount = 0;
        int n = 0;
        for (Order order : orders) {
            if (order != null) {
                totalAmount += order.getAmount();
                totalPrice += order.getPrice() * order.getAmount();
                n++;
            }
        }
        if (totalAmount == 0)
            return null;
        return new double[]{totalPrice / totalAmount, totalAmount};
    }

    /*
        Input: orders
    */
    private double calculateAvgPriceByAmount(List<Order> orders, double amount, boolean isBuy) {
        if(amount <=0 || orders==null || orders.size()==0){
            throw new IllegalStateException("wrong input");
        }
        sort(orders, isBuy);
        double current = 0;
        double target = amount;
        double cost = 0;
        for (Order order : orders) {
            if (order != null) {
                if (current + order.getAmount() < target) {
                    current += order.getAmount();
                    cost += order.getPrice() * order.getAmount();
                } else {
                    //got enough amount
                    double lastRequiredAmount = target - current;
                    cost += order.getPrice() * lastRequiredAmount;
                    return cost/amount;
                }
            }
        }
        //did not get enough amount, assume we can get them all based on the last price(it's also the worst price)
        double lastPrice = orders.get(orders.size() - 1).getPrice();
        double lastRequiredAmount = target - current;
        cost += lastPrice * lastRequiredAmount;
        return cost/amount;
    }

    private void sort(List<Order> orders, boolean isBuy) {
        orders.removeAll(Collections.singleton(null));
        if (isBuy)
            orders.sort((o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice()));//买 从便宜到贵
        else
            orders.sort((o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice()));
    }
}
