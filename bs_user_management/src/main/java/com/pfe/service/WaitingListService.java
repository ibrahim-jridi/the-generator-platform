package com.pfe.service;

import com.pfe.service.dto.WaitingListDTO;
import com.pfe.service.dto.response.WaitingListResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

public interface WaitingListService {
    WaitingListDTO save(WaitingListDTO waitingListDTO);
    boolean isUserUnsubscribedOrNotExist();
    Integer getUserRank();
    List<WaitingListResponse> getAllWaitingList();

     void unsubscribeFromWaitingList(UUID userId);
    ResponseEntity<WaitingListDTO> getWaitingListByUserId(UUID userId) ;
    WaitingListDTO updateCategory(UUID idUser, WaitingListDTO dto) ;
    WaitingListDTO update(UUID idUser, WaitingListDTO waitingListDTO);

}
