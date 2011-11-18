package com.morntea.taobao.task;

import java.util.HashMap;
import java.util.Map;

import com.morntea.helper.Http;
import com.morntea.taobao.TaobaoUser;

public class WeiboTask extends TaobaoTask {
	
	public WeiboTask(TaobaoUser user, String description) {
		super(user, description);
	}

	public void reply(String weibo_id, String feed_id, String comment) {
		Http http = user.http;
		Map<String, String> data = new HashMap<String, String>();
		/*data.put("page", "1");
		data.put("vfeedTabId", "-1");
		data.put("userId", "374732528");*/
		
		data.put("target_key", feed_id);
		data.put("type_id", "11");
		data.put("feed_id", feed_id);
		data.put("rec_user_id", weibo_id);
		data.put("needNotice", "true");
		data.put("title", "转发");
		String html = null;
		/*int replyNum = 0;
		do {
//			html = http.post("http://jianghu.taobao.com/admin/home/v_front_my_feeds.htm?_input_charset=utf-8&t=" + System.currentTimeMillis() + "&_tb_token_=" + user.getToken(), data);
//			//System.out.println(html);
//			String sReplyNumber = StringHelper.regFetch(html, feed_id + "\"}'>评论\\((\\d+)\\)");
//			ConsoleLog.log(sReplyNumber);
//			replyNum = Integer.parseInt(sReplyNumber);			

			html = http.post("http://comment.jianghu.taobao.com/json/cc_list.htm?_input_charset=utf-8&t=" + System.currentTimeMillis() + "&_tb_token_=" + user.getToken(), data);
			//System.out.println(html);
			String sReplyNumber = StringHelper.regFetch(html, "- (\\d+)楼");
			ConsoleLog.log(sReplyNumber);
			replyNum = Integer.parseInt(sReplyNumber);
			
			SystemHelper.sleep(3000);
		} while(replyNum<187);*/
		
		Map<String, String> repost = new HashMap<String, String>();
		repost = new HashMap<String, String>();
		repost.put("tId", "0");
		repost.put("uId", weibo_id);
		repost.put("fId", feed_id);
		repost.put("appId", "1100003");
		repost.put("from", "1");
		repost.put("publishType", "1");
		repost.put("content", comment);
		html = http.post("http://t.taobao.com/weibo/add_weibo_result_4_home.htm?event_submit_do_publish_weibo=1&action=weibo/weiboAction&_input_charset=utf-8&t=" + System.currentTimeMillis() + "&_tb_token_=" + user.getToken(), repost);
		System.out.println(html);

		data.put("content", comment);
		data.put("TPL_checkcode", "");
		html = http.post("http://comment.jianghu.taobao.com/json/add.htm?action=comment/comment_action&event_submit_do_add=true&_input_charset=utf-8&t=" + System.currentTimeMillis() + "&_tb_token_=" + user.getToken(), data);
		System.out.println(html);
	}
	
	public void execute() {
		reply("374732528", "3625583613", "Beautiful, I like it very much!");
	}
}
