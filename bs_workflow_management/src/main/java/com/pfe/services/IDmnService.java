package com.pfe.services;

import com.pfe.dto.DeployDTO;
import com.pfe.dto.DmnActivityDecisionDefinitionDTO;
import com.pfe.dto.XmlFileDto;
import com.pfe.services.criteria.DecisionDefinitionCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDmnService {

    List<DmnActivityDecisionDefinitionDTO> findAllDmmActivityDecisionDefinition();

    List<DmnActivityDecisionDefinitionDTO> findDmmActivityDecisionDefinitionByKey(String key);

    String findDmmActivityDecisionDefinitionById(String id);

    DeployDTO deployDecisionDefinition(XmlFileDto dmnData);

    Page<DmnActivityDecisionDefinitionDTO> findAllDmmActivityDecisionDefinitionByCriteria(DecisionDefinitionCriteria criteria, Pageable pageable);


}
