package com.pfe.services.servicesImplementation;

import com.pfe.dto.ProcessDefinitionDTO;
import com.pfe.web.rest.errors.ProcessEngineException;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IBPMNServiceTest {
    @Mock
    private ProcessEngine processEngine;

    @Mock
    private RepositoryService repositoryService;

    private ProcessDefinitionDTO sampleDTO;


    @InjectMocks
    private ProcessEngineServiceImpl processEngineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.sampleDTO = new ProcessDefinitionDTO();
        this.sampleDTO.setName("TestProcess");
    }


    @Test
    void deployDecisionDefinition_success() throws IOException {
        // Arrange
        String bpmnFilePath = "src/test/resources/bpmnForTest/process.bpmn";
        String bpmnXml = new String(Files.readAllBytes(Paths.get(bpmnFilePath)));

        Deployment mockDeployment = mock(Deployment.class);
        DeploymentBuilder mockDeploymentBuilder = mock(DeploymentBuilder.class);

        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createDeployment()).thenReturn(mockDeploymentBuilder);

        // Mock chaining methods to return the mock object itself
        when(mockDeploymentBuilder.name(this.sampleDTO.getName())).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.addString("TestProcess.bpmn", bpmnXml)).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.deploy()).thenReturn(mockDeployment);

        // Act
        Deployment result = this.processEngineService.deployProcessDefinition(this.sampleDTO.getName(), bpmnXml);

        // Assert
        assertNotNull(result);
        assertEquals(mockDeployment, result);
        verify(mockDeploymentBuilder, times(1)).deploy();
    }

    @Test
    void deployProcessDefinition_processEngineException() throws IOException {
        // Arrange
        String bpmnFilePath = "src/test/resources/bpmnForTest/process.bpmn";
        String bpmnXml = new String(Files.readAllBytes(Paths.get(bpmnFilePath)));

        DeploymentBuilder mockDeploymentBuilder = mock(DeploymentBuilder.class);

        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createDeployment()).thenReturn(mockDeploymentBuilder);

        // Mock chaining methods to simulate deployment failure
        when(mockDeploymentBuilder.name(this.sampleDTO.getName())).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.addString(this.sampleDTO.getName() + ".bpmn", bpmnXml)).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.deploy()).thenThrow(new RuntimeException("Deployment failed"));

        // Act & Assert
        ProcessEngineException exception = assertThrows(
            ProcessEngineException.class,
            () -> this.processEngineService.deployProcessDefinition(this.sampleDTO.getName(), bpmnXml)
        );

        // Validate the exception message
        assertTrue(exception.getMessage().contains("Failed to deploy process definition: " + this.sampleDTO.getName()));
    }

}
