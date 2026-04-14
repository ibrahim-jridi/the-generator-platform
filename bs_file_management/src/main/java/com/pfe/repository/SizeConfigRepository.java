package com.pfe.repository;

import com.pfe.domain.SizeConfig;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SizeConfigRepository extends BaseRepository<SizeConfig, UUID> {
}
