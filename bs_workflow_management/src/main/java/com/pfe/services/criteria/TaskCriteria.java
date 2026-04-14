package com.pfe.services.criteria;

import java.util.Date;

public record TaskCriteria(String id, Date startTime, String name, Date endTime, String businessKey) {
}
