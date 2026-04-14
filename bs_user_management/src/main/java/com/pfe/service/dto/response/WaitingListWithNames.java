package com.pfe.service.dto.response;

import com.pfe.domain.WaitingList;

public interface WaitingListWithNames {
  WaitingList getWaitingList();
  String getFirstName();
  String getLastName();
}
