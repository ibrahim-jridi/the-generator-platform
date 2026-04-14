package com.pfe.feignServices;

import com.pfe.service.dto.request.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-management", url = "${endpoint.gateway}"
    + "/user-management/api/v1/open-api")
public interface UserManagementClient {

  @GetMapping("/find-pm-username-by-mf/{fiscalId}")
  ResponseEntity<UserDTO> findUserByFiscalId(@PathVariable String fiscalId);

  @GetMapping("/find-user-by-user-name/{username}")
  ResponseEntity<UserDTO> findUserByUserName(@PathVariable String username);

}
