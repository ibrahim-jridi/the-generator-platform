package com.pfe.repository;

import com.pfe.domain.Days;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the Days entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IDaysRepository extends BaseRepository<Days, UUID>{}
