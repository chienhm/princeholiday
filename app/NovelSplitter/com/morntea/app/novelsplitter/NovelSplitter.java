package com.morntea.app.novelsplitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class NovelSplitter {
	final String HEADER = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>${title}</title></head><body>";
	final String FOOTER = "</body></html>";
	private int pageSize;
	public NovelSplitter() {
		this(500);
	}
	public NovelSplitter(int pageSize) {
		super();
		this.pageSize = pageSize;
	}
	public void split(String fileName, String outputFolder){
        BufferedReader reader = null;
        StringBuffer sb = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "gb2312"));
            sb = new StringBuffer();
            String tempString = null;
            int line = 1;
            int pageNumber = 1;
            while ((tempString = reader.readLine()) != null) {
            	sb.append(tempString + "<br>");
            	if(sb.length()>pageSize) {
            		writeToFile(outputFolder + "\\" + pageNumber + ".html", pageNumber, sb.toString());
            		pageNumber++;
            		sb = new StringBuffer();
            	}
                //System.out.println("line " + line + ": " + tempString);
                line++;
                //break;
            }
            reader.close();
            writeIndex(outputFolder + "\\0.html", pageNumber-1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	}
	
	private void writeToFile(String fileName, int pageNumber, String body) {
		try {
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(HEADER.replace("${title}", "Page " + pageNumber));
            writer.write(body);
            writer.write("<a href=\"" + (pageNumber-1) + ".html\">" + (pageNumber-1) + "</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
            		"<a href=\"0.html\">index</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
            		"<a href=\"" + (pageNumber+1) + ".html\">" + (pageNumber+1) + "</a>");
            writer.write(FOOTER);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void writeIndex(String fileName, int maxPage) {
		StringBuffer sb = new StringBuffer();
		for(int i=1; i<=maxPage; i++) {
			sb.append("<a href=\"" + i + ".html\">" + i + "</a>");
			if(i%3 == 0) {
				sb.append("<br>");
			} else {
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			}
		}
		try {
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(HEADER.replace("${title}", "Index"));
            writer.write(sb.toString());
            writer.write(FOOTER);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
