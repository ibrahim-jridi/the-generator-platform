package com.pfe.rest;

import com.pfe.service.OpenIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/open-api")
public class OpenIdRessource {
    private static final Logger log = LoggerFactory.getLogger(OpenIdRessource.class);
    private final OpenIdService openIdService ;
    public OpenIdRessource(OpenIdService openIdService) {
        this.openIdService = openIdService;
    }

    @GetMapping("/get-ehoweya-user")
    public ResponseEntity<?> getTokenAndUserInfo(@RequestParam String code ,@RequestParam String codeVerifier) throws Exception {
        log.debug("start to extchage code and fetc user data");
        Map<String, Object> userInfo = openIdService.exchangeCodeAndFetchUser(code,codeVerifier);
        log.debug("recieved user info from e-howeya is :{}", userInfo);
        if (userInfo.containsKey("error")) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(userInfo);
        }
        return ResponseEntity.ok(userInfo);
    }
    @GetMapping("/ehouweya-config")
    public ResponseEntity<?> getEhoweyaConfig(
    ) {
        Map<String, String> ehoweyaConfig = this.openIdService.getEhouweyaConfig();
        log.debug("e-howeya config :{}", ehoweyaConfig);
        return ResponseEntity.ok().body(ehoweyaConfig);
    }
}
