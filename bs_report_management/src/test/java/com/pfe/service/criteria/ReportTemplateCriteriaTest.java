package com.pfe.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReportTemplateCriteriaTest {

    @Test
    void newReportTemplateCriteriaHasAllFiltersNullTest() {
        var reportTemplateCriteria = new ReportTemplateCriteria();
        assertThat(reportTemplateCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reportTemplateCriteriaFluentMethodsCreatesFiltersTest() {
        var reportTemplateCriteria = new ReportTemplateCriteria();

        setAllFilters(reportTemplateCriteria);

        assertThat(reportTemplateCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reportTemplateCriteriaCopyCreatesNullFilterTest() {
        var reportTemplateCriteria = new ReportTemplateCriteria();
        var copy = reportTemplateCriteria.copy();

        assertThat(reportTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reportTemplateCriteria)
        );
    }

    @Test
    void reportTemplateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reportTemplateCriteria = new ReportTemplateCriteria();
        setAllFilters(reportTemplateCriteria);

        var copy = reportTemplateCriteria.copy();

        assertThat(reportTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reportTemplateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reportTemplateCriteria = new ReportTemplateCriteria();

        assertThat(reportTemplateCriteria).hasToString("ReportTemplateCriteria{}");
    }

    private static void setAllFilters(ReportTemplateCriteria reportTemplateCriteria) {
        reportTemplateCriteria.id();
        reportTemplateCriteria.type();
        reportTemplateCriteria.path();
        reportTemplateCriteria.distinct();
    }

    private static Condition<ReportTemplateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getPath()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReportTemplateCriteria> copyFiltersAre(
        ReportTemplateCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getPath(), copy.getPath()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
