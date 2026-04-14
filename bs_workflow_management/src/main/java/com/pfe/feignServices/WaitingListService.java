package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import com.pfe.dto.WaitingListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${bs-app.endpoint.gateway}"
    + "/user-management/api/v1", configuration = FeignClientConfig.class)

public interface WaitingListService {

    @PostMapping("/waiting-list")
    ResponseEntity<WaitingListDTO> createWaitingList(@RequestBody WaitingListDTO waitingListDTO);

    @PutMapping("/waiting-list/unsubscribe/{userId}")
     ResponseEntity<String> unsubscribe(@PathVariable UUID userId) ;

    @PutMapping("/waiting-list/renewal-region/{idUser}")
    ResponseEntity<WaitingListDTO> renewalWaitingListForGovernorate(@PathVariable UUID idUser, @RequestBody WaitingListDTO waitingListDTO);

    @PutMapping("waiting-list/category/renewal/{userId}")
    ResponseEntity<WaitingListDTO> renewUserCategory(@PathVariable UUID userId, @RequestBody  WaitingListDTO waitingListDTO);

    }
