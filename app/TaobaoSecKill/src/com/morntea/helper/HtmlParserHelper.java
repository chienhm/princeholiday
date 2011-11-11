package com.morntea.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.FormTag;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HtmlParserHelper {
	public String getAttribute(String element, String attrName) {
		String attrValue = "";
		/*
		 * such as <input name = "age" > <input name="age"/> <input value = 123>
		 * <input value=123 />
		 */
		Pattern pattern = Pattern.compile(" " + attrName
				+ "\\s*=\\s*(\"([^\"]+)\"|([^>\\s]+)[\\s>])",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(element);
		if (matcher.find()) {
			attrValue = matcher.group(2);
			if (attrValue == null)
				attrValue = matcher.group(3);
		}

		return attrValue;
	}

	/**
	 * Get all form elements' name and value from html. The form is specified by
	 * its attribute name and value, such as a form with id attribute. The form
	 * elements include input, textarea, select, etc.
	 * 
	 * @param html
	 *            html string that contains form element
	 * @param attrName
	 *            form attribute name
	 * @param attrValue
	 *            form attribute value
	 * @return name value pair in map format.
	 */
	public static Map<String, String> parseForm(String html, String attrName,
			String attrValue) {
		/*
		 * Parser parser = null; NodeList formNodes = null; try { parser = new
		 * Parser(html); formNodes = parser .parse(new AndFilter(new
		 * NodeClassFilter(FormTag.class), new HasAttributeFilter(attrName,
		 * attrValue))); } catch (ParserException e) { e.printStackTrace();
		 * return null; }
		 */

		Map<String, String> nvpair = new HashMap<String, String>();
		ComplexFilter cf[] = { new ComplexFilter(new AndFilter(
				new NodeClassFilter(FormTag.class), new HasAttributeFilter(
						attrName, attrValue)), 0) };
		FormTag formNode = (FormTag) getNode(html, cf);
		if (formNode != null) {
			// System.out.println(formNode.toHtml());

			NodeList formElements = formNode.getFormInputs();
			for (int i = 0; i < formElements.size(); i++) {
				InputTag input = (InputTag) formElements.elementAt(i);
				String inputName = input.getAttribute("name");
				String inputValue = input.getAttribute("value");
				addToMap(nvpair, inputName, inputValue);
				// System.out.println(inputName + "=" + inputValue);
			}

			formElements = formNode.getFormTextareas();
			for (int i = 0; i < formElements.size(); i++) {
				TextareaTag textarea = (TextareaTag) formElements.elementAt(i);
				String inputName = textarea.getAttribute("name");
				String inputValue = textarea.getValue();
				// System.out.println(inputName + "=" + inputValue);
				addToMap(nvpair, inputName, inputValue);
			}

			NodeList selectList = new NodeList();
			formNode.collectInto(selectList, new NodeClassFilter(SelectTag.class));
			for (int i = 0; i < selectList.size(); i++) {
				SelectTag select = (SelectTag) selectList.elementAt(i);
				String selectName = select.getAttribute("name");
				String selectValue = null, defaultValue = null;
				OptionTag[] options = select.getOptionTags();
				for(OptionTag option : options) {
					if(defaultValue == null)defaultValue = option.getValue().trim();
					if(option.getAttribute("selected").trim().equalsIgnoreCase("selected")){
						selectValue = option.getValue().trim();
					}
				}
				if(selectValue==null) {
					selectValue = defaultValue;
				}
				addToMap(nvpair, selectName, selectValue);
			}
		}
		return nvpair;
	}

	private static void addToMap(Map<String, String> map, String name,
			String value) {
		if (name != null && !name.isEmpty()) {
			if (value == null) {
				value = "";
			}
			map.put(name, value);
		}
	}

	public static Node getNode(String html, ComplexFilter filters[]) {
		Parser parser = null;
		NodeList nodeList = null;
		try {
			parser = new Parser(html);
			nodeList = parser.parse(new NodeClassFilter(Html.class));
		} catch (ParserException e) {
			e.printStackTrace();
			return null;
		}

		Node htmlNode, node = null;
		if (nodeList != null && nodeList.size() > 0) {
			htmlNode = nodeList.elementAt(0);
			node = getNode(htmlNode, filters);
		}
		return node;
	}

	public static Node getNode(Node root, ComplexFilter filters[]) {
		Node node = root;
		for (int i = 0; i < filters.length; i++) {
			NodeList list = new NodeList();
			ComplexFilter filter = filters[i];
			node.collectInto(list, filter.filter);
			if (list.size() > 0 && list.size() > filter.index) {
				node = list.elementAt(filter.index);
			} else {
				System.err.println("Can't find element at index "
						+ filter.index + ":\n" + node.getText());
				return null;
			}
		}
		if (node == root)
			node = null;
		return node;
	}

}
