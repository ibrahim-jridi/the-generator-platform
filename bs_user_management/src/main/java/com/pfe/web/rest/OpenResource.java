package com.pfe.web.rest;

import com.pfe.service.UserService;
import com.pfe.service.WaitingListService;
import com.pfe.service.dto.ResetPasswordDTO;
import com.pfe.service.dto.request.ChangePasswordRequestDTO;
import com.pfe.service.dto.response.WaitingListResponse;
import com.pfe.validator.impl.UserValidator;
import com.pfe.service.dto.UserDTO;

import io.jsonwebtoken.JwtException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/v1/open-api")
public class OpenResource {

  private static final Logger log = LoggerFactory.getLogger(OpenResource.class);

  private final UserService userService;
  private final UserValidator userValidator;
  private final WaitingListService waitingListService;

  public OpenResource(UserService userService, UserValidator userValidator,WaitingListService waitingListService) {
    this.userService = userService;
    this.userValidator = userValidator;
    this.waitingListService = waitingListService;
  }

  @GetMapping("/verify-mail")
  public ResponseEntity<?> verifyMail(@RequestParam("token") String token) {
    log.info("==> verifyMail started: {}", token);

    try {
      this.userService.verifyEmail(token);
      return ResponseEntity.ok(true);
    } catch (JwtException | IllegalArgumentException e) {
      log.warn("Token invalid or user already exists: {}", e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      log.error("Unexpected error during email verification", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Internal server error");
    }
  }

  @GetMapping("/getRootId")
  UUID getRootId() {
    return this.userService.getRootId();
  }

  @GetMapping("/find-pm-username-by-mf/{fiscalId}")
  public ResponseEntity<UserDTO> findMoralPersonUserNameByFiscalId(@PathVariable String fiscalId) {
    return ResponseEntity.ok(this.userService.findUserByFiscalId(fiscalId));
  }

  @GetMapping("/find-user-by-user-name/{username}")
  public ResponseEntity<UserDTO> findUserByUserName(@PathVariable String username) {
    return ResponseEntity.ok(this.userService.findUserByUsername(username));
  }


  @PostMapping("/reset-password-request")
  public ResponseEntity<Void> resetPasswordRequest(
      @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
    this.userService.resetPasswordRequest(changePasswordRequestDTO);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reset-user-password")
  public ResponseEntity<Void> resetUserPassword(@RequestBody ResetPasswordDTO resetPasswordDTO,
      @RequestParam(value = "token", required = false) String token) {
    this.userService.resetPassword(resetPasswordDTO, token);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert("User-management", true, "User",
            "Updated"))
        .build();
  }


  @GetMapping("/nationalid-verif")
  public ResponseEntity<Void> checkIfnationalIdExist(
      @RequestParam("nationalId") String nationalId) {
    this.userValidator.checkIfNationalIdExist(nationalId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/signup-verif")
  public ResponseEntity<Void> checkBeforeSignUp(
      @RequestParam("address") String address,
      @RequestParam("birthDate") String birthDate,
      @RequestParam("firstName") String firstName,
      @RequestParam("country") String country,
      @RequestParam("nationality") String nationality,
      @RequestParam("lastName") String lastName,
      @RequestParam("email") String email,
      @RequestParam("phoneNumber") String phoneNumber,
      @RequestParam("nationalId") String nationalId,
      @RequestParam("gender") String gender) {
    this.userService.checkUserInformations(gender, nationalId, phoneNumber,
        email, lastName, nationality, country, firstName,
        birthDate, address);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/get-all-waiting-list")
  public ResponseEntity<List<WaitingListResponse>> getAllWaitingListDisplay() {
    return ResponseEntity.ok(waitingListService.getAllWaitingList());
  }
    @GetMapping("/tax-registration-verif")
    public ResponseEntity<Void> checkIfTaxRegistrationExist(
        @RequestParam("taxRegistration") String taxRegistration) {
        this.userValidator.checkIfTaxRegistrationExist(taxRegistration);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/by-national-id")
    public ResponseEntity<List<UserDTO>> getUserByNationalId(@RequestParam String nationalId) {
        log.debug("Received request to fetch user by national ID: {}", nationalId);

        List<UserDTO> users = userService.getUserByNationalId(nationalId);

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        log.debug("Found {} user(s) with national ID: {}", users.size(), nationalId);

        return ResponseEntity.ok(users);
    }
    @PostMapping("/check-username-prefix")
    public ResponseEntity<Boolean> checkUsernameStartsWithInt(@RequestBody UserDTO request) {
        try {
            boolean result = userService.checkUsernameStartsWithInt(request.getNationalId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PostMapping("/check-username-match")
    public ResponseEntity<Boolean> checkUsernameMatches(@RequestBody UserDTO request) {
        try {
            boolean result = userService.checkUsernameMatches(request.getNationalId(), request.getUsername());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

  @GetMapping("/check-cin")
  public ResponseEntity<Boolean> checkCinExists(@RequestParam String nationalId) {
    log.debug("Check if CIN already exists: {}", nationalId);
    return ResponseEntity.ok(userService.existsByCin(nationalId));
  }

  @GetMapping("/check-email")
  public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
    log.debug("Check if email already exists: {}", email);
    return ResponseEntity.ok(userService.existsByEmail(email));
  }
}
