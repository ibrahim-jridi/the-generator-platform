package com.pfe.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ConfigurationReportCriteriaTest {

    @Test
    void newConfigurationReportCriteriaHasAllFiltersNullTest() {
        var configurationReportCriteria = new ConfigurationReportCriteria();
        assertThat(configurationReportCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void configurationReportCriteriaFluentMethodsCreatesFiltersTest() {
        var configurationReportCriteria = new ConfigurationReportCriteria();

        setAllFilters(configurationReportCriteria);

        assertThat(configurationReportCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void configurationReportCriteriaCopyCreatesNullFilterTest() {
        var configurationReportCriteria = new ConfigurationReportCriteria();
        var copy = configurationReportCriteria.copy();

        assertThat(configurationReportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(configurationReportCriteria)
        );
    }

    @Test
    void configurationReportCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var configurationReportCriteria = new ConfigurationReportCriteria();
        setAllFilters(configurationReportCriteria);

        var copy = configurationReportCriteria.copy();

        assertThat(configurationReportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(configurationReportCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var configurationReportCriteria = new ConfigurationReportCriteria();

        assertThat(configurationReportCriteria).hasToString("ConfigurationReportCriteria{}");
    }

    private static void setAllFilters(ConfigurationReportCriteria configurationReportCriteria) {
        configurationReportCriteria.id();
        configurationReportCriteria.name();
        configurationReportCriteria.address();
        configurationReportCriteria.postalCode();
        configurationReportCriteria.phone();
        configurationReportCriteria.fax();
        configurationReportCriteria.email();
        configurationReportCriteria.logo();
        configurationReportCriteria.footer();
        configurationReportCriteria.distinct();
    }

    private static Condition<ConfigurationReportCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getPostalCode()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getFax()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getLogo()) &&
                condition.apply(criteria.getFooter()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ConfigurationReportCriteria> copyFiltersAre(
        ConfigurationReportCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getPostalCode(), copy.getPostalCode()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getFax(), copy.getFax()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getLogo(), copy.getLogo()) &&
                condition.apply(criteria.getFooter(), copy.getFooter()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
