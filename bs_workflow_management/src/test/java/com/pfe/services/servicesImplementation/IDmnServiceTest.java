package com.pfe.services.servicesImplementation;

import com.pfe.dto.DeployDTO;
import com.pfe.dto.DmnActivityDecisionDefinitionDTO;
import com.pfe.dto.XmlFileDto;
import com.pfe.mapper.DeployMapper;
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

class IDmnServiceTest {

    @Mock
    private ProcessEngine processEngine;

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private DmnActivityDecisionDefinitionDTO sampleDto;

    @Mock
    private DeployMapper deployMapper;

    @InjectMocks
    private DmnServiceImpl dmnService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleDto = new DmnActivityDecisionDefinitionDTO();
        sampleDto.setName("test");
    }

    @Test
    void deployDecisionDefinition_success() throws IOException {
        // Arrange
        String dmnFilePath = "src/test/resources/diagram.dmn";
        String dmnXml = new String(Files.readAllBytes(Paths.get(dmnFilePath)));
        XmlFileDto dmnData = new XmlFileDto();
        dmnData.setName(sampleDto.getName());
        dmnData.setXml(dmnXml);
        Deployment mockDeployment = mock(Deployment.class);
        DeployDTO mockDeployDTO = mock(DeployDTO.class);
        DeploymentBuilder mockDeploymentBuilder = mock(DeploymentBuilder.class);

        when(processEngine.getRepositoryService()).thenReturn(repositoryService);
        when(repositoryService.createDeployment()).thenReturn(mockDeploymentBuilder);

        // Mock chaining methods to return the mock object itself
        when(mockDeploymentBuilder.name(sampleDto.getName())).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.addString(sampleDto.getName()+".dmn", dmnXml)).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.deploy()).thenReturn(mockDeployment);

        when(deployMapper.toDto(mockDeployment)).thenReturn(mockDeployDTO);

        // Act
        DeployDTO result = dmnService.deployDecisionDefinition(dmnData);

        // Assert
        assertNotNull(result);
        assertEquals(mockDeployDTO, result);
        verify(mockDeploymentBuilder, times(1)).deploy();
    }

    @Test
    void deployDecisionDefinition_nullDmnData_throwsException() {
        // Act & Assert
        ProcessEngineException exception = assertThrows(
            ProcessEngineException.class,
            () -> dmnService.deployDecisionDefinition(null)
        );
        assertEquals("Failed to deploy decision definition: null", exception.getMessage());
    }

    @Test
    void deployDecisionDefinition_processEngineException() throws IOException {
        // Arrange
        String dmnFilePath = "src/test/resources/diagram.dmn";
        String dmnXml = new String(Files.readAllBytes(Paths.get(dmnFilePath)));
        XmlFileDto dmnData = new XmlFileDto();
        dmnData.setName(sampleDto.getName());
        dmnData.setXml(dmnXml);

        DeploymentBuilder mockDeploymentBuilder = mock(DeploymentBuilder.class);

        when(processEngine.getRepositoryService()).thenReturn(repositoryService);
        when(repositoryService.createDeployment()).thenReturn(mockDeploymentBuilder);

        // Mock chaining methods to simulate deployment failure
        when(mockDeploymentBuilder.name(sampleDto.getName())).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.addString(sampleDto.getName()+".dmn", dmnXml)).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.deploy()).thenThrow(new RuntimeException("Deployment failed"));

        // Act & Assert
        ProcessEngineException exception = assertThrows(
            ProcessEngineException.class,
            () -> dmnService.deployDecisionDefinition(dmnData)
        );

        // Validate the exception message
        assertTrue(exception.getMessage().contains("Failed to deploy decision definition: " + dmnData));
    }
}

