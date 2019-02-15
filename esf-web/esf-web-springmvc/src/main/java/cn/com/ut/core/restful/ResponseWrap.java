package cn.com.ut.core.restful;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.constant.EnumConstant;
import cn.com.ut.core.common.jdbc.PageBean;
import cn.com.ut.core.common.jdbc.PageBuilder;
import cn.com.ut.core.common.util.ArrayUtil;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.MapUtil;

public class ResponseWrap {

	private static final String CODE = "code";
	private static final String MSG = "msg";
	public static final String ROWS = "rows";
	public static final String ROW = "row";
	public static final String DATA = "data";

	private Map<String, Object> responseData = new LinkedHashMap<String, Object>();
	private JSONObject json;
	private JSONArray jsonArray;
	private PageBean page;
	private EnumConstant.UpperLowerCase upperLowerCase = EnumConstant.UpperLowerCase.PLAIN;
	private Set<String> convertFields = new HashSet<String>();

	public void setConvertFields(Set<String> convertFields) {

		this.convertFields = convertFields;
	}

	public Set<String> getConvertFields() {

		return convertFields;
	}

	public EnumConstant.UpperLowerCase getUpperLowerCase() {

		return upperLowerCase;
	}

	public void setUpperLowerCase(EnumConstant.UpperLowerCase upperLowerCase) {

		this.upperLowerCase = upperLowerCase;
	}

	public PageBean getPage() {

		return page;
	}

	public void setPage(PageBean page) {

		this.page = page;
	}

	public ResponseWrap appendPage(PageBean page) {

		setPage(page);
		return this;
	}

	public ResponseWrap appendConvertFields(String[] convertFields) {

		if (!ArrayUtil.isEmptyArray(convertFields)) {
			for (String convertField : convertFields) {
				if (!CommonUtil.isEmpty(convertField))
					getConvertFields().add(convertField);
			}
		}

		return this;
	}

	public ResponseWrap appendConvertFields(String convertField) {

		if (!CommonUtil.isEmpty(convertField))
			getConvertFields().add(convertField);
		return this;
	}

	public void appendPage() {

		Map<String, Object> pageVo = new HashMap<String, Object>();
		if (getPage() != null) {
			pageVo.put(PageBuilder.PAGENO, page.getCurrentPage());
			pageVo.put(PageBuilder.PAGESIZE, page.getPageSize());
			pageVo.put(PageBuilder.RECORDS, page.getTotalCount());
			pageVo.put(PageBuilder.TOTAL, page.getTotalPage());
			pageVo.put(PageBuilder.PAGEGROUP, page.pageGroupToString());
			getResponseData().put(PageBuilder.PAGE, pageVo);
		}
	}

	public ResponseWrap appendRows(List<Map<String, Object>> rows) {

		return appendRows(rows, null);
	}

	public ResponseWrap appendRows(List<Map<String, Object>> rows, DictBuilder dictBuilder) {

		return appendRows(ROWS, rows, dictBuilder);
	}

	private ResponseWrap appendRows(String noteName, List<Map<String, Object>> rows,
			DictBuilder dictBuilder) {

		if (rows != null) {
			Result2JsonPreHandle.preHandle(rows, dictBuilder == null ? null : dictBuilder.getDict(),
					getPage(), getConvertFields());
			getResponseData().put(noteName,
					MapUtil.replaceMapKeyToUpperLowerCase(rows, getUpperLowerCase()));
		}
		return this;
	}

	public ResponseWrap appendRow(Map<String, Object> row) {

		return appendRow(row, null);
	}

	public ResponseWrap appendRow(Map<String, Object> row, DictBuilder dictBuilder) {

		return appendRow(ROW, row, dictBuilder);
	}

	public ResponseWrap appendData(Map<String, Object> row) {

		return appendData(row, null);
	}

	public ResponseWrap appendData(Map<String, Object> row, DictBuilder dictBuilder) {

		return appendRow(DATA, row, dictBuilder);
	}

	public ResponseWrap appendData(List<Map<String, Object>> rows) {

		return appendData(rows, null);
	}

	public ResponseWrap appendData(List<Map<String, Object>> rows, DictBuilder dictBuilder) {

		return appendRows(DATA, rows, dictBuilder);
	}

	private ResponseWrap appendRow(String nodeName, Map<String, Object> row,
			DictBuilder dictBuilder) {

		if (row != null) {
			Result2JsonPreHandle.preHandle(row, dictBuilder == null ? null : dictBuilder.getDict(),
					getConvertFields());
			getResponseData().put(nodeName,
					MapUtil.replaceMapKeyToUpperLowerCase(row, getUpperLowerCase()));
		}
		return this;
	}

	public JSONArray getJsonArray() {

		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {

		this.jsonArray = jsonArray;
	}

	public JSONObject getJson() {

		return json;
	}

	public void setJson(JSONObject json) {

		this.json = json;
	}

	private ResponseWrap() {

		responseData.put(CODE, ConstantUtil.SUCCESS_CODE);
		responseData.put(MSG, "success");
	}

	public static ResponseWrap builder() {

		return new ResponseWrap();
	}

	public boolean hasErr() {

		return getCode() != ConstantUtil.SUCCESS_CODE;
	}

	public Map<String, Object> getResponseData() {

		return responseData;
	}

	public String getMsg() {

		return (String) responseData.get(MSG);
	}

	public int getCode() {

		return (int) responseData.get(CODE);
	}

	public ResponseWrap setMsg(String msg) {

		responseData.put(MSG, msg);
		return this;
	}

	public ResponseWrap setCode(int code) {

		responseData.put(CODE, code);
		return this;
	}

	public ResponseWrap setCodeMsg(int code, String msg) {

		responseData.put(CODE, code);
		responseData.put(MSG, msg);
		return this;
	}
}
