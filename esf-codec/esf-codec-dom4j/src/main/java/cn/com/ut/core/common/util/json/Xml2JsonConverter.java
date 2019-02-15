package cn.com.ut.core.common.util.json;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Json工具类
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class Xml2JsonConverter {

	/**
	 * "@attributes"
	 */
	public static final String ATTRIBUTES = "@attributes";
	/**
	 * "@text"
	 */
	public static final String TEXT = "@text";

	/**
	 * 将Element的元素转为JSONObject
	 * 
	 * @param element
	 * @param object
	 */
	public static void attributesToJson(Element element, JSONObject object) {

		if (element.attributeCount() > 0) {
			JSONObject attributes = new JSONObject();
			object.put(ATTRIBUTES, attributes);
			Iterator<Attribute> iter = element.attributeIterator();
			while (iter.hasNext()) {
				Attribute attribute = iter.next();
				attributes.put(attribute.getName(), attribute.getValue());
			}
		}
	}

	/**
	 * xml to Json Array {@link #attributesToJson(Element, JSONObject)}
	 * 
	 * @param element
	 * @param parentObject
	 */
	public static void xmlToJsonArray(Element element, JSONObject parentObject) {

		JSONObject object = new JSONObject();
		attributesToJson(element, object);

		JSONArray tempArray = null;

		if (parentObject.containsKey(element.getName())) {
			Object array = parentObject.getJSONObject(element.getName());
			if (array instanceof JSONArray) {
				tempArray = ((JSONArray) array);
			} else {
				Object tempObject = parentObject.remove(element.getName());
				tempArray = new JSONArray();
				tempArray.add(tempObject);
				parentObject.put(element.getName(), tempArray);
			}
		}

		if (!element.hasContent())
			element.setText("");

		if (element.isTextOnly()) {
			if (element.attributeCount() > 0) {
				object.put(TEXT, element.getText());

				if (tempArray == null) {
					parentObject.put(element.getName(), object);
				} else {
					tempArray.add(object);
				}
			} else {

				if (tempArray == null) {
					parentObject.put(element.getName(), element.getText());
				} else {
					tempArray.add(element.getText());
				}

			}
		} else {
			if (tempArray == null) {
				parentObject.put(element.getName(), object);
			} else {
				tempArray.add(object);
			}
			Iterator<Element> eIter = element.elementIterator();
			while (eIter.hasNext()) {
				xmlToJsonArray(eIter.next(), object);
			}
		}
	}

	/**
	 * xml to Json
	 * 
	 * @param element
	 * @param parentObject
	 * @param object
	 */
	public static void xmlToJson(Element element, JSONObject parentObject, JSONObject object) {

		if (parentObject != null) {
			parentObject.put(element.getName(), object);
		}

		if (element.attributeCount() > 0) {
			JSONObject attributes = new JSONObject();
			object.put(ATTRIBUTES, attributes);
			Iterator<Attribute> iter = element.attributeIterator();
			while (iter.hasNext()) {
				Attribute attribute = iter.next();
				attributes.put(attribute.getName(), attribute.getValue());
			}
		}

		if (element.hasContent()) {
			if (element.isTextOnly()) {
				if (element.attributeCount() > 0 || parentObject == null)
					object.put(TEXT, element.getText());
				else
					parentObject.put(element.getName(), element.getText());
			} else {
				Iterator<Element> eIter = element.elementIterator();
				while (eIter.hasNext()) {
					xmlToJson(eIter.next(), object, new JSONObject());
				}
			}
		}
	}

	/**
	 * json to xml
	 * 
	 * @param object
	 * @param parentElement
	 */
	public static void jsonToXml(JSONObject object, Element parentElement) {

		Set<Entry<String, Object>> set = object.entrySet();
		Iterator<Entry<String, Object>> iter = set.iterator();
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			String key = entry.getKey();
			Object value = entry.getValue();

			if (value instanceof JSONArray) {
				Iterator<Object> arrIter = ((JSONArray) value).iterator();
				while (arrIter.hasNext()) {
					Object obj = arrIter.next();
					Element currentElement = parentElement.addElement(key);
					jsonToXml((JSONObject) obj, currentElement);
				}
			} else if (value instanceof JSONObject) {
				JSONObject aObject = (JSONObject) value;
				if (key.equals(ATTRIBUTES)) {
					Set<Entry<String, Object>> bSet = aObject.entrySet();
					Iterator<Entry<String, Object>> bIter = bSet.iterator();
					while (bIter.hasNext()) {
						Entry<String, Object> bEntry = bIter.next();
						parentElement.addAttribute(bEntry.getKey(),
								String.valueOf(bEntry.getValue()));
					}
				} else {
					Element currentElement = parentElement.addElement(key);
					jsonToXml(aObject, currentElement);
				}

			} else {
				if (key.equals(TEXT)) {
					parentElement.setText(String.valueOf(value));
				} else {
					parentElement.addElement(key).addText(String.valueOf(value));
				}
			}
		}
	}

}
