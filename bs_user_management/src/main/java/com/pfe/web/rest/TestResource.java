package com.pfe.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Tests. to be deleted
 */
@RestController
@RequestMapping("/api/test")
public class TestResource {

    private final Logger log = LoggerFactory.getLogger(TestResource.class);

    @GetMapping("/test")
    public ResponseEntity<String> getTest() {
        this.log.debug("REST test ");
        String value = "test ok without authorized";

        return ResponseEntity.ok().body(value);
    }

    @GetMapping("/testadmin")
    @PreAuthorize("hasAuthority('BS_ADMIN')")
    public ResponseEntity<String> getTestAdmin() {
        this.log.debug("REST test Admin ");
        String value = "test ok with role admin";
        return ResponseEntity.ok().body(value);
    }

    @GetMapping("/testadmin2")
    @PreAuthorize("hasAuthority('BS_ADMIN2')")
    public ResponseEntity<String> getTestAdmin2() {
        this.log.debug("REST test Admin ");
        String value = "test ok with role admin";
        return ResponseEntity.ok().body(value);
    }
}
