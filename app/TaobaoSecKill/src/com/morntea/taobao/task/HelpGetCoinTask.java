package com.morntea.taobao.task;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.morntea.helper.Http;
import com.morntea.taobao.TaobaoUser;

public class HelpGetCoinTask extends TaobaoTask {
	private final int MAX_HELP_NUM = 4;
	public HelpGetCoinTask(TaobaoUser user) {
		super(user);
	}

	public HelpGetCoinTask(TaobaoUser user, String description) {
		super(user, description);
	}

	@Override
	public void execute() {
		String json = user
				.visit("http://fx.taobao.com/share/find_friend.htm?_tb_token_="
						+ user.getToken());
		// System.out.println(json);
		Pattern pattern = Pattern.compile("\"id\":\"(\\d+)\"");
		Matcher match = pattern.matcher(json);
		Map<String, String> data = new HashMap<String, String>();
		data.put("takeIds", "");
		data.put("_tb_token_", user.getToken());
		Http http = user.http;//new Http(user.getHttpClient());
		int i = 0;
		while (match.find() && i < MAX_HELP_NUM) {
			String id = match.group(1);
			// System.out.println(data);
			if (http.get(
					"http://taojinbi.taobao.com/ajax/take/check_take.htm?method=checkTakenUser&takenUserId="
							+ id
							+ "&takenNum=1&t="
							+ System.currentTimeMillis()).indexOf("\"false\"") != -1) {
				System.out.println(id + " has already got coins.");
			} else {
				i++;
				data.put("takeIds", id);
				String temp = http.post(
						"http://taojinbi.taobao.com/ajax/take/coin_take.htm",
						data);
				if (temp.indexOf("true") != -1) {
					System.out.println(user.getUserName() + " helps " + id
							+ " to get coin successfully.");
				}
			}
		}
	}

}
