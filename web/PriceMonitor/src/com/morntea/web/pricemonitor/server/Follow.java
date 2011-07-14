package com.morntea.web.pricemonitor.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Follow {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long followId;
    
    @Persistent
    private Long userId;
    
    @Persistent
    private Long productId;
    
    @Persistent
    private Integer level;
    
    public Follow(Long userId, Long productId, Integer level) {
        this.userId = userId;
        this.productId = productId;
        this.level = level;
    }
    public Long getFollowId() {
        return this.followId;
    }
    public Long getUserId() {
        return this.userId;
    }
    public Long getProductId() {
        return this.productId;
    }
    public Integer getLevel() {
        return this.level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
}