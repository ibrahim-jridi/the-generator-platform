package com.pfe.web.rest;

import com.pfe.dto.SizeConfigDto;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.services.SizeConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/size-configs")
public class SizeConfigResource {

    private final SizeConfigService sizeConfigService;

    public SizeConfigResource(SizeConfigService sizeConfigService) {
        this.sizeConfigService = sizeConfigService;
    }

    @PostMapping("")
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<SizeConfigDto> createSizeConfig(@RequestBody SizeConfigDto sizeConfigDto) {
        SizeConfigDto result = sizeConfigService.createSizeConfig(sizeConfigDto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<SizeConfigDto> updateSizeConfig(
        @PathVariable UUID id,
        @RequestBody SizeConfigDto sizeConfigDto) {
        SizeConfigDto result = sizeConfigService.updateSizeConfig(id, sizeConfigDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Void> deleteSizeConfig(@PathVariable UUID id) {
        sizeConfigService.deleteSizeConfig(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<SizeConfigDto> getSizeConfigById(@PathVariable UUID id) {
        SizeConfigDto sizeConfigDto = sizeConfigService.getSizeConfigById(id);
        return ResponseEntity.ok(sizeConfigDto);
    }

    @GetMapping("")
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<SizeConfigDto>> getAllSizeConfigs() {
        List<SizeConfigDto> sizeConfigDtos = sizeConfigService.getAllSizeConfigs();
        return ResponseEntity.ok(sizeConfigDtos);
    }
}
