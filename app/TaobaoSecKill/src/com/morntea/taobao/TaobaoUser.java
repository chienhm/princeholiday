package com.morntea.taobao;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;

import com.morntea.helper.ConsoleLog;
import com.morntea.helper.DateHelper;
import com.morntea.helper.HtmlParserHelper;
import com.morntea.helper.Http;
import com.morntea.helper.StringHelper;
import com.morntea.taobao.task.TaobaoTask;

public class TaobaoUser {

	private String username = null;
	private String password = null;
	private String token = null;
	private boolean vip = false;
	private boolean login = false;
	private String userid;
	public Http http = null;

	final static String USER_HOME_URL = "http://i.taobao.com";
	final static String LOGIN = "https://login.taobao.com";
	final static String LOGIN_URL = "https://login.taobao.com/member/login.jhtml";
	final static String LOGOUT_URL = "http://login.taobao.com/member/logout.jhtml";

	public TaobaoUser(String username, String password) {
		this (username, password, false);
	}
	
	public TaobaoUser(String username, String password, boolean isVip) {
		super();
		this.username = username;
		this.password = password;
		this.vip = isVip;
	}

	public String getUserName() {
		return username;
	}

	public String getToken() {
		return token;
	}

	public void login() {
		if(http==null)http = new Http(new DefaultHttpClient());
		/* First visit login page to get token */
		this.visit(LOGIN);
		token = http.getCookie("_tb_token_");
		// System.out.println(token);

		Map<String, String> data = new HashMap<String, String>();
		data.put("TPL_username", username);
		data.put("TPL_password", password);
		data.put("TPL_checkcode", "");
		data.put("need_check_code", "");
		data.put("_tb_token_", token);
		data.put("action", "Authenticator");
		data.put("event_submit_do_login", "anything");
		data.put("TPL_redirect_url", "");
		data.put("from", "tb");
		data.put("fc", "2");
		data.put("style", "default");
		data.put("css_style", "");
		data.put("tid", "");
		data.put("support", "000001");
		data.put("CtrlVersion", "1,0,0,7");
		data.put("loginType", password.startsWith("3DES")?"4":"3");
		data.put("minititle", "");
		data.put("minipara", "");
		data.put("umto", "");
		data.put("pstrong", "2");
		data.put("llnick", "");
		data.put("sign", "");
		data.put("need_sign", "");
		data.put("isIgnore", "");
		data.put("full_redirect", "");
		data.put("popid", "");
		data.put("callback", "");
		data.put("guf", "");
		data.put("not_duplite_str", "");
		data.put("need_user_id", "");
		data.put("poy", "");
		data.put("gvfdcname", "10");
		data.put("gvfdcre", "");
		data.put("from_encoding", "");

		http.post(LOGIN_URL, data);

		this.login = true;
		String html = this.visit(USER_HOME_URL);
		//System.out.print(html);
		userid = StringHelper.regFetch(html, "userid=(\\d+)");
		if (html.indexOf("Œ“µƒÃ‘±¶") != -1) {
			this.login = true;
			ConsoleLog.log(this.username + " login success.");
		}

	}

	public void logout() {
		if (http == null) {
			return;
		}
		this.visit(LOGOUT_URL);
		http.terminate();
		http = null;
		this.login = false;
		ConsoleLog.log(this.username + " logout.");
	}

	public String visit(String url) {
		if(url==null || http==null)throw new NullPointerException();
		return http.get(url);
	}

	public void runTask(TaobaoTask task) {
		if (this.login) {
			if(DateHelper.inTheFuture(task.endDate)) {
				System.out.println(this.username + " run task: " + task.getDescription());
				task.execute();
			} else {
				System.err.println("This task is out of date.");
			}
		} else {
			System.err.println(this.username + " doesn't login.");
		}
	}
	
	public void buyTmallCommodity(String id) {
		Commodity c = new Commodity();
		c.http = http;
		Map<String, String> data = c.getData(id);

		String html = null;
		html = http.post("http://buy.tmall.com/order/confirm_order.htm", data);
		System.out.println(html);
	}
	
	public void buyCommodity(String id) {
		Commodity c = new Commodity();
		c.http = http;
		Map<String, String> data = c.getData(id);
		ConsoleLog.log(data);

		String html = null;
		html = http.post("http://buy.taobao.com/auction/buy_now.jhtml", data);
		//System.out.println(html);
		data = HtmlParserHelper.parseForm(html, "id", "J_Form");
		ConsoleLog.log(data);
		html = http.post("http://buy.taobao.com/auction/order/unity_order_confirm.htm", data);
		System.out.println(html);
	}

	public void setVip(boolean isVip) {
		this.vip = isVip;
	}

	public boolean isVip() {
		return vip;
	}

	public boolean isLogin() {
		return login;
	}

	public String getUserid() {
		return userid;
	}
}
