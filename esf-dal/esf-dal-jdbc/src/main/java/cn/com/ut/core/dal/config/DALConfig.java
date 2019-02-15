package cn.com.ut.core.dal.config;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import cn.com.ut.core.common.util.CollectionUtil;
import cn.com.ut.core.common.util.io.FileUtil;
import cn.com.ut.core.common.util.xml.XmlUtil;
import cn.com.ut.core.dal.beans.ORMapperElement;
import cn.com.ut.core.dal.transaction.ChainedTransactionManager;

@Configuration
@ConfigurationProperties(prefix = "dal")
public class DALConfig {

	private static final Logger logger = LoggerFactory.getLogger(DALConfig.class);

	@Autowired
	private DataSourceBeanDefinition dataSourceBeanDefinition;

	@Conditional(TransactionConditional.class)
	@Bean
	@Primary
	public ChainedTransactionManager transactionManager() {

		logger.trace("===ChainedTransactionManager===");
		return new ChainedTransactionManager(dataSourceBeanDefinition.getTxc()
				.toArray(new PlatformTransactionManager[dataSourceBeanDefinition.getTxc().size()]));
	}

	private String ormapper;

	public String getOrmapper() {

		return ormapper;
	}

	public void setOrmapper(String ormapper) {

		this.ormapper = ormapper;
	}

	public Map<String, ORMapperElement> mappers = new TreeMap<String, ORMapperElement>();

	@PreDestroy
	public void destroy() {

		CollectionUtil.clearMap(mappers);
	}

	/**
	 * 解析实体类表关系映射
	 */
	// @PostConstruct
	public void init() {

		List<URL> urls = FileUtil.listUrls(getOrmapper(), "ormapper-*.xml");
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
	private void putORMapperElement(Element root) {

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
	public ORMapperElement getMapper(String key) {

		return mappers.get(key);
	}

}
