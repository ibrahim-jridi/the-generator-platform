package com.pfe.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NotificationPushedCriteriaTest {

    @Test
    void newNotificationPushedCriteriaHasAllFiltersNullTest() {
        var notificationPushedCriteria = new NotificationPushedCriteria();
        assertThat(notificationPushedCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void notificationPushedCriteriaFluentMethodsCreatesFiltersTest() {
        var notificationPushedCriteria = new NotificationPushedCriteria();

        setAllFilters(notificationPushedCriteria);

        assertThat(notificationPushedCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void notificationPushedCriteriaCopyCreatesNullFilterTest() {
        var notificationPushedCriteria = new NotificationPushedCriteria();
        var copy = notificationPushedCriteria.copy();

        assertThat(notificationPushedCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(notificationPushedCriteria)
        );
    }

    @Test
    void notificationPushedCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var notificationPushedCriteria = new NotificationPushedCriteria();
        setAllFilters(notificationPushedCriteria);

        var copy = notificationPushedCriteria.copy();

        assertThat(notificationPushedCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(notificationPushedCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var notificationPushedCriteria = new NotificationPushedCriteria();

        assertThat(notificationPushedCriteria).hasToString("NotificationPushedCriteria{}");
    }

    private static void setAllFilters(NotificationPushedCriteria notificationPushedCriteria) {
        notificationPushedCriteria.id();
        notificationPushedCriteria.name();
        notificationPushedCriteria.description();
        notificationPushedCriteria.message();
        notificationPushedCriteria.notificationDate();
        notificationPushedCriteria.recipientId();
        notificationPushedCriteria.isSeen();
        notificationPushedCriteria.distinct();
    }

    private static Condition<NotificationPushedCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getMessage()) &&
                condition.apply(criteria.getNotificationDate()) &&
                condition.apply(criteria.getRecipientId()) &&
                condition.apply(criteria.getIsSeen()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NotificationPushedCriteria> copyFiltersAre(
        NotificationPushedCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getMessage(), copy.getMessage()) &&
                condition.apply(criteria.getNotificationDate(), copy.getNotificationDate()) &&
                condition.apply(criteria.getRecipientId(), copy.getRecipientId()) &&
                condition.apply(criteria.getIsSeen(), copy.getIsSeen()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
