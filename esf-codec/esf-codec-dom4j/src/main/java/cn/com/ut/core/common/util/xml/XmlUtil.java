package cn.com.ut.core.common.util.xml;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import cn.com.ut.core.common.constant.EnumConstant;
import cn.com.ut.core.common.util.CollectionUtil;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.converter.TypeConvert;

/**
 * xml工具类
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:27:50
 */
public class XmlUtil {

	/**
	 * 根据指定路径获取Document
	 * 
	 * @param name
	 * @return Document
	 * @throws DocumentException
	 */
	public static Document readXml(String name) throws DocumentException {

		URL url = XmlUtil.class.getClassLoader().getResource(name);
		return readXml(url);
	}

	/**
	 * 根据指定url获取Document
	 * 
	 * @param url
	 * @return Document
	 * @throws DocumentException
	 */
	public static Document readXml(URL url) throws DocumentException {

		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}

	/**
	 * 根据指定file获取Document
	 * 
	 * @param file
	 * @return Document
	 * @throws DocumentException
	 */
	public static Document readXml(File file) throws DocumentException {

		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		return document;
	}

	/**
	 * 根据指定InputStream获取Document
	 * 
	 * @param input
	 * @return Document
	 * @throws DocumentException
	 */
	public static Document readXml(InputStream input) throws DocumentException {

		if (input == null) {
			return null;
		}
		SAXReader reader = new SAXReader();
		Document document = reader.read(input);
		return document;
	}

