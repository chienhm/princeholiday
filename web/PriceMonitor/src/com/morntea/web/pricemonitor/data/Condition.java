package com.morntea.web.pricemonitor.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable="true")
public class Condition {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent
    private int type;
    @Persistent
    private String parameter;
    @Persistent
    private boolean meet;
    @Persistent
    private Email email;
    @Persistent
    private String phone;
    @Persistent
    private ProductItem item;
    
	public Key getKey() {
		return key;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setMeet(boolean meet) {
		this.meet = meet;
	}

	public boolean isMeet() {
		return meet;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Email getEmail() {
		return email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setItem(ProductItem item) {
		this.item = item;
	}

	public ProductItem getItem() {
		return item;
	}
}
