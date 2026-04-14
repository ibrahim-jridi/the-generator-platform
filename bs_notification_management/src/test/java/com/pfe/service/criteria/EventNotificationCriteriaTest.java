package com.pfe.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EventNotificationCriteriaTest {

    @Test
    void newEventNotificationCriteriaHasAllFiltersNullTest() {
        var eventNotificationCriteria = new EventNotificationCriteria();
        assertThat(eventNotificationCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void eventNotificationCriteriaFluentMethodsCreatesFiltersTest() {
        var eventNotificationCriteria = new EventNotificationCriteria();

        setAllFilters(eventNotificationCriteria);

        assertThat(eventNotificationCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void eventNotificationCriteriaCopyCreatesNullFilterTest() {
        var eventNotificationCriteria = new EventNotificationCriteria();
        var copy = eventNotificationCriteria.copy();

        assertThat(eventNotificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(eventNotificationCriteria)
        );
    }

    @Test
    void eventNotificationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var eventNotificationCriteria = new EventNotificationCriteria();
        setAllFilters(eventNotificationCriteria);

        var copy = eventNotificationCriteria.copy();

        assertThat(eventNotificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(eventNotificationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var eventNotificationCriteria = new EventNotificationCriteria();

        assertThat(eventNotificationCriteria).hasToString("EventNotificationCriteria{}");
    }

    private static void setAllFilters(EventNotificationCriteria eventNotificationCriteria) {
        eventNotificationCriteria.id();
        eventNotificationCriteria.taskId();
        eventNotificationCriteria.status();
        eventNotificationCriteria.notificationId();
        eventNotificationCriteria.distinct();
    }

    private static Condition<EventNotificationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTaskId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getNotificationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EventNotificationCriteria> copyFiltersAre(
        EventNotificationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTaskId(), copy.getTaskId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getNotificationId(), copy.getNotificationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
