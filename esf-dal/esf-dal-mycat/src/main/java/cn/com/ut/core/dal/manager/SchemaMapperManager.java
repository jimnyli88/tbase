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

	/**
	 * ormapper 配置文件路径
	 */
	public static final String SCHEMA_MAPPER_PATH = "cn/com/ut/config/ormapper/ormapper.xml";

	public static Set<ResourceElement> resources = new LinkedHashSet<ResourceElement>();
	public static Map<String, ORMapperElement> mappers = new TreeMap<String, ORMapperElement>();

	static {
		init();
		// System.out.println(mappers);
	}

	public static void destroy() {

		CollectionUtil.clearCollection(resources);
		CollectionUtil.clearMap(mappers);
	}

	/**
	 * 解析实体类表关系映射
	 */
	public static void init() {

		Document doc = null;
		Element root = null;
		try {
			doc = XmlUtil.readXml(SCHEMA_MAPPER_PATH);
			root = doc.getRootElement();
		} catch (DocumentException e) {
			return;
		}

		putORMapperElement(root);

		List<ResourceElement> resourceElements = XmlUtil
				.toElementList(root.selectNodes("resources/resource"), ResourceElement.class);

		if (resourceElements != null && !resourceElements.isEmpty()) {
			resources.addAll(resourceElements);
		}

		if (resources != null && !resources.isEmpty()) {
			for (ResourceElement r : resources) {
				List<URL> urls = FileUtil.listUrls(r.getName(), null);
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
