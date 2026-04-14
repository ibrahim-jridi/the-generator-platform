package com.pfe.web.rest;

import static com.pfe.domain.NotificationPushedAsserts.*;
import static com.pfe.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.IntegrationTest;
import com.pfe.domain.NotificationPushed;
import com.pfe.repository.INotificationPushedRepository;
import com.pfe.service.dto.NotificationPushedDTO;
import com.pfe.service.mapper.NotificationPushedMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link NotificationPushedResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationPushedResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_NOTIFICATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NOTIFICATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final UUID DEFAULT_RECIPIENT_ID = UUID.randomUUID();
    private static final UUID UPDATED_RECIPIENT_ID = UUID.randomUUID();

    private static final Boolean DEFAULT_IS_SEEN = false;
    private static final Boolean UPDATED_IS_SEEN = true;

    private static final String ENTITY_API_URL = "/api/notification-pusheds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private INotificationPushedRepository notificationPushedRepository;

    @Autowired
    private NotificationPushedMapper notificationPushedMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationPushedMockMvc;

    private NotificationPushed notificationPushed;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationPushed createEntity(EntityManager em) {
        NotificationPushed notificationPushed = new NotificationPushed()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .message(DEFAULT_MESSAGE)
            .notificationDate(DEFAULT_NOTIFICATION_DATE)
            .recipientId(DEFAULT_RECIPIENT_ID)
            .isSeen(DEFAULT_IS_SEEN);
        return notificationPushed;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationPushed createUpdatedEntity(EntityManager em) {
        NotificationPushed notificationPushed = new NotificationPushed()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .message(UPDATED_MESSAGE)
            .notificationDate(UPDATED_NOTIFICATION_DATE)
            .recipientId(UPDATED_RECIPIENT_ID)
            .isSeen(UPDATED_IS_SEEN);
        return notificationPushed;
    }

    @BeforeEach
    public void initTest() {
        notificationPushed = createEntity(em);
    }

    @Test
    @Transactional
    void createNotificationPushed() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NotificationPushed
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);
        var returnedNotificationPushedDTO = om.readValue(
            restNotificationPushedMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPushedDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificationPushedDTO.class
        );

        // Validate the NotificationPushed in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificationPushed = notificationPushedMapper.toEntity(returnedNotificationPushedDTO);
        assertNotificationPushedUpdatableFieldsEquals(
            returnedNotificationPushed,
            getPersistedNotificationPushed(returnedNotificationPushed)
        );
    }

    @Test
    @Transactional
    void createNotificationPushedWithExistingId() throws Exception {
        // Create the NotificationPushed with an existing ID
        notificationPushedRepository.saveAndFlush(notificationPushed);
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationPushedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPushedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationPushed in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationPushed.setName(null);

        // Create the NotificationPushed, which fails.
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        restNotificationPushedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPushedDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationPushed.setMessage(null);

        // Create the NotificationPushed, which fails.
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        restNotificationPushedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPushedDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotificationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationPushed.setNotificationDate(null);

        // Create the NotificationPushed, which fails.
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        restNotificationPushedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPushedDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipientIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationPushed.setRecipientId(null);

        // Create the NotificationPushed, which fails.
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        restNotificationPushedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPushedDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSeenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationPushed.setIsSeen(null);

        // Create the NotificationPushed, which fails.
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        restNotificationPushedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPushedDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificationPusheds() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList
        restNotificationPushedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationPushed.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].notificationDate").value(hasItem(DEFAULT_NOTIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].recipientId").value(hasItem(DEFAULT_RECIPIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].isSeen").value(hasItem(DEFAULT_IS_SEEN.booleanValue())));
    }

    @Test
    @Transactional
    void getNotificationPushed() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get the notificationPushed
        restNotificationPushedMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationPushed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationPushed.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.notificationDate").value(DEFAULT_NOTIFICATION_DATE.toString()))
            .andExpect(jsonPath("$.recipientId").value(DEFAULT_RECIPIENT_ID.toString()))
            .andExpect(jsonPath("$.isSeen").value(DEFAULT_IS_SEEN.booleanValue()));
    }

    @Test
    @Transactional
    void getNotificationPushedsByIdFiltering() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        UUID id = notificationPushed.getId();

        defaultNotificationPushedFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where name equals to
        defaultNotificationPushedFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where name in
        defaultNotificationPushedFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where name is not null
        defaultNotificationPushedFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByNameContainsSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where name contains
        defaultNotificationPushedFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where name does not contain
        defaultNotificationPushedFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where description equals to
        defaultNotificationPushedFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where description in
        defaultNotificationPushedFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where description is not null
        defaultNotificationPushedFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where description contains
        defaultNotificationPushedFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where description does not contain
        defaultNotificationPushedFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where message equals to
        defaultNotificationPushedFiltering("message.equals=" + DEFAULT_MESSAGE, "message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where message in
        defaultNotificationPushedFiltering("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE, "message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where message is not null
        defaultNotificationPushedFiltering("message.specified=true", "message.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByMessageContainsSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where message contains
        defaultNotificationPushedFiltering("message.contains=" + DEFAULT_MESSAGE, "message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where message does not contain
        defaultNotificationPushedFiltering("message.doesNotContain=" + UPDATED_MESSAGE, "message.doesNotContain=" + DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByNotificationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where notificationDate equals to
        defaultNotificationPushedFiltering(
            "notificationDate.equals=" + DEFAULT_NOTIFICATION_DATE,
            "notificationDate.equals=" + UPDATED_NOTIFICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByNotificationDateIsInShouldWork() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where notificationDate in
        defaultNotificationPushedFiltering(
            "notificationDate.in=" + DEFAULT_NOTIFICATION_DATE + "," + UPDATED_NOTIFICATION_DATE,
            "notificationDate.in=" + UPDATED_NOTIFICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByNotificationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where notificationDate is not null
        defaultNotificationPushedFiltering("notificationDate.specified=true", "notificationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByRecipientIdIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where recipientId equals to
        defaultNotificationPushedFiltering("recipientId.equals=" + DEFAULT_RECIPIENT_ID, "recipientId.equals=" + UPDATED_RECIPIENT_ID);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByRecipientIdIsInShouldWork() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where recipientId in
        defaultNotificationPushedFiltering(
            "recipientId.in=" + DEFAULT_RECIPIENT_ID + "," + UPDATED_RECIPIENT_ID,
            "recipientId.in=" + UPDATED_RECIPIENT_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByRecipientIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where recipientId is not null
        defaultNotificationPushedFiltering("recipientId.specified=true", "recipientId.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByIsSeenIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where isSeen equals to
        defaultNotificationPushedFiltering("isSeen.equals=" + DEFAULT_IS_SEEN, "isSeen.equals=" + UPDATED_IS_SEEN);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByIsSeenIsInShouldWork() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where isSeen in
        defaultNotificationPushedFiltering("isSeen.in=" + DEFAULT_IS_SEEN + "," + UPDATED_IS_SEEN, "isSeen.in=" + UPDATED_IS_SEEN);
    }

    @Test
    @Transactional
    void getAllNotificationPushedsByIsSeenIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        // Get all the notificationPushedList where isSeen is not null
        defaultNotificationPushedFiltering("isSeen.specified=true", "isSeen.specified=false");
    }

    private void defaultNotificationPushedFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNotificationPushedShouldBeFound(shouldBeFound);
        defaultNotificationPushedShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationPushedShouldBeFound(String filter) throws Exception {
        restNotificationPushedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationPushed.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].notificationDate").value(hasItem(DEFAULT_NOTIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].recipientId").value(hasItem(DEFAULT_RECIPIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].isSeen").value(hasItem(DEFAULT_IS_SEEN.booleanValue())));

        // Check, that the count call also returns 1
        restNotificationPushedMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationPushedShouldNotBeFound(String filter) throws Exception {
        restNotificationPushedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationPushedMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotificationPushed() throws Exception {
        // Get the notificationPushed
        restNotificationPushedMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificationPushed() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationPushed
        NotificationPushed updatedNotificationPushed = notificationPushedRepository.findById(notificationPushed.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNotificationPushed are not directly saved in db
        em.detach(updatedNotificationPushed);
        updatedNotificationPushed
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .message(UPDATED_MESSAGE)
            .notificationDate(UPDATED_NOTIFICATION_DATE)
            .recipientId(UPDATED_RECIPIENT_ID)
            .isSeen(UPDATED_IS_SEEN);
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(updatedNotificationPushed);

        restNotificationPushedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationPushedDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationPushedDTO))
            )
            .andExpect(status().isOk());

        // Validate the NotificationPushed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationPushedToMatchAllProperties(updatedNotificationPushed);
    }

    @Test
    @Transactional
    void putNonExistingNotificationPushed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPushed.setId(UUID.randomUUID());

        // Create the NotificationPushed
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationPushedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationPushedDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationPushedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationPushed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificationPushed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPushed.setId(UUID.randomUUID());

        // Create the NotificationPushed
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationPushedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationPushedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationPushed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificationPushed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPushed.setId(UUID.randomUUID());

        // Create the NotificationPushed
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationPushedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPushedDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationPushed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationPushedWithPatch() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationPushed using partial update
        NotificationPushed partialUpdatedNotificationPushed = new NotificationPushed();
        partialUpdatedNotificationPushed.setId(notificationPushed.getId());

        partialUpdatedNotificationPushed.description(UPDATED_DESCRIPTION).message(UPDATED_MESSAGE).isSeen(UPDATED_IS_SEEN);

        restNotificationPushedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationPushed.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationPushed))
            )
            .andExpect(status().isOk());

        // Validate the NotificationPushed in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationPushedUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationPushed, notificationPushed),
            getPersistedNotificationPushed(notificationPushed)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificationPushedWithPatch() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationPushed using partial update
        NotificationPushed partialUpdatedNotificationPushed = new NotificationPushed();
        partialUpdatedNotificationPushed.setId(notificationPushed.getId());

        partialUpdatedNotificationPushed
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .message(UPDATED_MESSAGE)
            .notificationDate(UPDATED_NOTIFICATION_DATE)
            .recipientId(UPDATED_RECIPIENT_ID)
            .isSeen(UPDATED_IS_SEEN);

        restNotificationPushedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationPushed.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationPushed))
            )
            .andExpect(status().isOk());

        // Validate the NotificationPushed in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationPushedUpdatableFieldsEquals(
            partialUpdatedNotificationPushed,
            getPersistedNotificationPushed(partialUpdatedNotificationPushed)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNotificationPushed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPushed.setId(UUID.randomUUID());

        // Create the NotificationPushed
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationPushedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationPushedDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationPushedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationPushed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificationPushed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPushed.setId(UUID.randomUUID());

        // Create the NotificationPushed
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationPushedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationPushedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationPushed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificationPushed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPushed.setId(UUID.randomUUID());

        // Create the NotificationPushed
        NotificationPushedDTO notificationPushedDTO = notificationPushedMapper.toDto(notificationPushed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationPushedMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationPushedDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationPushed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificationPushed() throws Exception {
        // Initialize the database
        notificationPushedRepository.saveAndFlush(notificationPushed);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificationPushed
        restNotificationPushedMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationPushed.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationPushedRepository.count();
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

    protected NotificationPushed getPersistedNotificationPushed(NotificationPushed notificationPushed) {
        return notificationPushedRepository.findById(notificationPushed.getId()).orElseThrow();
    }

    protected void assertPersistedNotificationPushedToMatchAllProperties(NotificationPushed expectedNotificationPushed) {
        assertNotificationPushedAllPropertiesEquals(expectedNotificationPushed, getPersistedNotificationPushed(expectedNotificationPushed));
    }

    protected void assertPersistedNotificationPushedToMatchUpdatableProperties(NotificationPushed expectedNotificationPushed) {
        assertNotificationPushedAllUpdatablePropertiesEquals(
            expectedNotificationPushed,
            getPersistedNotificationPushed(expectedNotificationPushed)
        );
    }
}
