package com.morntea.taobao.task;

import java.util.Date;

import com.morntea.helper.DateHelper;
import com.morntea.taobao.TaobaoUser;

public class SimpleTask extends TaobaoTask {

	private String url;
	public SimpleTask(TaobaoUser user, String url) {
		this(user, url, "");
	}
	
	public SimpleTask(TaobaoUser user, String url, String description) {
		this(user, url, description, DateHelper.getDate(2099, 12, 31));
	}
	
	public SimpleTask(TaobaoUser user, String url, String description, Date endDate) {
		super(user, description, endDate);
		this.url = url;
	}
	
	public void execute() {
		user.visit(url);
	}

}
