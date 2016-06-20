package com.thoughtworks.twist.core.execution;

/**
 * TwistDataStoreFactory is a wrapper for DataStores in Gauge.
 */
public class TwistDataStoreFactory {
	
	public TwistScenarioDataStore getScenarioStore() {
		return new TwistScenarioDataStore();
	}
	
	public TwistSuiteDataStore getSuiteStore() {
		return new TwistSuiteDataStore();
	}
}
