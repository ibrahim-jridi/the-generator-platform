package com.pfe.services.criteria;

import java.time.Instant;
import java.util.Date;

public record ProcessInstanceCriteria(String id, String state, String processDefinitionId, Date startTime) { }
