package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import java.util.List;
import java.util.UUID;

import com.pfe.dto.RoleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${bs-app.endpoint.gateway}"
    + "/user-management/api/v1/roles", configuration = FeignClientConfig.class)

public interface RoleService {
    @GetMapping("/getUserIdsByRoleId")
    ResponseEntity<List<UUID>> getUserIdsByRoleId(@RequestParam UUID id);
    @GetMapping("/{id}")
    ResponseEntity<RoleDTO> getRole(@PathVariable("id") UUID id);

    @GetMapping("/get-role-by-label/{label}")
     ResponseEntity<RoleDTO> getRoleByLabel(@PathVariable String label);

}
