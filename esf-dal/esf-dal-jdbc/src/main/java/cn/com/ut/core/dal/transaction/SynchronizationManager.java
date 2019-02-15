package cn.com.ut.core.dal.transaction;

public interface SynchronizationManager {

	void initSynchronization();

	boolean isSynchronizationActive();

	void clearSynchronization();
}
