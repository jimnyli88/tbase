package cn.com.ut.core.restful;

import java.util.HashMap;
import java.util.Map;

public class DictBuilder {

	private Map<String, String> dict = new HashMap<String, String>();

	private DictBuilder() {

	}

	public DictBuilder appendDict(String field, String dictType) {

		getDict().put(field, dictType);
		return this;
	}

	public static DictBuilder build() {

		return new DictBuilder();
	}

	public Map<String, String> getDict() {

		return dict;
	}
}
