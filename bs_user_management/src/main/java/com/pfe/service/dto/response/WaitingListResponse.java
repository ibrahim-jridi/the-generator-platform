package com.pfe.service.dto.response;

import com.pfe.service.dto.WaitingListDTO;
import java.io.Serializable;

public class WaitingListResponse implements Serializable {

  private static final long serialVersionUID = 4893052290523446782L;

  private WaitingListDTO waitingList;
  private String firstName;
  private String lastName;

  public WaitingListResponse() {}

  public WaitingListResponse(WaitingListDTO waitingList, String firstName, String lastName) {
    this.waitingList = waitingList;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public WaitingListDTO getWaitingList() {
    return waitingList;
  }

  public void setWaitingList(WaitingListDTO waitingList) {
    this.waitingList = waitingList;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
