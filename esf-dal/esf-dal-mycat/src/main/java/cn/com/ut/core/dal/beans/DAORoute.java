package cn.com.ut.core.dal.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DAORoute {

	private String dataNode;
	private int serviceSignature;
	private int daoIndex = 1;

}
