package com.morntea.web.family;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemberServlet extends HttpServlet {

	private static final long serialVersionUID = 8074349473325877818L;
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		MemberService ms = new MemberService();
		ms.addMember("test", -1L);
		resp.sendRedirect("/family/list.jsp");
	}
}
