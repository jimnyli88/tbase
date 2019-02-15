package cn.com.ut.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;

public class DatasourceSetter {

	/**
	 * 公共属性设置，类似基于xml配置中的父子继承效果
	 * 
	 * @param dataSource
	 */
	protected void dataSourceSetter(DruidDataSource dataSource) {

		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUsername("sa");
		dataSource.setPassword("123");
	}
}
