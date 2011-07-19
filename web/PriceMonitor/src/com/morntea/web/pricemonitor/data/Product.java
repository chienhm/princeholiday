package com.morntea.web.pricemonitor.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Product {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long productId;
    
    @Persistent
    private String pid;
    
    @Persistent
    private String sid;
    
    @Persistent
    private String type;
    
    @Persistent
    private String name;
/*    
    @Persistent
    private Long referenceCount;*/
    
    public Product(String type, String pid, String sid, String name) {
        this.type = type;
        this.pid = pid;
        this.sid = sid;
        this.name = name;
    }
    
    public Long getProductId() {
        return this.productId;
    }
    public String getPid() {
        return this.pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getSid() {
        return this.sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
/*    public Long getReferenceCount() {
        return this.referenceCount;
    }
    public void increaseReferenceCount() {
        this.referenceCount++;
    }
    public void decreaseReferenceCount() {
        this.referenceCount--;
    }*/
}
