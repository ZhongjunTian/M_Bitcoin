package com.jontian.bitcoin.price.dao;

import com.google.common.base.MoreObjects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by zhongjun on 9/11/16.
 */
@Entity
@SuppressWarnings("unused")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double buy;
    private double buyAmount;
    private double sell;
    private double sellAmount;
    private long time;

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
//                .add("service",market)
                .add("buy",buy)
                .add("sell",sell)
//                .add("buy1",buy1)
//                .add("buy10",buy10)
//                .add("buyWeighted",buyWeighted)
//                .add("buyAmount",buyAmount)
//                .add("sell1",sell1)
//                .add("sell10",sell10)
//                .add("sellWeighted",sellWeighted)
//                .add("sellAmount",sellAmount)
                .toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public long getTime() {
        return time;
    }

    public Price setTime(long time) {
        this.time = time;
        return this;
    }

    public double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(double buyAmount) {
        this.buyAmount = buyAmount;
    }

    public double getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(double sellAmount) {
        this.sellAmount = sellAmount;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }
}
