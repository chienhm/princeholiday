package com.morntea.helper;

import org.htmlparser.NodeFilter;


public class ComplexFilter {
	public ComplexFilter(NodeFilter filter, int index) {
		super();
		this.filter = filter;
		this.index = index;
	}

	public NodeFilter filter;
	public int index;
}