	/**
	 * 根据指定InputStream和指定的编码方式获取Document
	 * 
	 * @param input
	 * @param charsetName
	 * @return Document
	 * @throws DocumentException
	 */
	public static Document readXml(InputStream input, String charsetName) throws DocumentException {

		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(input, charsetName);
		} catch (Exception e) {
			throw new DocumentException(e);
		}
		SAXReader reader = new SAXReader();
		Document document = reader.read(isr);
		return document;
	}

	/**
	 * 根据指定的xml字符串创建Document
	 * 
	 * @param text
	 * @return Document
	 * @throws DocumentException
	 */
	public static Document createDocument(String text) throws DocumentException {

		if (CommonUtil.isEmpty(text))
			return null;

		Document document = DocumentHelper.parseText(text.trim());
		return document;
	}

	/**
	 * 将指定的流以指定的格式写入指定的文档
	 * 
	 * @param doc
	 * @param out
	 * @param format
	 * @throws Exception
	 */
	public static void writeXml(Document doc, OutputStream out, OutputFormat format)
			throws Exception {

		if (format == null)
			format = OutputFormat.createCompactFormat();

		if (out == null)
			out = System.out;

		XMLWriter writer = new XMLWriter(out, format);
		writer.write(doc);
		writer.close();
	}

	/**
	 * element to document
	 * 
	 * @param e
	 * @return Document
	 */
	public static Document ElementToDocument(Element e) {

		if (e == null)
			return null;
		e.setParent(null);
		return DocumentHelper.createDocument(e);
	}

	/**
	 * 将Element的属性映射到java对象
	 * 
	 * @param element
	 * @param t
	 * @return 返回生成的对象
	 */
	public static <T> T newElement(Element element, T t) {

		if (element == null || t == null)
			return t;

		List<Attribute> attributes = element.attributes();

		for (Attribute attribute : attributes) {
			try {
				BeanUtils.setProperty(t, attribute.getName(), attribute.getValue());
			} catch (IllegalAccessException | InvocationTargetException e) {
				continue;
			}
		}

		return t;
	}

	/**
	 * 
	 * @param element
	 * @param clazz
	 * @return object
	 */
	public static <T> T toElement(Element element, Class<T> clazz) {

		return toElement(element, clazz, null);
	}

	/**
	 * 将Element的属性映射到指定的java对象
	 * 
	 * @param element
	 * @param clazz
	 * @param text
	 * @return java object or null
	 */
	public static <T> T toElement(Element element, Class<T> clazz, String text) {

		if (element == null || clazz == null)
			return null;

		List<Attribute> attributes = element.attributes();

		T t = null;
		try {
			t = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}

		if (attributes != null) {
			for (Attribute attribute : attributes) {
				try {
					BeanUtils.setProperty(t, attribute.getName(), attribute.getValue());
				} catch (IllegalAccessException | InvocationTargetException e) {
					continue;
				}
			}
		}

		if (!CommonUtil.isEmpty(text)) {
			try {
				String temp = BeanUtils.getProperty(t, text);
				if (CommonUtil.isEmpty(temp))
					BeanUtils.setProperty(t, text, element.getTextTrim());
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				;
			}
		}

		return t;
	}

	/**
	 * 
	 * @param elements
	 * @param clazz
	 * @return List<T>
	 */
	public static <T> List<T> toElementList(List<?> elements, Class<T> clazz) {

		return toElementList(elements, clazz, null);
	}

	/**
	 * 
	 * @param elements
	 * @param clazz
	 * @param text
	 * @return List<T>
	 */
	public static <T> List<T> toElementList(List<?> elements, Class<T> clazz, String text) {

		return toElementList(elements, clazz, text, null);
	}

	/**
	 * 将List<?> 转为List<Map<String, String>>
	 * 
	 * @param elements
	 * @return List<Map<String, String>> or null
	 */
	public static List<Map<String, String>> toMapList(List<?> elements) {

		if (elements == null || elements.isEmpty())
			return null;

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Element element : (List<Element>) elements) {
			List<Attribute> attributes = element.attributes();
			if (attributes != null && !attributes.isEmpty()) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				for (Attribute attribute : attributes) {
					map.put(attribute.getName(), attribute.getValue());
				}
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 将List<Node> 转为List<T>
	 * 
	 * @param elements
	 * @param clazz
	 * @param text
	 * @param defaultValue
	 * @return List<T>
	 */
	public static <T> List<T> toElementList(List<?> elements, Class<T> clazz, String text,
			String defaultValue) {

		if (elements == null || elements.isEmpty())
			return null;
		List<T> fieldList = new ArrayList<T>();
		T requestField = null;
		Element element = null;
		List<Attribute> attributes = null;
		for (Node field : (List<Node>) elements) {
			try {
				requestField = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				return null;
			}
			element = (Element) field;
			attributes = element.attributes();

			if (attributes != null) {
				for (Attribute attribute : attributes) {
					try {
						BeanUtils.setProperty(requestField, attribute.getName(),
								attribute.getValue());
					} catch (IllegalAccessException | InvocationTargetException e) {
						continue;
					}
				}
			}

			if (!CommonUtil.isEmpty(text)) {

				try {
					String temp = BeanUtils.getProperty(requestField, text);

					if (CommonUtil.isEmpty(temp)) {
						if (CommonUtil.isEmpty(defaultValue))
							BeanUtils.setProperty(requestField, text, element.getTextTrim());
						else
							BeanUtils.setProperty(requestField, text, defaultValue);
					}
				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					e.printStackTrace();
				}

			}

			fieldList.add(requestField);
		}
		return fieldList;
	}

	/**
	 * 将List<Element>转成一个java object
	 * 
	 * @param elements
	 * @param object
	 * @return object
	 */
	public static Object propertiesSetter(List<Element> elements, Object object) {

		if (elements == null || object == null)
			return object;
		String value = null;
		for (Element e : elements) {
			if (e.isTextOnly())
				value = e.getTextTrim();
			else
				value = e.asXML();
			try {
				BeanUtils.setProperty(object, e.getName(), value);
			} catch (Exception ex) {
			}
		}

		return object;
	}

	/**
	 * elements setter
	 * 
	 * @param elements
	 * @param object
	 * @return object
	 */
	public static Object elementsSetter(List<Element> elements, Object object) {

		if (elements == null || object == null)
			return object;

		for (Element e : elements) {
			try {
				BeanUtils.setProperty(object, e.getName(), e);
			} catch (Exception ex) {
			}
		}

		return object;
	}

	/**
	 * map to element
	 * 
	 * @param elementName
	 * @param map
	 * @param upperLowerCase
	 * @return Element
	 */
	public static Element mapToElement(String elementName, Map<String, Object> map,
			EnumConstant.UpperLowerCase upperLowerCase) {

		if (CommonUtil.isEmpty(elementName) || map == null)
			return null;

		Element ele = DocumentHelper.createElement(elementName);

		Set<Entry<String, Object>> entrySet = map.entrySet();
		Entry<String, Object> entry = null;
		Iterator<Entry<String, Object>> iter = entrySet.iterator();
		if (EnumConstant.UpperLowerCase.UPPER.equals(upperLowerCase)) {
			while (iter.hasNext()) {
				entry = iter.next();
				ele.addAttribute(entry.getKey().toUpperCase(),
						TypeConvert.getStringValue(entry.getValue()));
			}
		} else if (EnumConstant.UpperLowerCase.LOWER.equals(upperLowerCase)) {
			while (iter.hasNext()) {
				entry = iter.next();
				ele.addAttribute(entry.getKey().toLowerCase(),
						TypeConvert.getStringValue(entry.getValue()));
			}
		} else {
			while (iter.hasNext()) {
				entry = iter.next();
				ele.addAttribute(entry.getKey(), TypeConvert.getStringValue(entry.getValue()));
			}
		}

		return ele;
	}

	/**
	 * map to XmlString
	 * 
	 * @param elementName
	 * @param map
	 * @param upperLowerCase
	 * @return Element
	 */
	public static String mapToStringXml(String elementName, Map<String, Object> map,
			EnumConstant.UpperLowerCase upperLowerCase) {

		if (CommonUtil.isEmpty(elementName) || map == null)
			return null;

		Element ele = DocumentHelper.createElement(elementName);

		Set<Entry<String, Object>> entrySet = map.entrySet();
		Entry<String, Object> entry = null;
		Iterator<Entry<String, Object>> iter = entrySet.iterator();
		if (EnumConstant.UpperLowerCase.UPPER.equals(upperLowerCase)) {
			while (iter.hasNext()) {
				entry = iter.next();
				ele.addElement(entry.getKey().toUpperCase())
						.setText(TypeConvert.getStringValue(entry.getValue()));
			}
		} else if (EnumConstant.UpperLowerCase.LOWER.equals(upperLowerCase)) {
			while (iter.hasNext()) {
				entry = iter.next();
				ele.addElement(entry.getKey().toLowerCase())
						.setText(TypeConvert.getStringValue(entry.getValue()));
			}
		} else {
			while (iter.hasNext()) {
				entry = iter.next();
				ele.addElement(entry.getKey())
						.setText(TypeConvert.getStringValue(entry.getValue()));
			}
		}

		return ele.asXML();
	}

	/**
	 * 指定doc的指定节点的指定attribute value encrypt
	 * 
	 * @param doc
	 * @param xpath
	 * @param attributeNames
	 */
	public static void attributeValueEncrypt(Document doc, String xpath, String... attributeNames) {

		List<Element> list = doc.selectNodes(xpath);
		if (list == null || list.isEmpty() || attributeNames == null || attributeNames.length == 0)
			return;
		Attribute attribute = null;
		for (Element e : list) {
			for (String attributeName : attributeNames) {
				attribute = e.attribute(attributeName);
				if (attribute == null) {
					continue;
				} else {
					attribute.setValue(CommonUtil.encryptFormat(attribute.getValue(), 2, 2));
				}
			}
		}
	}

	/**
	 * 将指定元素下面所有的子元素映射为一个实体，指定的节点下的子节点必须都是叶子节点
	 * 
	 * @param elements
	 * @param clazz
	 * @param text
	 * @param defaultValue
	 * @return 指定元素下的所有数据集合
	 */
	public static <T> List<T> toElementList2(List<T> elements, Class<T> clazz, String text,
			String defaultValue) {

		if (elements == null || elements.isEmpty())
			return null;
		List<T> fieldList = new ArrayList<T>();
		T requestField = null;
		Element element = null;
		List<Element> eles = null;
		for (Node field : (List<Node>) elements) {
			try {
				requestField = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				return null;
			}
			element = (Element) field;
			eles = element.elements();

			if (eles != null) {
				for (Element ele : eles) {
					try {
						BeanUtils.setProperty(requestField, ele.getName(), ele.getText());
					} catch (IllegalAccessException | InvocationTargetException e) {
						continue;
					}
				}
			}

			if (!CommonUtil.isEmpty(text)) {

				try {
					String temp = BeanUtils.getProperty(requestField, text);

					if (CommonUtil.isEmpty(temp)) {
						if (CommonUtil.isEmpty(defaultValue))
							BeanUtils.setProperty(requestField, text, element.getTextTrim());
						else
							BeanUtils.setProperty(requestField, text, defaultValue);
					}
				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					e.printStackTrace();
				}

			}

			fieldList.add(requestField);
		}
		return fieldList;
	}

	public static Element toItemList(Element cont, String parentElementName, String elementName,
			List<Map<String, Object>> list) {

		if (!CollectionUtil.isEmptyCollection(list)) {
			Element parent = DocumentHelper.createElement(parentElementName);
			Element row = null;
			for (Map<String, Object> map : list) {
				row = XmlUtil.mapToElement(elementName, map, EnumConstant.UpperLowerCase.LOWER);
				parent.add(row);
			}
			cont.add(parent);
		}

		return cont;
	}

	/**
	 * 判断结点下是否存在指定名字的属性
	 * 
	 * @param parent
	 *            结点
	 * @param attributeName
	 *            属性名字
	 * @return 是否存在
	 */
	public static boolean isExistsAttribute(Element parent, String attributeName) {

		Attribute attr = parent.attribute(attributeName);
		return attr != null;
	}

	/**
	 * 判断结点下是否存在指定名字的子结点
	 * 
	 * @param parent
	 *            结点
	 * @param childName
	 *            子结点名字
	 * @return 是否存在
	 */
	public static boolean isExistsChildElement(Element parent, String childName) {

		Element child = parent.element(childName);
		return child != null;
	}

}
