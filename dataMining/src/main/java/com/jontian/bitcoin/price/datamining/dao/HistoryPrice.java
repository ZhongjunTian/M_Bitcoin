package com.jontian.bitcoin.price.datamining.dao;

import com.jontian.bitcoin.price.datamining.service.Util;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by zhongjun on 10/16/16.
 */
/*
https://api.gdax.com/products/BTC-USD/candles?start=2016-09-28T21:00:00&end=2016-09-28T22:00:00&granularity=60
[
  [
    1475099940, //  time bucket start time
    605.01,     //low lowest price during the bucket interval
    605.01,     //high highest price during the bucket interval
    605.01,     //open opening price (open trade) in the bucket interval
    605.01,     //close closing price (close trade) in the bucket interval
    1.20063802  //volume volume of trading activity during the bucket interval
  ],
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"time"})})
public class HistoryPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private long time;

    private String timeUTC;
    private double lowest;
    private double highest;
    private double open;
    private double close;
    private double volume;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        timeUTC = Util.getUtc(time);
        this.time = time;
    }

    public double getLowest() {
        return lowest;
    }

    public void setLowest(double lowest) {
        this.lowest = lowest;
    }

    public double getHighest() {
        return highest;
    }

    public void setHighest(double highest) {
        this.highest = highest;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getTimeUTC() {
        return timeUTC;
    }
}
