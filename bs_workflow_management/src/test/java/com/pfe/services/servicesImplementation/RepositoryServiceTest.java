package com.pfe.services.servicesImplementation;

import com.pfe.dto.ProcessDefinitionDTO;
import com.pfe.mapper.ProcessDefinitionMapper;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RepositoryServiceTest {

    @Mock
    private ProcessDefinitionQuery processDefinitionQuery;

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private ProcessDefinitionMapper processDefinitionMapper;

    @InjectMocks
    private RepositoryServiceImpl repositoryServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDeployedProcesses() {
        // Arrange
        ProcessDefinition mockDefinition = mock(ProcessDefinition.class);
        when(mockDefinition.getId()).thenReturn("processId");
        when(mockDefinition.getName()).thenReturn("processName");
        when(mockDefinition.getKey()).thenReturn("processKey");
        when(mockDefinition.getVersion()).thenReturn(1);
        when(mockDefinition.getDeploymentId()).thenReturn("deploymentId");
        when(mockDefinition.getResourceName()).thenReturn("resourceName");
        when(mockDefinition.isSuspended()).thenReturn(false);
        when(mockDefinition.isStartableInTasklist()).thenReturn(true);

        // Map the ProcessDefinition to a ProcessDefinitionDTO
        ProcessDefinitionDTO processDefinitionDTO = new ProcessDefinitionDTO();
        processDefinitionDTO.setId("processId");
        processDefinitionDTO.setName("processName");
        processDefinitionDTO.setKey("processKey");
        processDefinitionDTO.setVersion(1);
        processDefinitionDTO.setDeploymentId("deploymentId");
        processDefinitionDTO.setResourceName("resourceName");
        processDefinitionDTO.setSuspensionState(false);
        processDefinitionDTO.setStartableInTasklist(true);

        when(processDefinitionMapper.toDto(mockDefinition)).thenReturn(processDefinitionDTO);

        List<ProcessDefinition> processDefinitions = Arrays.asList(mockDefinition);
        Pageable pageable = PageRequest.of(0, 10);

        // Mock the process definition query chain
        when(repositoryService.createProcessDefinitionQuery()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.orderByDeploymentTime()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.desc()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.listPage((int) pageable.getOffset(), pageable.getPageSize()))
            .thenReturn(processDefinitions);

        // Mock the count
        when(processDefinitionQuery.count()).thenReturn(1L);

        // Act
        Page<ProcessDefinitionDTO> result = repositoryServiceImpl.getDeployedProcesses(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        ProcessDefinitionDTO resultDto = result.getContent().get(0);
        assertEquals("processId", resultDto.getId());
        assertEquals("processName", resultDto.getName());
        assertEquals("processKey", resultDto.getKey());
        assertEquals(1, resultDto.getVersion());
        assertEquals("deploymentId", resultDto.getDeploymentId());
        assertEquals("resourceName", resultDto.getResourceName());
        assertFalse(resultDto.isSuspensionState());
        assertTrue(resultDto.getStartableInTasklist());
    }
}

