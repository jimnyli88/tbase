package cn.com.ut.auth.sign;

import java.util.Map;

public interface SignService {

	boolean signFromVerify(Map<String, Object> fromMap);

	void signTo(Map<String, Object> toMap);

	String signGen(Map<String, Object> fromMap);

}
