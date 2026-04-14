package com.pfe.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class AssertUtils {

    public static Comparator<Instant> instantCompareTo = Comparator.nullsFirst(
        (e1, a2) -> e1.compareTo(a2)    );

    public static Comparator<BigDecimal> bigDecimalCompareTo = Comparator.nullsFirst((e1, a2) -> e1.compareTo(a2));
}
