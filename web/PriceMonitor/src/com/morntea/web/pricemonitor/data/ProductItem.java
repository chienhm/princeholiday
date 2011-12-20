package com.morntea.web.pricemonitor.data;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class ProductItem {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    @Persistent
	private String url;
    @Persistent
	private float price = -1;
    @Persistent
    private Date fetchDate = new Date();
    @Persistent(mappedBy = "item")
    private List<Condition> conditions;
    @Persistent
    private int errorTimes = 0;
    
	public ProductItem(String url) {
		super();
		this.url = url;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	public float getPrice() {
		return price;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public Long getId() {
		return id;
	}

	public void setFetchDate(Date fetchDate) {
		this.fetchDate = fetchDate;
	}

	public Date getFetchDate() {
		return fetchDate;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setErrorTimes(int errorTimes) {
		this.errorTimes = errorTimes;
	}

	public int getErrorTimes() {
		return errorTimes;
	}
}
