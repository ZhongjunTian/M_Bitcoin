package com.jontian.bitcoin.price.dao;

import com.google.common.base.MoreObjects;

/**
 * Created by zhongjun on 9/22/16.
 */
public class Order {
    private double price = 0;
    private double amount = 0;

    public Order(double price, double amount) {
        this.price = price;
        this.amount = amount;
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("price",price)
                .add("amount",amount)
                .toString();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
