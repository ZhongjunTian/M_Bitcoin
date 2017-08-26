package com.jontian.bitcoin.trade.entity;

import java.math.BigDecimal;

/**
 * Created by irufus on 7/31/15.
 */
public class LimitOrder {
    private BigDecimal price;
    private BigDecimal size;
    private String side;
    private String type = "limit";

    private String product_id = "BTC-USD";
    private LimitOrder(){
    }
    public LimitOrder(BigDecimal price, BigDecimal size, String side){
        if(!side.equals("buy") && !side.equals("sell"))
            throw new IllegalStateException();
        if(price.doubleValue() < 500 || size.doubleValue() > 100)
            throw new IllegalStateException("impossible size or price");
        setPrice(price);
        setSize(size);
        setSide(side);
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }
}
