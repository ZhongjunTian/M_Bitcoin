package com.jontian.bitcoin.trade.entity;

import java.math.BigDecimal;

/**
 * Created by irufus on 2/18/15.
 */
public class Order {
    private String id;
    private BigDecimal price;
    private BigDecimal size;
    private String product_id;
    private String side;
    private String type;
    private String created_at;
    private String done_at;
    private String done_reason;
    private BigDecimal filled_size;
    private BigDecimal fill_fees;
    private String status;
    private Boolean settled;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDone_at() {
        return done_at;
    }

    public void setDone_at(String done_at) {
        this.done_at = done_at;
    }

    public String getDone_reason() {
        return done_reason;
    }

    public void setDone_reason(String done_reason) {
        this.done_reason = done_reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getFilled_size() {
        return filled_size;
    }

    public void setFilled_size(BigDecimal filled_size) {
        this.filled_size = filled_size;
    }

    public BigDecimal getFill_fees() {
        return fill_fees;
    }

    public void setFill_fees(BigDecimal fill_fees) {
        this.fill_fees = fill_fees;
    }

    public Boolean getSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
