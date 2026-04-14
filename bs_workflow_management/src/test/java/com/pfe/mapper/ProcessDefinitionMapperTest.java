package com.pfe.mapper;

import com.pfe.dto.ProcessDefinitionDTO;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProcessDefinitionMapperTest {

    private ProcessDefinitionMapper mapper;

    @BeforeEach
    void setUp() {
        // Get a new instance of the mapper
        mapper = Mappers.getMapper(ProcessDefinitionMapper.class);
    }

    @Test
    void toDto_ShouldMapProcessDefinitionToProcessDefinitionDTO() {
        // Mock a ProcessDefinition instance
        ProcessDefinition processDefinition = mock(ProcessDefinition.class);
        when(processDefinition.getId()).thenReturn("processId");
        when(processDefinition.getKey()).thenReturn("processKey");
        when(processDefinition.getName()).thenReturn("Process Name");
        when(processDefinition.getVersion()).thenReturn(1);
        when(processDefinition.getDeploymentId()).thenReturn("deploymentId");
        when(processDefinition.getResourceName()).thenReturn("process.bpmn");
        when(processDefinition.isSuspended()).thenReturn(false);
        when(processDefinition.isStartableInTasklist()).thenReturn(false);

        // Call the mapper method
        ProcessDefinitionDTO dto = mapper.toDto(processDefinition);

        // Verify each field is correctly mapped
        assertNotNull(dto);
        assertEquals("processId", dto.getId());
        assertEquals("processKey", dto.getKey());
        assertEquals("Process Name", dto.getName());
        assertEquals(1, dto.getVersion());
        assertEquals("deploymentId", dto.getDeploymentId());
        assertEquals("process.bpmn", dto.getResourceName());
        assertFalse(dto.isSuspensionState());
        assertFalse(dto.getStartableInTasklist());
    }
}
