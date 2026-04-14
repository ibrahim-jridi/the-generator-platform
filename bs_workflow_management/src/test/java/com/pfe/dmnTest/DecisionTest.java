package com.pfe.dmnTest;

import org.camunda.bpm.dmn.engine.*;
import org.camunda.bpm.dmn.engine.test.junit5.DmnEngineExtension;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;
import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@ExtendWith(DmnEngineExtension.class)
class DecisionTest {

    @Test
    public void evaluateDecision(DmnEngine dmnEngine) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dmnToTest/eligibilityDecision.dmn");
        DmnDecision decision = dmnEngine.parseDecision("eligibility_decision", inputStream);
        //first rule
        VariableMap variables = Variables.createVariables()
            .putValue("produit", "auto")
            .putValue("age", 19);
        DmnDecisionTableResult results = dmnEngine.evaluateDecisionTable(decision, variables);
        assertThat(results).hasSize(1);
        DmnDecisionRuleResult result = results.getSingleResult();
        assertThat(result)
            .containsOnly(
                entry("eligibility", true)
            );
        //second rule
        variables = Variables.createVariables()
            .putValue("produit", "sante")
            .putValue("age", 15);
        results = dmnEngine.evaluateDecisionTable(decision, variables);
        assertThat(results).hasSize(1);
        result = results.getSingleResult();
        assertThat(result)
            .containsOnly(
                entry("eligibility", true)
            );
        //third rule
        variables = Variables.createVariables()
            .putValue("produit", "habitation")
            .putValue("age", 45);
        results = dmnEngine.evaluateDecisionTable(decision, variables);
        assertThat(results).hasSize(1);
        result = results.getSingleResult();
        assertThat(result)
            .containsOnly(
                entry("eligibility", true)
            );
        //forth rule
        variables = Variables.createVariables()
            .putValue("produit", null)
            .putValue("age", null);
        results = dmnEngine.evaluateDecisionTable(decision, variables);
        assertThat(results).hasSize(1);
        result = results.getSingleResult();
        assertThat(result)
            .containsOnly(
                entry("eligibility", false)
            );

    }

    @Test
    public void evaluateLiteralExpression(DmnEngine dmnEngine) throws ParseException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dmnToTest/ageCalculation.dmn");
        DmnDecision decision = dmnEngine.parseDecision("test_literal_expression", inputStream);
        VariableMap variables = Variables.createVariables()
            .putValue("birthDate", "2000-05-28T12:00:00+01:00");
        DmnDecisionResult results = dmnEngine.evaluateDecision(decision, variables);
        assertThat(results).hasSize(1);
        DmnDecisionResultEntries result = results.getSingleResult();
        assertThat(result)
            .containsOnly(
                entry("age", 24)
            );
    }
}
