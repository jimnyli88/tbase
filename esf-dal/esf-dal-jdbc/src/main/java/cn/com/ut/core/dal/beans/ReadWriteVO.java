package cn.com.ut.core.dal.beans;

public class ReadWriteVO extends BaseElement {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	protected boolean writeable = false;

	public boolean isWriteable() {

		return writeable;
	}

	public void setWriteable(boolean writeable) {

		this.writeable = writeable;
	}

}
