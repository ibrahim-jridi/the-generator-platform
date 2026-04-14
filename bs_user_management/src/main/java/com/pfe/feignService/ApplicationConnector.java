
package com.pfe.feignService;

import com.pfe.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "applicationClient",  url = "${bs-app.endpoint.external-api-url:localhost:8088}", configuration = FeignClientConfig.class)

public interface ApplicationConnector {

    @GetMapping(value = "/api/v1/Application/by-applicant", produces = "application/json")
    ResponseEntity <List<String>> getApplications(@RequestParam("applicantId") String applicantId);
    @GetMapping(value = "/api/v1/Application/by-submitter", produces = "application/json")
    ResponseEntity <List<String>> getApplicationsBySubmitter(@RequestParam("submitterId") String submitterId);
}
