package cn.com.ut.core.dal.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceRoute {

	private boolean xa = false;
	private boolean master = false;
	private int depth = 1;
	private String serviceSignature;

}
