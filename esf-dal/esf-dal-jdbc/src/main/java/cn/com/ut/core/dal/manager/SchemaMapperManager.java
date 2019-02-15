package cn.com.ut.core.dal.manager;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import cn.com.ut.core.common.util.CollectionUtil;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.PropertiesUtil;
import cn.com.ut.core.common.util.io.FileUtil;
import cn.com.ut.core.common.util.xml.XmlUtil;
import cn.com.ut.core.dal.beans.ORMapperElement;
import cn.com.ut.core.dal.beans.ResourceElement;

/**
 * 实体类表关系映射管理
 * 
 * @author wuxiaohua
 * @since 2015-12-30
 */
public class SchemaMapperManager {

	public static Map<String, ORMapperElement> mappers = new TreeMap<String, ORMapperElement>();

	static {
		init();
		// System.out.println(mappers);
	}

	public static void destroy() {

		CollectionUtil.clearMap(mappers);
	}

	/**
	 * 解析实体类表关系映射
	 */
	public static void init() {

		String ormapper = PropertiesUtil.getProperty("dal.ormapper");
		List<URL> urls = null;
		if (!CommonUtil.isEmpty(ormapper)) {
			urls = FileUtil.listUrls(ormapper, "ormapper-*.xml");
		} else {
			urls = FileUtil.listUrls("**/ormapper-*.xml", null);
		}
		if (urls != null) {
			for (URL url : urls) {
				Document resDoc = null;
				try {
					resDoc = XmlUtil.readXml(url);
					// System.out.println("resDoc==\n"+resDoc.asXML());
				} catch (DocumentException e) {
				}
				if (resDoc != null) {
					putORMapperElement(resDoc.getRootElement());
				}
			}
		}

	}

	/**
	 * 逐条解析实体类表关系映射
	 */
	private static void putORMapperElement(Element root) {

		List<Element> mapperElements = root.selectNodes("schemas/schema/ormapper");
		if (mapperElements != null && !mapperElements.isEmpty()) {
			for (Element mapper : mapperElements) {
				ORMapperElement orMapperElement = new ORMapperElement();
				orMapperElement.setName(mapper.attribute("name").getValue());
				orMapperElement.setTable(mapper.attribute("table").getValue());
				orMapperElement.setSchema(mapper.getParent().attribute("name").getValue());
				mappers.put(orMapperElement.getName(), orMapperElement);
			}
		}
	}

	/**
	 * 获取一条实体表关系映射
	 * 
	 * @param key
	 *            实体类名称
	 * @return
	 */
	public static ORMapperElement getMapper(String key) {

		return mappers.get(key);
	}
}
