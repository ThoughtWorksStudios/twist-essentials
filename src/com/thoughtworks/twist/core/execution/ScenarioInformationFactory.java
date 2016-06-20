package com.thoughtworks.twist.core.execution;

import com.thoughtworks.twist.gauge.migration.ScenarioInformationHolder;

public class ScenarioInformationFactory {

    public static IScenarioInformation getScenarioInformation() {
        return ScenarioInformationHolder.scenarioInfo;
    }
}
