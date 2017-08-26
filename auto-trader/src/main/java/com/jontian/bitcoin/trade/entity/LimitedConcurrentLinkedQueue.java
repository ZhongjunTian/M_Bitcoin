package com.jontian.bitcoin.trade.entity;


import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhongjun on 10/22/16.
 */
public class LimitedConcurrentLinkedQueue extends ConcurrentLinkedQueue<Double> {
    private LimitedConcurrentLinkedQueue(){}
    private volatile int maxSize;
    public LimitedConcurrentLinkedQueue(int maxSize){
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(Double o) {
        boolean b = super.add(o);
        while(this.size() > maxSize){
            this.poll();
        }
        return b;
    }

    public double getAveragePrice(){
        if(this.size() == 0)
            throw new IllegalStateException("0 size queue have no avg price");
        double price = 0;
        for(Double p : this){
            price += p;
        }
        return price/this.size();
    }

}
