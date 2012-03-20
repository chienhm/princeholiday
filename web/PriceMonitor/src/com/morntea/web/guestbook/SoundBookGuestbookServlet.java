package com.morntea.web.guestbook;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SoundBookGuestbookServlet extends HttpServlet {
	private static final long serialVersionUID = 2584137171752358810L;
	
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
        String content = req.getParameter("content");
        GusetbookService gs = new GusetbookService();
        gs.addMessage(content, 1);
        resp.sendRedirect("/seacrystal/guestbook.jsp");
    }
}
