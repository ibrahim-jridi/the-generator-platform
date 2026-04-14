package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import com.pfe.dto.FormDTO;
import com.pfe.dto.SubmissionDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "form-service", url = "${bs-app.endpoint.gateway}"
    + "/form-management/api/v1", configuration = FeignClientConfig.class)

public interface FormService {
    @PostMapping("/submissions/submit")
    void submitFormData(@RequestBody List<SubmissionDTO> submissionDTOList);
    @GetMapping("/forms/{id}")
     ResponseEntity<FormDTO> getForm(@PathVariable("id") UUID id);
    @GetMapping("/submissions/by-task-instance/{taskInstanceId}")
    ResponseEntity<List<SubmissionDTO>> getAllSubmissionsByTaskInstanceId(@PathVariable UUID taskInstanceId);
}
