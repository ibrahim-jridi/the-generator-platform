package com.pfe.services.servicesImplementation;

import com.pfe.domain.SizeConfig;
import com.pfe.dto.SizeConfigDto;
import com.pfe.mappers.SizeConfigMapper;
import com.pfe.repository.SizeConfigRepository;
import com.pfe.services.SizeConfigService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SizeConfigServiceImpl implements SizeConfigService {
    private final SizeConfigRepository sizeConfigRepository;

    private final SizeConfigMapper sizeConfigMapper;

    public SizeConfigServiceImpl(SizeConfigRepository sizeConfigRepository, SizeConfigMapper sizeConfigMapper) {
        this.sizeConfigRepository = sizeConfigRepository;
        this.sizeConfigMapper = sizeConfigMapper;

    }

    @Override
    public SizeConfigDto createSizeConfig(SizeConfigDto sizeConfigDto) {
        SizeConfig sizeConfig = sizeConfigMapper.toEntity(sizeConfigDto);
        SizeConfig savedSizeConfig = sizeConfigRepository.save(sizeConfig);
        return sizeConfigMapper.toDto(savedSizeConfig);
    }

    @Override
    public SizeConfigDto updateSizeConfig(UUID id, SizeConfigDto sizeConfigDto) {
        SizeConfig sizeConfig = sizeConfigRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("SizeConfig not found with ID " + id));

        sizeConfig.setExtension(sizeConfigDto.getExtension());
        sizeConfig.setMaxSize(sizeConfigDto.getMaxSize());

        SizeConfig updatedSizeConfig = sizeConfigRepository.save(sizeConfig);
        return sizeConfigMapper.toDto(updatedSizeConfig);
    }


    @Override
    public void deleteSizeConfig(UUID id) {
        sizeConfigRepository.deleteById(id);
    }

    @Override
    public List<SizeConfigDto> getAllSizeConfigs() {
        List<SizeConfig> sizeConfigs = sizeConfigRepository.findAll();
        return sizeConfigs.stream()
            .map(sizeConfigMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public SizeConfigDto getSizeConfigById(UUID id) {
        return sizeConfigRepository.findById(id)
            .map(sizeConfigMapper::toDto)
            .orElseThrow(() -> new RuntimeException("SizeConfig not found with ID " + id));
    }
}
