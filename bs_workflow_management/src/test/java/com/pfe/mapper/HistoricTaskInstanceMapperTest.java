package com.pfe.mapper;
import com.pfe.dto.HistoricTaskInstanceDTO;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HistoricTaskInstanceMapperTest {

    private HistoricTaskInstanceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(HistoricTaskInstanceMapper.class);
    }

    @Test
    void toDto_ShouldMapHistoricTaskInstanceToHistoricTaskInstanceDTO() {

        // given
        HistoricTaskInstance historicTaskInstance = mock(HistoricTaskInstance.class);
        when(historicTaskInstance.getId()).thenReturn("taskId");
        when(historicTaskInstance.getRootProcessInstanceId()).thenReturn("rootProcessInstanceId");
        when(historicTaskInstance.getProcessDefinitionKey()).thenReturn("processDefinitionKey");
        when(historicTaskInstance.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(historicTaskInstance.getCaseDefinitionId()).thenReturn("caseDefinitionId");
        when(historicTaskInstance.getCaseExecutionId()).thenReturn("caseExecutionId");

        // when
        HistoricTaskInstanceDTO dto = mapper.toDto(historicTaskInstance);

        // then
        assertNotNull(dto);
        assertEquals("taskId", dto.getId());
        assertEquals("rootProcessInstanceId", dto.getRootProcessInstanceId());
        assertEquals("processDefinitionKey", dto.getProcessDefinitionKey());
        assertEquals("processDefinitionId", dto.getProcessDefinitionId());
        assertEquals("caseExecutionId", dto.getCaseExecutionId());
    }

}
