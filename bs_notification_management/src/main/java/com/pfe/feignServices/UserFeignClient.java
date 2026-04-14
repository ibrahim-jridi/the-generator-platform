package com.pfe.feignServices;


import com.pfe.config.FeignClientConfig;
import com.pfe.service.dto.GroupDTO;
import com.pfe.service.dto.RoleDTO;
import com.pfe.service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "bsUserManagement", url = "${bs-app.endpoint.gateway-url}/user-management/api/v1", configuration = FeignClientConfig.class)
public interface UserFeignClient {

  @GetMapping("/user/{id}")
  ResponseEntity<UserDTO> getUser(@PathVariable("id") UUID id);

    @GetMapping("/roles/{id}")
    ResponseEntity<RoleDTO> getRole(@PathVariable("id") UUID id);

    @GetMapping("/groups/{id}")
    ResponseEntity<GroupDTO> getGroup(@PathVariable("id") UUID id);

    @GetMapping("/open-api/getRootId")
    UUID getRootId();

    @GetMapping("/user/get-all-group-users-by-group-id/{id}")
    public ResponseEntity<List<UserDTO>> getUserByGroupId(@PathVariable("id") UUID id);

    @GetMapping("/user/get-all-role-users-by-role-id/{id}")
    public ResponseEntity<List<UserDTO>> getUsersByRoleId(@PathVariable("id") UUID id);

    @GetMapping("/user/{id}")
    ResponseEntity<UserDTO> getUserWithAuthorization(@PathVariable("id") UUID id, @RequestHeader("Authorization") String authorization);

}
