package com.thoughtworks.twist.gauge.migration;

import com.thoughtworks.gauge.*;
import com.thoughtworks.twist.core.execution.ScenarioInformation;
import com.thoughtworks.twist.core.execution.StepInformation;
import com.thoughtworks.twist.core.execution.report.json.ExecutionStatus;

public class ScenarioInformationHolder {

    public static ScenarioInformation scenarioInfo;
    
    @BeforeSuite
    @BeforeScenario
    @BeforeStep
    @BeforeSpec
    public void updateScenarioInfo(ExecutionContext info) {
    	updateScenarioInformationFromSpecInfo(info);
    }

    private void updateScenarioInformationFromSpecInfo(ExecutionContext info) {
        if (scenarioInfo == null) {
            scenarioInfo = new ScenarioInformation();
        }
        
        Scenario currentScenario = info.getCurrentScenario();
        if (currentScenario != null) {
            scenarioInfo.setScenarioName(currentScenario.getName());
            scenarioInfo.setTags(currentScenario.getTags().toArray(new String[currentScenario.getTags().size()]));
        }
        StepDetails currentStep = info.getCurrentStep();
        if (currentStep != null) {
            ExecutionStatus execStatus = currentStep.getIsFailing()? ExecutionStatus.failed : ExecutionStatus.passed;
            scenarioInfo.setCurrentStep(new StepInformation(currentStep.getText(), execStatus));
        }
    }
}
