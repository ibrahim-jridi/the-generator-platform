package com.pfe.service;

import com.pfe.service.dto.request.ApplicantListUserResponse;
import com.pfe.service.dto.request.ApplicationUsersResponse;
import com.pfe.domain.DesignationsList;
import com.pfe.service.dto.DesignationsListDTO;
import com.pfe.service.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DesignationsListService {

  DesignationsListDTO save(DesignationsListDTO dto);

  List<DesignationsList> getListByPmUserId(UUID pmUserId, Pageable pageable);

  List<DesignationsList> getListByUserId(UUID userId);

  ResponseEntity<ApplicationUsersResponse> getApplicationNames();

   DesignationsListDTO saveDesignation(String userPm, String designee, String role, UUID pmUserId, String laboratoryUser) ;
  void updateDesignationWithUserPmId(UserDTO userDto);
  boolean existsDesignationsListByDesignatedUserId(UUID designatedUserId);
  Map<String,String> getDesignatedUserIdsByPmId(UUID pmKeycloakId) ;
  void saveDesignationsUsersAttributes(String username);
  List<DesignationsList> getDesignationListOfIndustryUsers(UUID pmKeycloakId) ;
  void saveDesignationsIndustrialUsersAttributes(String username);
  void deleteDesignation(UUID id);
  boolean pmHasEctdByDesignatedUser(UUID designatedUserId) ;
  List<UserRepresentation> getAllPPUsers();
 ResponseEntity<List<ApplicantListUserResponse>> getApplicantsAttributes();

}
