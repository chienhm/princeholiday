package com.morntea.taobao.task;

import java.util.Date;

import com.morntea.helper.ConsoleLog;
import com.morntea.taobao.TaobaoUser;

public class KangshifuTask extends TaobaoTask {

	public KangshifuTask(TaobaoUser user, String description, Date endDate) {
		super(user, description, endDate);
	}
	public void execute() {
		String html;
		html = user.visit("http://msp.taobao.com/pc/ksf/sendSnsCoin.do?callback=handledata&_=" + System.currentTimeMillis());
		ConsoleLog.log(html);
		html = user.visit("http://msp.taobao.com/pc/ksf/getSnsCoin.do?callback=jQuery1607788366617169231_"+System.currentTimeMillis()+"&_=" + System.currentTimeMillis());
		ConsoleLog.log(html);
	}
}
