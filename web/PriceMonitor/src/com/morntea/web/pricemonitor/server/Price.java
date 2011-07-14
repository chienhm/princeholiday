package com.morntea.web.pricemonitor.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Price {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long priceId;
    
    @Persistent
    private Long productId;
    
    @Persistent
    private Date time;
    
    @Persistent
    private Double price;
    
    @Persistent
    private String note;
    
    public Price(Long productId, Date time, Double price, String note) {
        this.productId = productId;
        this.time = time;
        this.price = price;
        this.note = note;
    }
    
    public Long getPriceId() {
        return this.priceId;
    }
    public Long getProductId() {
        return this.productId;
    }
    public Date getTime() {
        return this.time;
    }
    public Double getPrice() {
        return this.price;
    }
    public String getNote() {
        return this.note;
    }
}