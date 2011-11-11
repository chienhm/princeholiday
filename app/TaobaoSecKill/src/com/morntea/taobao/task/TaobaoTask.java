package com.morntea.taobao.task;

import java.util.Date;

import com.morntea.helper.DateHelper;
import com.morntea.taobao.TaobaoUser;


public class TaobaoTask {
	
	public void execute(){}

	public TaobaoTask(TaobaoUser user) {
		this(user, "");
	}

	public TaobaoTask(TaobaoUser user, String description) {
		this(user, description, DateHelper.getDate(2099, 12, 31));
	}
	
	public TaobaoTask(TaobaoUser user, String description, Date endDate) {
		this.description = description;
		this.endDate = endDate;
		this.user = user;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	protected TaobaoUser user;
	protected String description;
	public Date endDate = DateHelper.getDate(2099, 12, 31);
}
