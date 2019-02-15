package cn.com.ut.core.common.jdbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.ut.core.common.util.CollectionUtil;

/**
 * 分页实体
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class PageBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2508592216124137984L;

	/**
	 * ASC
	 */
	public static final int SORT_ASC = 1;
	/**
	 * DESC
	 */
	public static final int SORT_DESC = -SORT_ASC;

	/**
	 * 默认每页10条记录
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;
	/**
	 * 可见的页组
	 */
	public static final int VISIBLE_PAGE_GROUP_SIZE = 8;
	/**
	 * 单页显示的记录数
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;
	/**
	 * 第一条数据的偏移位置,从0开始
	 */
	private int startIndex = 0;
	/**
	 * 当前页
	 */
	private int currentPage = 1;
	/**
	 * 总记录数
	 */
	private int totalCount;
	/**
	 * 总页数
	 */
	private int totalPage = 0;
	/**
	 * 结果集
	 */
	private List<?> voList;
	/**
	 * 页面结果集
	 */
	private List<Map<String, Object>> itemList;
	/**
	 * 可见的页组
	 */
	private int[] visiblePageGroup;

	/**
	 * 动态where，来自组合查询请求
	 */
	private String sqlWhere = "";
	/**
	 * 动态order by
	 */
	private String sqlOrder = "";
	/**
	 * 用来存放条件的参数
	 */
	private List<Object> parameters = new ArrayList<Object>();
	/**
	 * 是否按照分页显示
	 */
	private boolean isPageShow = true;

	/**
	 * 字段前缀组装
	 */
	private FieldPrefixBuilder fieldPrefixBuilder;

	/**
	 *
	 * @return fieldPrefixBuilder
	 */
	public FieldPrefixBuilder getFieldPrefixBuilder() {

		return fieldPrefixBuilder;
	}

	public void setFieldPrefixBuilder(FieldPrefixBuilder fieldPrefixBuilder) {

		this.fieldPrefixBuilder = fieldPrefixBuilder;
	}

	/**
	 * @return 返回mongodb的每页记录大小
	 */
	public Integer getCursorLimit() {

		return getPageSize();
	}

	/**
	 * @return 返回startIndex
	 */
	public Integer getCursorSkip() {

		return startIndex;
	}

	public boolean isPageShow() {

		return isPageShow;
	}

	public void setPageShow(boolean isPageShow) {

		this.isPageShow = isPageShow;
	}

	public void setSqlWhere(String sqlWhere) {

		this.sqlWhere = sqlWhere;
	}

	public void setSqlOrder(String sqlOrder) {

		this.sqlOrder = sqlOrder;
	}

	/**
	 * 
	 * @return 返回关系数据库组装好的where条件
	 */
	public String getSqlWhere() {

		return sqlWhere;
	}

	/**
	 * 
	 * @return 返回关系数据库组装好的排序条件
	 */
	public String getSqlOrder() {

		return sqlOrder;
	}

	/**
	 * 获取参数集合
	 * 
	 * @return parameters
	 */
	public List<Object> getParameters() {

		return parameters;
	}

	public void setParameters(List<Object> parameters) {

		this.parameters = parameters;
	}

	/**
	 * 获取当前页的结果集
	 * 
	 * @return itemList
	 */
	public List<Map<String, Object>> getItemList() {

		return itemList;
	}

	public void setItemList(List<Map<String, Object>> itemList) {

		this.itemList = itemList;
	}

	public PageBean() {

	}

	/**
	 * 如果当前页码等于1，则返回当前页码；否则返回当前页码减去一的页码
	 * 
	 * @return 返回上一页的页码
	 */
	public int getLastPage() {

		return currentPage > 1 ? currentPage - 1 : currentPage;

	}

	/**
	 * 如果当前页码下雨总页数，则返回当前页码加上一的页码；否则返回当前页码
	 * 
	 * @return 返回下一页的页码
	 */
	public int getNextPage() {

		return currentPage < totalPage ? currentPage + 1 : currentPage;
	}

	/**
	 * 获取页面记录大小
	 * 
	 * @return pageSize
	 */
	public int getPageSize() {

		return pageSize;
	}

	/**
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {

		this.pageSize = pageSize;
		this.setCurrentPage(this.getCurrentPage());
	}

	/**
	 * 
	 * @return 返回起始位置
	 */
	public int getStartIndex() {

		return startIndex;
	}

	/**
	 * 
	 * @return 返回当前页码
	 */
	public int getCurrentPage() {

		return currentPage;
	}

	/**
	 * 
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {

		// 页号必须从1开始才有效
		if (currentPage > 0) {
			this.currentPage = currentPage;
			startIndex = (this.currentPage - 1) * pageSize;
		}

	}

	/**
	 * 
	 * @return 返回总记录数
	 */
	public int getTotalCount() {

		return totalCount;
	}

	/**
	 * 设置总记录数，计算总页数，计算页组
	 * 
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount) {

		this.totalCount = totalCount;
		// 获得总记录数后计算总页数，并保证当前页号不超过总页数，另计算可见的页组
		totalPage = (this.totalCount + pageSize - 1) / pageSize;

		if (this.currentPage > totalPage) {
			setCurrentPage(totalPage);
		}

		// 计算页组
		this.visiblePageGroup = calculatePageGroup();
	}

	/**
	 * 
	 * @return 返回总页数
	 */
	public long getTotalPage() {

		return totalPage;
	}

	public List<?> getVoList() {

		return voList;
	}

	public void setVoList(List<?> voList) {

		this.voList = voList;
	}

	/**
	 * 如当前可见页组：[3,4,6,7,8,9,10,11,12,13]
	 * 
	 * @return 返回可见的页面分组
	 */
	public int[] getVisiblePageGroup() {

		return visiblePageGroup;
	}

	/**
	 * 如当前可见页组："3,4,6,7,8,9,10,11,12,13"
	 * 
	 * @return 返回可见的页面组字符串或者空字符串
	 */
	public String pageGroupToString() {

		if (visiblePageGroup == null || visiblePageGroup.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < visiblePageGroup.length; i++) {
			if (i == 0)
				sb.append(visiblePageGroup[i]);
			else
				sb.append(",").append(visiblePageGroup[i]);
		}

		return sb.toString();
	}

	/**
	 * 根据当前页码计算当前可见页码组
	 * 
	 * @return 返回页组
	 */
	private int[] calculatePageGroup() {

		int start = this.currentPage - 4;

		if (start > this.totalPage)
			start = this.totalPage;

		if (start < 1)
			start = 1;

		int end = this.currentPage + 3;

		int temp = end - start + 1;
		if (temp < VISIBLE_PAGE_GROUP_SIZE)
			end += (VISIBLE_PAGE_GROUP_SIZE - temp);

		if (end > this.totalPage)
			end = this.totalPage;

		int size = end - start + 1;
		int[] pageGroup = new int[size];
		for (int i = 0; i < size; i++)
			pageGroup[i] = start + i;

		return pageGroup;
	}

	/**
	 * 给指定的字段添加前缀
	 * 
	 * @param fullName
	 * @return 返回添加了前缀的字符串
	 */
	private String appendFieldPrefix(String fullName) {

		if (getFieldPrefixBuilder() == null
				|| CollectionUtil.isEmptyMap(getFieldPrefixBuilder().getFieldPrefix()))
			return fullName;
		else {
			String shortName = fullName;
			int index = fullName.indexOf(".");
			if (index != -1)
				shortName = fullName.substring(index + 1);
			String prefix = getFieldPrefixBuilder().getFieldPrefix().get(shortName);
			if (prefix == null)
				return fullName;
			else {
				if (index == -1)
					return prefix + "." + fullName;
				else
					return prefix + "." + shortName;
			}
		}

	}
}
