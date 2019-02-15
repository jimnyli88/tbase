package cn.com.ut.core.common.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.com.ut.core.common.constant.EnumConstant;
import cn.com.ut.core.common.constant.EnumConstant.SqlType;
import cn.com.ut.core.common.constant.EnumConstant.WhereCase;
import cn.com.ut.core.common.util.ArrayUtil;
import cn.com.ut.core.common.util.CollectionUtil;
import cn.com.ut.core.common.util.converter.TypeConvert;
import cn.com.ut.core.dal.sql.SQLHelper;

public class PageBuilder {

	public static final String PAGE = "page";
	public static final String PAGENO = "pageno";
	public static final String PAGESIZE = "pagesize";
	public static final String RECORDS = "records";
	public static final String TOTAL = "total";
	public static final String PAGEGROUP = "pagegroup";
	public static final String SORT = "sort";
	public static final String ROWNUM = "rownum";

	private List<WhereCondition> whereConditions = new ArrayList<WhereCondition>();
	private List<SortCondition> sortConditions = new ArrayList<SortCondition>();

	private static class WhereCondition {

		private String prefix;
		private String name;
		private EnumConstant.WhereCase whereCase;
		private EnumConstant.SqlType sqlType;

		public WhereCondition(String prefix, String name, WhereCase whereCase, SqlType sqlType) {
			this.prefix = prefix;
			this.name = name;
			this.whereCase = whereCase;
			this.sqlType = sqlType;
		}

		public EnumConstant.WhereCase getWhereCase() {

			return whereCase;
		}

		public String getPrefix() {

			return prefix;
		}

		public String getName() {

			return name;
		}

		public EnumConstant.SqlType getSqlType() {

			return sqlType;
		}

