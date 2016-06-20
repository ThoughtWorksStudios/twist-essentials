package com.thoughtworks.twist.core.execution;

public class TwistDataStoreFactory {
	
	public TwistScenarioDataStore getScenarioStore() {
		return new TwistScenarioDataStore();
	}
	
	public TwistSuiteDataStore getSuiteStore() {
		return new TwistSuiteDataStore();
	}
}
