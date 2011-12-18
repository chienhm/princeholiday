package com.morntea.web.pricemonitor.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Email;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Condition {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
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
    
	public Long getId() {
		return id;
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
}
