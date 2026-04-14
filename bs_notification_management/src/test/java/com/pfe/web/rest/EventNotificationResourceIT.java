package com.pfe.web.rest;

import static com.pfe.domain.EventNotificationAsserts.*;
import static com.pfe.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.IntegrationTest;
import com.pfe.domain.EventNotification;
import com.pfe.domain.Notification;
import com.pfe.domain.enums.Status;
import com.pfe.repository.EventNotificationRepository;
import com.pfe.service.dto.EventNotificationDTO;
import com.pfe.service.mapper.EventNotificationMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EventNotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventNotificationResourceIT {

    private static final String DEFAULT_TASK_ID = "AAAAAAAAAA";
    private static final String UPDATED_TASK_ID = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.STARTED;
    private static final Status UPDATED_STATUS = Status.STOPPED;

    private static final String ENTITY_API_URL = "/api/event-notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EventNotificationRepository eventNotificationRepository;

    @Autowired
    private EventNotificationMapper eventNotificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventNotificationMockMvc;

    private EventNotification eventNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventNotification createEntity(EntityManager em) {
        EventNotification eventNotification = new EventNotification().taskId(DEFAULT_TASK_ID).status(DEFAULT_STATUS);
        return eventNotification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventNotification createUpdatedEntity(EntityManager em) {
        EventNotification eventNotification = new EventNotification().taskId(UPDATED_TASK_ID).status(UPDATED_STATUS);
        return eventNotification;
    }

    @BeforeEach
    public void initTest() {
        eventNotification = createEntity(em);
    }

    @Test
    @Transactional
    void createEventNotification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);
        var returnedEventNotificationDTO = om.readValue(
            restEventNotificationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventNotificationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EventNotificationDTO.class
        );

        // Validate the EventNotification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEventNotification = eventNotificationMapper.toEntity(returnedEventNotificationDTO);
        assertEventNotificationUpdatableFieldsEquals(returnedEventNotification, getPersistedEventNotification(returnedEventNotification));
    }

    @Test
    @Transactional
    void createEventNotificationWithExistingId() throws Exception {
        // Create the EventNotification with an existing ID
        eventNotificationRepository.saveAndFlush(eventNotification);
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventNotificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTaskIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventNotification.setTaskId(null);

        // Create the EventNotification, which fails.
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        restEventNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventNotificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventNotification.setStatus(null);

        // Create the EventNotification, which fails.
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        restEventNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventNotificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventNotifications() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventNotification.getId().toString())))
            .andExpect(jsonPath("$.[*].taskId").value(hasItem(DEFAULT_TASK_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getEventNotification() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        // Get the eventNotification
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, eventNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventNotification.getId().toString()))
            .andExpect(jsonPath("$.taskId").value(DEFAULT_TASK_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getEventNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        UUID id = eventNotification.getId();

        defaultEventNotificationFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByTaskIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where taskId equals to
        defaultEventNotificationFiltering("taskId.equals=" + DEFAULT_TASK_ID, "taskId.equals=" + UPDATED_TASK_ID);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByTaskIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where taskId in
        defaultEventNotificationFiltering("taskId.in=" + DEFAULT_TASK_ID + "," + UPDATED_TASK_ID, "taskId.in=" + UPDATED_TASK_ID);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByTaskIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where taskId is not null
        defaultEventNotificationFiltering("taskId.specified=true", "taskId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventNotificationsByTaskIdContainsSomething() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where taskId contains
        defaultEventNotificationFiltering("taskId.contains=" + DEFAULT_TASK_ID, "taskId.contains=" + UPDATED_TASK_ID);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByTaskIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where taskId does not contain
        defaultEventNotificationFiltering("taskId.doesNotContain=" + UPDATED_TASK_ID, "taskId.doesNotContain=" + DEFAULT_TASK_ID);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where status equals to
        defaultEventNotificationFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where status in
        defaultEventNotificationFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventNotificationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        // Get all the eventNotificationList where status is not null
        defaultEventNotificationFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllEventNotificationsByNotificationIsEqualToSomething() throws Exception {
        Notification notification;
        if (TestUtil.findAll(em, Notification.class).isEmpty()) {
            eventNotificationRepository.saveAndFlush(eventNotification);
            //notification = NotificationResourceIT.createEntity(em);
        } else {
            notification = TestUtil.findAll(em, Notification.class).get(0);
        }
       // em.persist(notification);
        em.flush();
       // eventNotification.setNotification(notification);
        eventNotificationRepository.saveAndFlush(eventNotification);
       // UUID notificationId = notification.getId();
        // Get all the eventNotificationList where notification equals to notificationId
        //defaultEventNotificationShouldBeFound("notificationId.equals=" + notificationId);

        // Get all the eventNotificationList where notification equals to (notificationId + 1)
        defaultEventNotificationShouldNotBeFound("notificationId.equals=" + (UUID.randomUUID()));
    }

    private void defaultEventNotificationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEventNotificationShouldBeFound(shouldBeFound);
        defaultEventNotificationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventNotificationShouldBeFound(String filter) throws Exception {
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventNotification.getId().toString())))
            .andExpect(jsonPath("$.[*].taskId").value(hasItem(DEFAULT_TASK_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventNotificationShouldNotBeFound(String filter) throws Exception {
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventNotification() throws Exception {
        // Get the eventNotification
        restEventNotificationMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventNotification() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventNotification
        EventNotification updatedEventNotification = eventNotificationRepository.findById(eventNotification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventNotification are not directly saved in db
        em.detach(updatedEventNotification);
        updatedEventNotification.taskId(UPDATED_TASK_ID).status(UPDATED_STATUS);
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(updatedEventNotification);

        restEventNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventNotificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEventNotificationToMatchAllProperties(updatedEventNotification);
    }

    @Test
    @Transactional
    void putNonExistingEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(UUID.randomUUID());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(UUID.randomUUID());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(UUID.randomUUID());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventNotificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventNotificationWithPatch() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventNotification using partial update
        EventNotification partialUpdatedEventNotification = new EventNotification();
        partialUpdatedEventNotification.setId(eventNotification.getId());

        partialUpdatedEventNotification.taskId(UPDATED_TASK_ID).status(UPDATED_STATUS);

        restEventNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventNotification))
            )
            .andExpect(status().isOk());

        // Validate the EventNotification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventNotificationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEventNotification, eventNotification),
            getPersistedEventNotification(eventNotification)
        );
    }

    @Test
    @Transactional
    void fullUpdateEventNotificationWithPatch() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventNotification using partial update
        EventNotification partialUpdatedEventNotification = new EventNotification();
        partialUpdatedEventNotification.setId(eventNotification.getId());

        partialUpdatedEventNotification.taskId(UPDATED_TASK_ID).status(UPDATED_STATUS);

        restEventNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventNotification))
            )
            .andExpect(status().isOk());

        // Validate the EventNotification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventNotificationUpdatableFieldsEquals(
            partialUpdatedEventNotification,
            getPersistedEventNotification(partialUpdatedEventNotification)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(UUID.randomUUID());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventNotificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(UUID.randomUUID());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventNotification.setId(UUID.randomUUID());

        // Create the EventNotification
        EventNotificationDTO eventNotificationDTO = eventNotificationMapper.toDto(eventNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventNotificationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(eventNotificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventNotification() throws Exception {
        // Initialize the database
        eventNotificationRepository.saveAndFlush(eventNotification);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the eventNotification
        restEventNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventNotification.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return eventNotificationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected EventNotification getPersistedEventNotification(EventNotification eventNotification) {
        return eventNotificationRepository.findById(eventNotification.getId()).orElseThrow();
    }

    protected void assertPersistedEventNotificationToMatchAllProperties(EventNotification expectedEventNotification) {
        assertEventNotificationAllPropertiesEquals(expectedEventNotification, getPersistedEventNotification(expectedEventNotification));
    }

    protected void assertPersistedEventNotificationToMatchUpdatableProperties(EventNotification expectedEventNotification) {
        assertEventNotificationAllUpdatablePropertiesEquals(
            expectedEventNotification,
            getPersistedEventNotification(expectedEventNotification)
        );
    }
}
