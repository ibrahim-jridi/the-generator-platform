package com.pfe.rest;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
    Optional<String> value = Optional.of("test ok without authorized");
    return ResponseEntity.ok().body(value.get());
  }

  @GetMapping("/testadmin")
  @PreAuthorize("hasAuthority('BS_ROLE_test')")
  public Mono<ResponseEntity<String>> getTestAdmin() {
    this.log.debug("REST test Admin ");
    Optional<String> value = Optional.of("test ok with role admin");
    return Mono.just(ResponseEntity.ok().body(value.get()));
  }

  @GetMapping("/testadmin2")
  @PreAuthorize("hasAuthority('BS_ROLE_test2')")
  public Mono<ResponseEntity<String>> getTestAdmin2() {
    this.log.debug("REST test Admin ");
    Optional<String> value = Optional.of("test ok with role admin");
    return Mono.just(ResponseEntity.ok().body(value.get()));
  }
}
