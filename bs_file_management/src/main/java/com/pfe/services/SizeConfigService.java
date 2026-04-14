package com.pfe.services;

import com.pfe.dto.SizeConfigDto;

import java.util.List;
import java.util.UUID;

public interface SizeConfigService {
    SizeConfigDto createSizeConfig(SizeConfigDto sizeConfigDto);

    SizeConfigDto updateSizeConfig(UUID id, SizeConfigDto sizeConfigDto);

    void deleteSizeConfig(UUID id);

    List<SizeConfigDto> getAllSizeConfigs();

    SizeConfigDto getSizeConfigById(UUID id);
}
