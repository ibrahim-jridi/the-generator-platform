package com.pfe.service.impl;

import com.pfe.domain.WaitingList;
import com.pfe.domain.enumeration.Category;
import com.pfe.domain.enumeration.Governorate;
import com.pfe.domain.enumeration.StatusWaitingList;
import com.pfe.repository.WaitingListRepository;
import com.pfe.security.SecurityUtils;
import com.pfe.service.UserService;
import com.pfe.service.WaitingListService;
import com.pfe.service.dto.WaitingListDTO;
import com.pfe.service.dto.response.WaitingListResponse;
import com.pfe.service.mapper.UserMapper;
import com.pfe.service.mapper.WaitingListMapper;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WaitingListServiceImpl implements WaitingListService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final WaitingListRepository waitingListRepository;
    private final WaitingListMapper waitingListMapper;
    private final UserService userService;
    private final UserMapper userMapper;


    public WaitingListServiceImpl(WaitingListRepository waitingListRepository, WaitingListMapper waitingListMapper,
        UserService userService,UserMapper userMapper) {
        this.waitingListRepository = waitingListRepository;
        this.waitingListMapper = waitingListMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public WaitingListDTO save(WaitingListDTO waitingListDTO) {
        log.debug("Request to save WaitingList: {}", waitingListDTO);

        Optional<WaitingList> existingWaitingListOpt = this.waitingListRepository.findByIdUser(waitingListDTO.getIdUser());

        boolean exist = existingWaitingListOpt.isPresent() && existingWaitingListOpt.get().getStatus() == StatusWaitingList.UNSUBSCRIBE;

        WaitingList waitingList;
        if (exist) {
            waitingList = existingWaitingListOpt.get();
            waitingList.setStatus(StatusWaitingList.REGISTRED);
            waitingList.setGovernorate(waitingListDTO.getGovernorate());
            waitingList.setCategory(waitingListDTO.getCategory());
            waitingList.setId(existingWaitingListOpt.get().getId());
            if (waitingListDTO.getDelegation() != null) {
                waitingList.setDelegation(waitingListDTO.getDelegation());
                waitingList.setMunicipality(null);
            } else if (waitingListDTO.getMunicipality() != null) {
                waitingList.setMunicipality(waitingListDTO.getMunicipality());
                waitingList.setDelegation(null);
            }
        } else {
            if (this.waitingListRepository.existsByIdUserAndStatus(waitingListDTO.getIdUser(), StatusWaitingList.REGISTRED)) {
                throw new IllegalArgumentException("Cet utilisateur est déjà inscrit dans la liste d'attente avec le statut REGISTRED.");
            }
            waitingList = this.waitingListMapper.toEntity(waitingListDTO);
            waitingList.setStatus(StatusWaitingList.REGISTRED);
        }

        List<WaitingList> existingWaitingLists = this.waitingListRepository.findByCriteriaSortedByCreatedDate(
            waitingList.getGovernorate(),
            waitingList.getCategory(),
            waitingList.getDelegation(),
            waitingList.getMunicipality(),
            StatusWaitingList.REGISTRED
        );

        int newRank = existingWaitingLists.size() + 1;
        waitingList.setRank(newRank);

        WaitingList waitingListSaved = this.waitingListRepository.save(waitingList);
        return this.waitingListMapper.toDto(waitingListSaved);
    }

    @Override
    public boolean isUserUnsubscribedOrNotExist() {
        String userId = SecurityUtils.getUserIdFromCurrentUser();
        UUID userUuid = UUID.fromString(userId);

        return this.waitingListRepository.findByIdUser(userUuid)
            .map(waitingList -> waitingList.getStatus() == StatusWaitingList.UNSUBSCRIBE)
            .orElse(true);
    }
    @Override
    public Integer getUserRank() {
        String userId = SecurityUtils.getUserIdFromCurrentUser();
        UUID userUuid = UUID.fromString(userId);

        return this.waitingListRepository.findByIdUser(userUuid)
                .map(WaitingList::getRank)
                .orElse(null);
    }
    @Override
    public List<WaitingListResponse> getAllWaitingList() {
        return this.waitingListRepository.findAllWithUserNames().stream()
            .map(projection -> {
                WaitingListDTO dto = this.waitingListMapper.toDto(projection.getWaitingList());
                return new WaitingListResponse(dto, projection.getFirstName(), projection.getLastName());
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void unsubscribeFromWaitingList(UUID userId) {
        log.info("Attempting to unsubscribe user [{}] from the waiting list", userId);

        WaitingList user = this.waitingListRepository.findByIdUser(userId)
            .orElseThrow(() -> {
                log.error("User [{}] not found in waiting list", userId);
                return new RuntimeException("User not found in waiting list");
            });

        Category category = user.getCategory();
        int oldRank = user.getRank();
        String delegation = user.getDelegation();
        String municipality = user.getMunicipality();
        Governorate governorate = user.getGovernorate();

        log.debug("User [{}] found with rank [{}], category [{}], governorate [{}], delegation [{}], municipality [{}]",
            userId, oldRank, category, governorate, delegation, municipality);

        user.setStatus(StatusWaitingList.UNSUBSCRIBE);
        user.setRank(0);
        this.waitingListRepository.save(user);
        log.info("User [{}] marked as unsubscribed and rank reset", userId);

        List<WaitingList> toUpdate;
        if (delegation != null) {
            toUpdate = this.waitingListRepository
                .findAllWithRankGreaterThanByCategoryAndGovernorateAndDelegation(category, governorate, delegation, oldRank);
            log.debug("Fetched {} users in scope by delegation", toUpdate.size());
        } else if (municipality != null) {
            toUpdate = this.waitingListRepository
                .findAllWithRankGreaterThanByCategoryAndGovernorateAndMunicipality(category, governorate, municipality, oldRank);
            log.debug("Fetched {} users in scope by municipality", toUpdate.size());
        } else {
            log.error("User [{}] does not have a delegation or municipality", userId);
            throw new RuntimeException("User must have either delegation or municipality");
        }

        for (WaitingList other : toUpdate) {
            other.setRank(other.getRank() - 1);
        }

        this.waitingListRepository.saveAll(toUpdate);
        log.info("Updated ranks for {} users after unsubscription of user [{}]", toUpdate.size(), userId);
    }

    @Override
    public ResponseEntity<WaitingListDTO> getWaitingListByUserId(UUID userId) {
        log.info("Fetching waiting list for user [{}]", userId);

        WaitingList waitingList = this.waitingListRepository.findByIdUser(userId)
            .orElseThrow(() -> {
                log.error("No waiting list entry found for user ID: [{}]", userId);
                return new ResourceNotFoundException("No waiting list entries found for user ID: " + userId);
            });

        WaitingListDTO dto = this.waitingListMapper.toDto(waitingList);
        log.debug("Returning waiting list DTO for user [{}]: {}", userId, dto);
        return ResponseEntity.ok(dto);
    }

    @Override
    public WaitingListDTO updateCategory(UUID idUser, WaitingListDTO waitingListDTO) {
        WaitingList existing = this.waitingListRepository.findByIdUserAndStatus(idUser, StatusWaitingList.REGISTRED)
            .orElseThrow(() -> {
                log.error("User {} is not registered in the waiting list.", idUser);
                return new IllegalArgumentException("L'utilisateur n'est pas inscrit.");
            });

        Governorate newGov = waitingListDTO.getGovernorate();
        Category newCat = waitingListDTO.getCategory();
        String newDel = sanitize(waitingListDTO.getDelegation());
        String newComm = sanitize(waitingListDTO.getMunicipality());

        log.debug("New category: {}, Governorate: {}, Delegation: {}, Municipality: {}",
            newCat, newGov, newDel, newComm);
        validateCategoryRequirements(newCat, newGov, newDel, newComm);

        log.info("Category requirements validated for user: {}", idUser);
        boolean categoryChanged = !Objects.equals(existing.getCategory(), newCat);
        boolean locationChanged = categoryChanged || hasLocationChanged(existing, newGov, newDel, newComm);

        if (!locationChanged) {
            log.warn("No location or category change detected for user: {}", idUser);
            throw new IllegalArgumentException("Le renouvellement est applicable uniquement en cas de changement de localisation ou de catégorie.");
        }

        adjustOldRanks(existing);
        log.debug("Old ranks adjusted for user: {}", idUser);

        int newRank = findNextRank(newGov, newCat, newDel, newComm);
        log.info("New rank calculated: {} for user: {}", newRank, idUser);

        existing.setGovernorate(newGov);
        existing.setCategory(newCat);
        existing.setRank(newRank);
        existing.setDateRenewal(LocalDate.now());
        existing.setDelegation(newCat == Category.CATEGORY_A ? newDel : null);
        existing.setMunicipality(newCat == Category.CATEGORY_B ? newComm : null);

        WaitingList saved = this.waitingListRepository.save(existing);
        log.info("Category update completed for user: {}. New record ID: {}", idUser, saved.getId());

        return this.waitingListMapper.toDto(saved);

    }

    private String sanitize(String value) {
        return (value != null && !value.isBlank()) ? value : null;
    }

    private void validateCategoryRequirements(Category category, Governorate gov, String del, String comm) {
        if (gov == null) {
            throw new IllegalArgumentException("Le gouvernorat est requis.");
        }

        if (category == Category.CATEGORY_A) {
            if (del == null || comm != null) {
                throw new IllegalArgumentException("Catégorie A nécessite une délégation et pas de commune.");
            }
        } else if (category == Category.CATEGORY_B) {
            if (comm == null || del != null) {
                throw new IllegalArgumentException("Catégorie B nécessite une commune et pas de délégation.");
            }
        }
    }

    private boolean hasLocationChanged(WaitingList user, Governorate newGov, String newDel, String newComm) {
        if (user.getCategory() == Category.CATEGORY_A) {
            return !Objects.equals(user.getGovernorate(), newGov) || !Objects.equals(user.getDelegation(), newDel);
        } else {
            return !Objects.equals(user.getGovernorate(), newGov) || !Objects.equals(user.getMunicipality(), newComm);
        }
    }

    private void adjustOldRanks(WaitingList user) {
        List<WaitingList> affected;

        if (user.getCategory() == Category.CATEGORY_A && user.getDelegation() != null) {
            affected = this.waitingListRepository.findAllWithRankGreaterThanByCategoryAndGovernorateAndDelegation(
                user.getCategory(), user.getGovernorate(), user.getDelegation(), user.getRank());
        } else if (user.getCategory() == Category.CATEGORY_B && user.getMunicipality() != null) {
            affected = this.waitingListRepository.findAllWithRankGreaterThanByCategoryAndGovernorateAndMunicipality(
                user.getCategory(), user.getGovernorate(), user.getMunicipality(), user.getRank());
        } else {
            throw new RuntimeException("Utilisateur invalide : délégation ou commune manquante.");
        }

        for (WaitingList w : affected) {
            w.setRank(w.getRank() - 1);
        }
        this.waitingListRepository.saveAll(affected);
    }

    private int findNextRank(Governorate gov, Category cat, String del, String comm) {
        List<WaitingList> list = this.waitingListRepository.findByCriteriaSortedByCreatedDate(
            gov, cat, del, comm, StatusWaitingList.REGISTRED);
        return list.stream().mapToInt(WaitingList::getRank).max().orElse(0) + 1;
    }


    @Override
    public WaitingListDTO update(UUID idUser, WaitingListDTO waitingListDTO) {
        WaitingList existing = this.waitingListRepository
            .findByIdUserAndStatus(idUser, StatusWaitingList.REGISTRED)
            .orElseThrow(() -> new IllegalArgumentException("L'utilisateur n'est pas inscrit."));

        int oldRank = existing.getRank();
        Governorate oldGovernorate = existing.getGovernorate();
        String oldDelegation = existing.getDelegation();
        String oldMunicipality = existing.getMunicipality();
        Category category = existing.getCategory();

        Governorate newGovernorate = waitingListDTO.getGovernorate();
        String newDelegation = (waitingListDTO.getDelegation() != null && !waitingListDTO.getDelegation().isBlank())
            ? waitingListDTO.getDelegation()
            : null;
        String newMunicipality = (waitingListDTO.getMunicipality() != null && !waitingListDTO.getMunicipality().isBlank())
            ? waitingListDTO.getMunicipality()
            : null;

        boolean locationChanged = false;
        if (category == Category.CATEGORY_A) {
            locationChanged = !Objects.equals(oldGovernorate, newGovernorate)
                || !Objects.equals(oldDelegation, newDelegation);
        } else if (category == Category.CATEGORY_B) {
            locationChanged = !Objects.equals(oldGovernorate, newGovernorate)
                || !Objects.equals(oldMunicipality, newMunicipality);
        }

        if (!locationChanged) {
            throw new IllegalArgumentException("Le renouvellement est applicable uniquement en cas de changement de localisation.");
        }

        List<WaitingList> toUpdate;
        if (category == Category.CATEGORY_A && oldDelegation != null) {
            toUpdate = this.waitingListRepository
                .findAllWithRankGreaterThanByCategoryAndGovernorateAndDelegation(
                    category, oldGovernorate, oldDelegation, oldRank
                );
            log.debug("Fetched {} users in old delegation to update rank", toUpdate.size());
        } else if (category == Category.CATEGORY_B && oldMunicipality != null) {
            toUpdate = this.waitingListRepository
                .findAllWithRankGreaterThanByCategoryAndGovernorateAndMunicipality(
                    category, oldGovernorate, oldMunicipality, oldRank
                );
            log.debug("Fetched {} users in old municipality to update rank", toUpdate.size());
        } else {
            log.error("User [{}] has no delegation or municipality", idUser);
            throw new RuntimeException("User must have a delegation or municipality");
        }

        for (WaitingList user : toUpdate) {
            user.setRank(user.getRank() - 1);
        }
        this.waitingListRepository.saveAll(toUpdate);

        List<WaitingList> newLocalList = this.waitingListRepository.findByCriteriaSortedByCreatedDate(
            newGovernorate,
            category,
            newDelegation,
            newMunicipality,
            StatusWaitingList.REGISTRED
        );

        int newRank = newLocalList.stream()
            .mapToInt(WaitingList::getRank)
            .max()
            .orElse(0) + 1;

        existing.setGovernorate(newGovernorate);
        existing.setDateRenewal(LocalDate.now());
        existing.setRank(newRank);

        if (category == Category.CATEGORY_A) {
            existing.setDelegation(newDelegation);
            existing.setMunicipality(null);
        } else if (category == Category.CATEGORY_B) {
            existing.setMunicipality(newMunicipality);
            existing.setDelegation(null);
        }

        WaitingList saved = this.waitingListRepository.save(existing);
        return this.waitingListMapper.toDto(saved);
    }


}
