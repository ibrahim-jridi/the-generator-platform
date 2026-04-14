package com.pfe.web.rest;

import com.pfe.service.WaitingListService;
import com.pfe.service.dto.GroupDTO;
import com.pfe.service.dto.WaitingListDTO;
import com.pfe.web.rest.errors.FieldErrorVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/waiting-list")
public class WaitingListResource {
    private static final Logger log = LoggerFactory.getLogger(UserResource.class);
    private final WaitingListService waitingListService;

    public WaitingListResource(WaitingListService waitingListService) {
        this.waitingListService = waitingListService;
    }

    @PostMapping("")
    @Operation(
        summary = "Create a new Waiting List",
        description = "Create a new Waiting List by providing the waiting list details."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Waiting List created successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad Request. The Waiting List already has an ID with status REGISTRARTION or the request is invalid.",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized. User authentication is required.",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden. The user does not have the required role to create a Waiting List.",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal Server Error. An unexpected error occurred on the server.",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "default",
                description = "An unexpected error occurred.",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FieldErrorVM.class),
                        examples = @ExampleObject(
                            value = "{\n" +
                                "  \"type\": \"/error/type\",\n" +
                                "  \"title\": \"Error Title\",\n" +
                                "  \"status\": 500,\n" +
                                "  \"detail\": \"Detailed description of the error\",\n" +
                                "  \"instance\": \"/error/instance\"\n" +
                                "}"
                        )
                    )
                }
            )
        }
    )

    @PreAuthorize("hasAnyAuthority('BS_WAITLIST_REGISTRATION','BS_ADMIN')")
    public ResponseEntity<WaitingListDTO> createWaitingList(@RequestBody WaitingListDTO waitingListDTO) {
        log.info("REST request to save WaitingList : {}", waitingListDTO);

        if (waitingListDTO.getId() != null) {
            return ResponseEntity.badRequest().body(null);
        }

        WaitingListDTO result = waitingListService.save(waitingListDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/is-unsubscribed")
    @Operation(summary = "Existing Waiting List", description = "Check if unsubscribed waiting list exist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Waiting List retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('BS_WAITLIST_REGISTRATION','BS_ADMIN')")
    public ResponseEntity<Boolean> isUserUnsubscribed() {
        boolean result = waitingListService.isUserUnsubscribedOrNotExist();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/rank")
    @Operation(summary = "Get Waiting List Rank", description = "Get rank of waiting list by user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rank retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('BS_WAITLIST_REGISTRATION','BS_ADMIN')")
    public ResponseEntity<Integer> getUserRank() {
        Integer rank = waitingListService.getUserRank();
        return (rank != null) ? ResponseEntity.ok(rank) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAnyAuthority('BS_WAITLIST_UNSUBSCRIBE','BS_ADMIN')")
    @PutMapping("/unsubscribe/{userId}")
    public ResponseEntity<String> unsubscribe(@PathVariable UUID userId) {
        log.info("Received request to unsubscribe user [{}] from waiting list", userId);
        try {
            waitingListService.unsubscribeFromWaitingList(userId);
            log.info("Successfully unsubscribed user [{}]", userId);
            return ResponseEntity.ok("User unsubscribed and list updated successfully.");
        } catch (Exception e) {
            log.error("Error while unsubscribing user [{}]: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to unsubscribe user: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('BS_WAITLIST_UNSUBSCRIBE','BS_ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<WaitingListDTO> getWaitingListByUserId(@PathVariable UUID userId) {
        log.info("Received request to fetch waiting list for user [{}]", userId);
        try {
            WaitingListDTO result = waitingListService.getWaitingListByUserId(userId).getBody();
            log.info("Successfully retrieved waiting list for user [{}]", userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error fetching waiting list for user [{}]: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    @PreAuthorize("hasAnyAuthority('BS_WAIT_RENEW_CATEGOR_CHANG','BS_ADMIN')")
    @PutMapping("/category/renewal/{userId}")
    public ResponseEntity<WaitingListDTO> renewUserCategory(
        @PathVariable UUID userId,
        @RequestBody WaitingListDTO waitingListDTO
    ) {
        log.debug("Received request to renew category for userId: {}, payload: {}", userId, waitingListDTO);

        try {
            WaitingListDTO updated = waitingListService.updateCategory(userId, waitingListDTO);
            log.info("Category successfully renewed for userId: {}", userId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Failed to renew category for userId: {}. Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/renewal-region/{idUser}")
    @PreAuthorize("hasAnyAuthority('BS_WAIT_RENEW_REGION_CHANGE','BS_ADMIN')")
    public ResponseEntity<WaitingListDTO> renewalWaitingListForGovernorate(@PathVariable UUID idUser, @RequestBody WaitingListDTO waitingListDTO) {
        log.info("REST request to save WaitingList : {}", waitingListDTO);

        if (waitingListDTO.getId() != null) {
            return ResponseEntity.badRequest().body(null);
        }

        WaitingListDTO result = waitingListService.update(idUser, waitingListDTO);
        return ResponseEntity.ok(result);
    }


}
