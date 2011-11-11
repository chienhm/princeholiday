package com.morntea.taobao.task;

import java.util.Date;
import java.util.Map;

import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableTag;

import com.morntea.helper.ComplexFilter;
import com.morntea.helper.HtmlParserHelper;
import com.morntea.helper.Http;
import com.morntea.taobao.TaobaoUser;

public class ReplyPostTask extends TaobaoTask {

	public ReplyPostTask(TaobaoUser user) {
		super(user);
	}

	public ReplyPostTask(TaobaoUser user, String description) {
		super(user, description);
	}
	
	public ReplyPostTask(TaobaoUser user, String description, Date endDate) {
		super(user, description, endDate);
	}

	@Override
	public void execute() {
		if (!user.isVip()) {
			System.err
					.println("Sorry, "
							+ user.getUserName()
							+ " is not vip. Only vip does not need to input verification code.");
			return;
		}
		System.out.println(user.getUserName() + " replys a post.");
		String url = getFirstPost(user
				.visit("http://bbs.taobao.com/catalog/424020.htm"));
		System.out.println(url);
		String html = user.visit(url);
		// System.out.println(html);
		Map<String, String> data = HtmlParserHelper.parseForm(html, "id",
				"addReplyForm");
		data.put("_fmw.publis._0.c", "<p>先顶起来再说，UP!Up!</p>");
		// data.put("_fmw.publis._0.ch", "3E4B");
		System.out.println(data.toString());
		Http http = user.http;
		http.post(url, data);
		// System.out.println();
	}

	public String getFirstPost(String html) {
		if(html==null)return null;
		String url = null;
		ComplexFilter filters[] = {
				new ComplexFilter(new AndFilter(new NodeClassFilter(
						TableTag.class), new HasAttributeFilter("class",
						"posts")), 1),
				new ComplexFilter(new NodeClassFilter(LinkTag.class), 2) };
		LinkTag link = (LinkTag) HtmlParserHelper.getNode(html, filters);
		if (link != null)
			url = link.extractLink();
		return url;
	}
}
