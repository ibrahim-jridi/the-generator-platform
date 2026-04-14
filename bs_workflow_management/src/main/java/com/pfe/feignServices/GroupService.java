package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import java.util.List;
import java.util.UUID;

import com.pfe.dto.GroupDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${bs-app.endpoint.gateway}"
    + "/user-management/api/v1/groups", configuration = FeignClientConfig.class)

public interface GroupService {
    @GetMapping("/getUserIdsByGroupId")
    ResponseEntity<List<UUID>> getUserIdsByGroupId(@RequestParam UUID id);
    @GetMapping("/{id}")
    ResponseEntity<GroupDTO> getGroup(@PathVariable("id") UUID id);

}