		@Override
		public int hashCode() {

			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {

			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			WhereCondition other = (WhereCondition) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

	}

	private static class SortCondition {

		private String prefix;
		private String name;
		private EnumConstant.OrderBy orderBy;

		public SortCondition(String prefix, String name, EnumConstant.OrderBy orderBy) {
			this.prefix = prefix;
			this.name = name;
			this.orderBy = orderBy;
		}

		public String getPrefix() {

			return prefix;
		}

		public String getName() {

			return name;
		}

		public EnumConstant.OrderBy getOrderBy() {

			return orderBy;
		}

		@Override
		public int hashCode() {

			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {

			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SortCondition other = (SortCondition) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

	}

	private PageBuilder() {
	}

	public PageBuilder appendWhereCondition(String prefix, String name,
			EnumConstant.WhereCase whereCase, EnumConstant.SqlType sqlType) {

		WhereCondition whereCondition = new WhereCondition(prefix, name.toLowerCase(), whereCase,
				sqlType);
		if (!whereConditions.contains(whereCondition))
			whereConditions.add(whereCondition);
		return this;
	}

	public PageBuilder appendWhereCondition(String name, EnumConstant.SqlType sqlType) {

		return appendWhereCondition(null, name, null, sqlType);
	}

	public PageBuilder appendWhereCondition(String name) {

		return appendWhereCondition(name, EnumConstant.SqlType.STRING);
	}

	public PageBuilder appendWhereConditions(String... names) {

		if (names != null) {
			for (String name : names)
				appendWhereCondition(name);
		}
		return this;
	}

	public PageBuilder appendSortCondition(String prefix, String name,
			EnumConstant.OrderBy orderBy) {

		SortCondition sortCondition = new SortCondition(prefix, name.toLowerCase(), orderBy);
		if (!sortConditions.contains(sortCondition))
			sortConditions.add(sortCondition);
		return this;
	}

	public PageBuilder appendSortCondition(String name) {

		return appendSortCondition(null, name, null);
	}

	public PageBuilder appendSortConditions(String... names) {

		if (names != null)
			for (String name : names)
				appendSortCondition(name);

		return this;
	}

	public static PageBuilder build() {

		return new PageBuilder();
	}

	private Object getValueObject(MultiValueMap<String, Object> map, String name) {

		Object valueObject = map.getFirst(name);
		if (valueObject == null) {
			valueObject = map.getFirst(name.toUpperCase());
		}
		return valueObject;
	}

	public PageBean buildSQL(MultiValueMap<String, Object> map) {

		List<Object> parameters = new ArrayList<Object>();
		List<String> wheres = new ArrayList<String>();

		for (WhereCondition whereCondition : whereConditions) {

			String name = whereCondition.getName();
			String fullName = name;
			if (whereCondition.getPrefix() != null) {
				fullName = whereCondition.getPrefix() + "." + name;
			}
			Object valueObject = getValueObject(map, name);
			if (valueObject == null)
				continue;
			String opValue = String.valueOf(valueObject);
			int index = opValue.indexOf("_");
			if (index == -1)
				continue;
			String op = opValue.substring(0, index).toUpperCase();
			String value = opValue.substring(index + 1);
			EnumConstant.SqlType sqlType = whereCondition.getSqlType();
			if (sqlType == null)
				sqlType = EnumConstant.SqlType.STRING;
			String type = sqlType.name();

			// 等于
			if (EnumConstant.WhereCase.EQ.name().equals(op)) {
				wheres.add(" " + fullName + " = ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// 不等于
			else if (EnumConstant.WhereCase.NE.name().equals(op)) {
				wheres.add(" " + fullName + " <> ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// 大于
			else if (EnumConstant.WhereCase.GT.name().equals(op)) {
				wheres.add(" " + fullName + " > ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// 小于
			else if (EnumConstant.WhereCase.LT.name().equals(op)) {
				wheres.add(" " + fullName + " < ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// 小于等于
			else if (EnumConstant.WhereCase.LE.name().equals(op)) {
				wheres.add(" " + fullName + " <= ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// 大于等于
			else if (EnumConstant.WhereCase.GE.name().equals(op)) {
				wheres.add(" " + fullName + " >= ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// IN 范围
			else if (EnumConstant.WhereCase.IN.name().equals(op)) {
				JSONArray ja = JSON.parseArray(value);
				wheres.add(" " + fullName + " IN (" + ArrayUtil.joinSameElement("?", ja.size(), ",")
						+ ") ");

				for (int k = 0; k < ja.size(); k++) {
					parameters.add(TypeConvert.convert(ja.getString(k), type));
				}

			}
			// BETWEEN 区间
			else if (EnumConstant.WhereCase.BETWEEN.name().equals(op)) {
				JSONArray ja = JSON.parseArray(value);
				wheres.add(" " + fullName + " BETWEEN ? AND ? ");
				for (int k = 0; k < ja.size(); k++) {
					parameters.add(TypeConvert.convert(ja.getString(k), type));
				}
			}

			// IS NULL
			else if (EnumConstant.WhereCase.NULL.name().equals(op)) {
				wheres.add(" " + fullName + " IS NULL ");
			}
			// IS NOT NULL
			else if (EnumConstant.WhereCase.NOTNULL.name().equals(op)) {
				wheres.add(" " + fullName + " IS NOT NULL ");
			}
			// 模糊匹配，只适用于varchar类型的字段
			else if (EnumConstant.WhereCase.LIKE.name().equals(op)) {
				Object obj = TypeConvert.convert(value, type);
				if (obj instanceof String) {
					wheres.add(SQLHelper.like(fullName));
					obj = ((String) obj).toUpperCase();
				} else
					wheres.add(" " + fullName + " = ? ");
				parameters.add(obj);
			}
			// 左匹配
			else if (EnumConstant.WhereCase.LLIKE.name().equals(op)) {
				Object obj = TypeConvert.convert(value, type);
				if (obj instanceof String)
					wheres.add(SQLHelper.llike(fullName));
				else
					wheres.add(" " + fullName + " = ? ");
				parameters.add(obj);
			}
			// 右匹配
			else if (EnumConstant.WhereCase.RLIKE.name().equals(op)) {
				Object obj = TypeConvert.convert(value, type);
				if (obj instanceof String)
					wheres.add(SQLHelper.rlike(fullName));
				else
					wheres.add(" " + fullName + " = ? ");
				parameters.add(obj);
			}
			// TRUE
			else if (EnumConstant.WhereCase.TRUE.name().equals(op)) {
				wheres.add(" TRUE ");
			}
			// FALSE
			else if (EnumConstant.WhereCase.FALSE.name().equals(op)) {
				wheres.add(" FALSE ");
			}
			// other
			else {
				continue;
			}

		}

		List<String> opSorts = null;
		Map<String, SortCondition> fromSortConditions = new HashMap<String, SortCondition>();
		if (map.containsKey(PageBuilder.SORT)) {
			Object sortValueObject = map.getFirst(PageBuilder.SORT);
			if (sortValueObject != null) {
				opSorts = CollectionUtil.splitStr2List(String.valueOf(sortValueObject), ",");
			}
		}

		if (opSorts != null) {
			for (String opSort : opSorts) {
				int index = opSort.indexOf("_");
				if (index == -1)
					continue;
				String op = opSort.substring(0, index).toUpperCase();
				String field = opSort.substring(index + 1).toLowerCase();
				fromSortConditions.put(field,
						new SortCondition(null, field, EnumConstant.OrderBy.valueOf(op)));
			}
		}

		List<String> orderBys = new ArrayList<String>();
		for (SortCondition sortCondition : sortConditions) {
			String name = sortCondition.getName();
			String fullName = name;
			if (sortCondition.getPrefix() != null) {
				fullName = sortCondition.getPrefix() + "." + name;
			}
			if (fromSortConditions.containsKey(name)) {
				SortCondition fromSortCondition = fromSortConditions.get(name);
				orderBys.add(fullName + " " + fromSortCondition.getOrderBy().name());
			}

		}

		PageBean page = new PageBean();
		if (map.containsKey(PageBuilder.PAGENO)) {
			page.setCurrentPage(Integer.parseInt((String) map.getFirst(PageBuilder.PAGENO)));
		}
		if (map.containsKey(PageBuilder.PAGESIZE)) {
			page.setPageSize(Integer.parseInt((String) map.getFirst(PageBuilder.PAGESIZE)));
		}
		if (!wheres.isEmpty()) {
			page.setSqlWhere(ArrayUtil.joinArrayElement(wheres, "AND"));
			page.setParameters(parameters);
		}
		if (!orderBys.isEmpty()) {
			page.setSqlOrder(ArrayUtil.joinArrayElement(orderBys, ","));
		}
		return page;

	}

	public PageBean buildSQL(Map<String, Object> map) {

		List<Object> parameters = new ArrayList<Object>();
		List<String> wheres = new ArrayList<String>();

		for (WhereCondition whereCondition : whereConditions) {

			String name = whereCondition.getName();
			String fullName = name;
			if (whereCondition.getPrefix() != null) {
				fullName = whereCondition.getPrefix() + "." + name;
			}
			Object valueObject = map.get(name);
			if (valueObject == null)
				continue;
			String opValue = String.valueOf(valueObject);
			EnumConstant.WhereCase opWherecase = whereCondition.getWhereCase();
			if (opWherecase == null) {
				opWherecase = EnumConstant.WhereCase.EQ;
			}
			String op = opWherecase.name();
			String value = opValue;
			EnumConstant.SqlType sqlType = whereCondition.getSqlType();
			if (sqlType == null)
				sqlType = EnumConstant.SqlType.STRING;
			String type = sqlType.name();

			// 等于
			if (EnumConstant.WhereCase.EQ.name().equals(op)) {
				wheres.add(" " + fullName + " = ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// 不等于
			else if (EnumConstant.WhereCase.NE.name().equals(op)) {
				wheres.add(" " + fullName + " <> ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// 大于
			else if (EnumConstant.WhereCase.GT.name().equals(op)) {
				wheres.add(" " + fullName + " > ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// 小于
			else if (EnumConstant.WhereCase.LT.name().equals(op)) {
				wheres.add(" " + fullName + " < ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// 小于等于
			else if (EnumConstant.WhereCase.LE.name().equals(op)) {
				wheres.add(" " + fullName + " <= ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// 大于等于
			else if (EnumConstant.WhereCase.GE.name().equals(op)) {
				wheres.add(" " + fullName + " >= ? ");
				parameters.add(TypeConvert.convert(value, type));
			}
			// IN 范围
			else if (EnumConstant.WhereCase.IN.name().equals(op)) {
				JSONArray ja = JSON.parseArray(value);
				wheres.add(" " + fullName + " IN (" + ArrayUtil.joinSameElement("?", ja.size(), ",")
						+ ") ");

				for (int k = 0; k < ja.size(); k++) {
					parameters.add(TypeConvert.convert(ja.getString(k), type));
				}

			}
			// BETWEEN 区间
			else if (EnumConstant.WhereCase.BETWEEN.name().equals(op)) {
				JSONArray ja = JSON.parseArray(value);
				wheres.add(" " + fullName + " BETWEEN ? AND ? ");
				for (int k = 0; k < ja.size(); k++) {
					parameters.add(TypeConvert.convert(ja.getString(k), type));
				}
			}

			// IS NULL
			else if (EnumConstant.WhereCase.NULL.name().equals(op)) {
				wheres.add(" " + fullName + " IS NULL ");
			}
			// IS NOT NULL
			else if (EnumConstant.WhereCase.NOTNULL.name().equals(op)) {
				wheres.add(" " + fullName + " IS NOT NULL ");
			}
			// 模糊匹配，只适用于varchar类型的字段
			else if (EnumConstant.WhereCase.LIKE.name().equals(op)) {
				Object obj = TypeConvert.convert(value, type);
				if (obj instanceof String) {
					wheres.add(SQLHelper.like(fullName));
					obj = ((String) obj).toUpperCase();
				} else
					wheres.add(" " + fullName + " = ? ");
				parameters.add(obj);
			}
			// 左匹配
			else if (EnumConstant.WhereCase.LLIKE.name().equals(op)) {
				Object obj = TypeConvert.convert(value, type);
				if (obj instanceof String)
					wheres.add(SQLHelper.llike(fullName));
				else
					wheres.add(" " + fullName + " = ? ");
				parameters.add(obj);
			}
			// 右匹配
			else if (EnumConstant.WhereCase.RLIKE.name().equals(op)) {
				Object obj = TypeConvert.convert(value, type);
				if (obj instanceof String)
					wheres.add(SQLHelper.rlike(fullName));
				else
					wheres.add(" " + fullName + " = ? ");
				parameters.add(obj);
			}
			// TRUE
			else if (EnumConstant.WhereCase.TRUE.name().equals(op)) {
				wheres.add(" TRUE ");
			}
			// FALSE
			else if (EnumConstant.WhereCase.FALSE.name().equals(op)) {
				wheres.add(" FALSE ");
			}
			// other
			else {
				continue;
			}

		}
		List<String> orderBys = new ArrayList<String>();
		for (SortCondition sortCondition : sortConditions) {
			String name = sortCondition.getName();
			String fullName = name;
			if (sortCondition.getPrefix() != null) {
				fullName = sortCondition.getPrefix() + "." + name;
			}
			if (sortCondition.getOrderBy() == null
					|| EnumConstant.OrderBy.ASC.name().equals(sortCondition.getOrderBy().name())) {
				orderBys.add(fullName + " " + EnumConstant.OrderBy.ASC.name());
			} else {
				orderBys.add(fullName + " " + EnumConstant.OrderBy.DESC.name());
			}

		}
		PageBean page = new PageBean();
		if (map.containsKey(PageBuilder.PAGENO)) {
			page.setCurrentPage(Integer.parseInt((String) map.get(PageBuilder.PAGENO)));
		}
		if (map.containsKey(PageBuilder.PAGESIZE)) {
			page.setPageSize(Integer.parseInt((String) map.get(PageBuilder.PAGESIZE)));
		}
		if (!wheres.isEmpty()) {
			page.setSqlWhere(ArrayUtil.joinArrayElement(wheres, "AND"));
			page.setParameters(parameters);
		}
		if (!orderBys.isEmpty()) {
			page.setSqlOrder(ArrayUtil.joinArrayElement(orderBys, ","));
		}
		return page;
	}
}
