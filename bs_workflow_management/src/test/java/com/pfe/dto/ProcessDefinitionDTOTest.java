package com.pfe.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessDefinitionDTOTest {

    @Test
    void getId() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setId("123");
        assertEquals("123", dto.getId());
    }

    @Test
    void setId() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setId("123");
        assertEquals("123", dto.getId());
    }

    @Test
    void getKey() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setKey("testKey");
        assertEquals("testKey", dto.getKey());
    }

    @Test
    void setKey() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setKey("testKey");
        assertEquals("testKey", dto.getKey());
    }

    @Test
    void getName() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setName("Test Name");
        assertEquals("Test Name", dto.getName());
    }

    @Test
    void setName() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setName("Test Name");
        assertEquals("Test Name", dto.getName());
    }

    @Test
    void getVersion() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setVersion(1);
        assertEquals(1, dto.getVersion());
    }

    @Test
    void setVersion() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setVersion(1);
        assertEquals(1, dto.getVersion());
    }

    @Test
    void getDeploymentId() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setDeploymentId("deployment123");
        assertEquals("deployment123", dto.getDeploymentId());
    }

    @Test
    void setDeploymentId() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setDeploymentId("deployment123");
        assertEquals("deployment123", dto.getDeploymentId());
    }

    @Test
    void getResourceName() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setResourceName("resource.bpmn");
        assertEquals("resource.bpmn", dto.getResourceName());
    }

    @Test
    void setResourceName() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setResourceName("resource.bpmn");
        assertEquals("resource.bpmn", dto.getResourceName());
    }

    @Test
    void isSuspensionState() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setSuspensionState(true);
        assertTrue(dto.isSuspensionState());
    }

    @Test
    void setSuspensionState() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setSuspensionState(false);
        assertFalse(dto.isSuspensionState());
    }

    @Test
    void IsStartableInTasklist() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setStartableInTasklist(true);
        assertTrue(dto.getStartableInTasklist());
    }

    @Test
    void setIsStartableInTasklist() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setStartableInTasklist(false);
        assertFalse(dto.getStartableInTasklist());
    }

    @Test
    void testEquals() {
        ProcessDefinitionDTO dto1 = new ProcessDefinitionDTO("id1", "key1", "name1", 1, "deploy1", "resource1", true, true);
        ProcessDefinitionDTO dto2 = new ProcessDefinitionDTO("id1", "key1", "name1", 1, "deploy1", "resource1", true, true);
        assertEquals(dto1, dto2);
    }

    @Test
    void testHashCode() {
        ProcessDefinitionDTO dto1 = new ProcessDefinitionDTO("id1", "key1", "name1", 1, "deploy1", "resource1", true, true);
        ProcessDefinitionDTO dto2 = new ProcessDefinitionDTO("id1", "key1", "name1", 1, "deploy1", "resource1", true, true);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO("id1", "key1", "name1", 1, "deploy1", "resource1", true, true);
        String expectedString = "ProcessDefinitionDTO{id='id1', key='key1', name='name1', version='1', deploymentId='deploy1', resourceName='resource1', suspensionState=true, startableInTasklist=true}";
        assertEquals(expectedString, dto.toString());
    }
}
