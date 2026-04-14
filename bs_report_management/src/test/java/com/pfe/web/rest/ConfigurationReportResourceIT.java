package com.pfe.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.IntegrationTest;
import com.pfe.domain.ConfigurationReport;
import com.pfe.repository.ConfigurationReportRepository;
import com.pfe.service.dto.ConfigurationReportDTO;
import com.pfe.service.mapper.ConfigurationReportMapper;
import com.pfe.domain.ConfigurationReportAsserts;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ConfigurationReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfigurationReportResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "0000";
    private static final String UPDATED_POSTAL_CODE = "8888";

    private static final String DEFAULT_PHONE = "25145223";
    private static final String UPDATED_PHONE = "25114233";

    private static final String DEFAULT_FAX = "25411233";
    private static final String UPDATED_FAX = "25336412";

    private static final String DEFAULT_EMAIL = "test@test.com";
    private static final String UPDATED_EMAIL = "test1@test.com";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_FOOTER = "AAAAAAAAAA";
    private static final String UPDATED_FOOTER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/v1/configuration-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConfigurationReportRepository configurationReportRepository;

    @Autowired
    private ConfigurationReportMapper configurationReportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigurationReportMockMvc;

    private ConfigurationReport configurationReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationReport createEntity(EntityManager em) {
        ConfigurationReport configurationReport = new ConfigurationReport()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .phone(DEFAULT_PHONE)
            .fax(DEFAULT_FAX)
            .email(DEFAULT_EMAIL)
            .logo(DEFAULT_LOGO)
            .footer(DEFAULT_FOOTER);
        return configurationReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationReport createUpdatedEntity(EntityManager em) {
        ConfigurationReport configurationReport = new ConfigurationReport()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .phone(UPDATED_PHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .logo(UPDATED_LOGO)
            .footer(UPDATED_FOOTER);
        return configurationReport;
    }

    @BeforeEach
    public void initTest() {
        configurationReport = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void createConfigurationReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ConfigurationReport
        ConfigurationReportDTO configurationReportDTO = configurationReportMapper.toDto(configurationReport);
        var returnedConfigurationReportDTO = om.readValue(
            restConfigurationReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configurationReportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConfigurationReportDTO.class
        );

        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConfigurationReport = configurationReportMapper.toEntity(returnedConfigurationReportDTO);
        ConfigurationReportAsserts.assertConfigurationReportUpdatableFieldsEquals(
            returnedConfigurationReport,
            getPersistedConfigurationReport(returnedConfigurationReport)
        );

    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void createConfigurationReportWithExistingId() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);
        ConfigurationReportDTO configurationReportDTO = configurationReportMapper.toDto(configurationReport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        restConfigurationReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configurationReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReports() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        restConfigurationReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationReport.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].footer").value(hasItem(DEFAULT_FOOTER)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getConfigurationReport() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        restConfigurationReportMockMvc
            .perform(get(ENTITY_API_URL_ID, configurationReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configurationReport.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.footer").value(DEFAULT_FOOTER));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getConfigurationReportsByIdFiltering() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        UUID id = configurationReport.getId();

        defaultConfigurationReportFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByNameIsEqualToSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByNameIsInShouldWork() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByNameIsNullOrNotNull() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByNameContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByNameNotContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByAddressIsEqualToSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByAddressIsInShouldWork() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByAddressIsNullOrNotNull() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByAddressContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByAddressNotContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByPostalCodeIsEqualToSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("postalCode.equals=" + DEFAULT_POSTAL_CODE, "postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByPostalCodeIsInShouldWork() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering(
            "postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE,
            "postalCode.in=" + UPDATED_POSTAL_CODE
        );
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByPostalCodeIsNullOrNotNull() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("postalCode.specified=true", "postalCode.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByPostalCodeContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("postalCode.contains=" + DEFAULT_POSTAL_CODE, "postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByPostalCodeNotContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering(
            "postalCode.doesNotContain=" + UPDATED_POSTAL_CODE,
            "postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE
        );
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByPhoneIsEqualToSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByPhoneIsInShouldWork() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByPhoneIsNullOrNotNull() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByPhoneContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByPhoneNotContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByFaxIsEqualToSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("fax.equals=" + DEFAULT_FAX, "fax.equals=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByFaxIsInShouldWork() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("fax.in=" + DEFAULT_FAX + "," + UPDATED_FAX, "fax.in=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByFaxIsNullOrNotNull() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("fax.specified=true", "fax.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByFaxContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("fax.contains=" + DEFAULT_FAX, "fax.contains=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByFaxNotContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("fax.doesNotContain=" + UPDATED_FAX, "fax.doesNotContain=" + DEFAULT_FAX);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByEmailIsEqualToSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByEmailIsInShouldWork() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByEmailIsNullOrNotNull() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByEmailContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByEmailNotContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByLogoIsEqualToSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("logo.equals=" + DEFAULT_LOGO, "logo.equals=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByLogoIsInShouldWork() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("logo.in=" + DEFAULT_LOGO + "," + UPDATED_LOGO, "logo.in=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByLogoIsNullOrNotNull() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("logo.specified=true", "logo.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByLogoContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("logo.contains=" + DEFAULT_LOGO, "logo.contains=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByLogoNotContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("logo.doesNotContain=" + UPDATED_LOGO, "logo.doesNotContain=" + DEFAULT_LOGO);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByFooterIsEqualToSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("footer.equals=" + DEFAULT_FOOTER, "footer.equals=" + UPDATED_FOOTER);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByFooterIsInShouldWork() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("footer.in=" + DEFAULT_FOOTER + "," + UPDATED_FOOTER, "footer.in=" + UPDATED_FOOTER);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByFooterIsNullOrNotNull() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("footer.specified=true", "footer.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByFooterContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("footer.contains=" + DEFAULT_FOOTER, "footer.contains=" + UPDATED_FOOTER);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllConfigurationReportsByFooterNotContainsSomething() throws Exception {
        configurationReportRepository.saveAndFlush(configurationReport);

        defaultConfigurationReportFiltering("footer.doesNotContain=" + UPDATED_FOOTER, "footer.doesNotContain=" + DEFAULT_FOOTER);
    }

    private void defaultConfigurationReportFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultConfigurationReportShouldBeFound(shouldBeFound);
        defaultConfigurationReportShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConfigurationReportShouldBeFound(String filter) throws Exception {

        restConfigurationReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationReport.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].footer").value(hasItem(DEFAULT_FOOTER)));

        // Check, that the count call also returns 1
        restConfigurationReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConfigurationReportShouldNotBeFound(String filter) throws Exception {
        restConfigurationReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        restConfigurationReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getNonExistingConfigurationReport() throws Exception {
        restConfigurationReportMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void putExistingConfigurationReport() throws Exception {
        // Initialize the database
        configurationReportRepository.saveAndFlush(configurationReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configurationReport
        ConfigurationReport updatedConfigurationReport = configurationReportRepository.findById(configurationReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConfigurationReport are not directly saved in db
        em.detach(updatedConfigurationReport);
        updatedConfigurationReport
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .phone(UPDATED_PHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .logo(UPDATED_LOGO)
            .footer(UPDATED_FOOTER);
        ConfigurationReportDTO configurationReportDTO = configurationReportMapper.toDto(updatedConfigurationReport);

        restConfigurationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configurationReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configurationReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConfigurationReportToMatchAllProperties(updatedConfigurationReport);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void putNonExistingConfigurationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configurationReport.setId(UUID.randomUUID());

        // Create the ConfigurationReport
        ConfigurationReportDTO configurationReportDTO = configurationReportMapper.toDto(configurationReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configurationReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configurationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void putWithIdMismatchConfigurationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configurationReport.setId(UUID.randomUUID());

        // Create the ConfigurationReport
        ConfigurationReportDTO configurationReportDTO = configurationReportMapper.toDto(configurationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configurationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void putWithMissingIdPathParamConfigurationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configurationReport.setId(UUID.randomUUID());

        // Create the ConfigurationReport
        ConfigurationReportDTO configurationReportDTO = configurationReportMapper.toDto(configurationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configurationReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigurationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void partialUpdateConfigurationReportWithPatch() throws Exception {
        // Initialize the database
        configurationReportRepository.saveAndFlush(configurationReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configurationReport using partial update
        ConfigurationReport partialUpdatedConfigurationReport = new ConfigurationReport();
        partialUpdatedConfigurationReport.setId(configurationReport.getId());

        partialUpdatedConfigurationReport
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .logo(UPDATED_LOGO);

        restConfigurationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigurationReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfigurationReport))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        ConfigurationReportAsserts.assertConfigurationReportUpdatableFieldsEquals(
            TestUtil.createUpdateProxyForBean(partialUpdatedConfigurationReport, configurationReport),
            getPersistedConfigurationReport(configurationReport)
        );
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void fullUpdateConfigurationReportWithPatch() throws Exception {
        // Initialize the database
        configurationReportRepository.saveAndFlush(configurationReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configurationReport using partial update
        ConfigurationReport partialUpdatedConfigurationReport = new ConfigurationReport();
        partialUpdatedConfigurationReport.setId(configurationReport.getId());

        partialUpdatedConfigurationReport
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .phone(UPDATED_PHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .logo(UPDATED_LOGO)
            .footer(UPDATED_FOOTER);

        restConfigurationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigurationReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfigurationReport))
            )
            .andExpect(status().isOk());

        // Validate the ConfigurationReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        ConfigurationReportAsserts.assertConfigurationReportUpdatableFieldsEquals(
            partialUpdatedConfigurationReport,
            getPersistedConfigurationReport(partialUpdatedConfigurationReport)
        );
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void patchNonExistingConfigurationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configurationReport.setId(UUID.randomUUID());

        // Create the ConfigurationReport
        ConfigurationReportDTO configurationReportDTO = configurationReportMapper.toDto(configurationReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configurationReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configurationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void patchWithIdMismatchConfigurationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configurationReport.setId(UUID.randomUUID());

        // Create the ConfigurationReport
        ConfigurationReportDTO configurationReportDTO = configurationReportMapper.toDto(configurationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configurationReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void patchWithMissingIdPathParamConfigurationReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configurationReport.setId(UUID.randomUUID());

        // Create the ConfigurationReport
        ConfigurationReportDTO configurationReportDTO = configurationReportMapper.toDto(configurationReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigurationReportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(configurationReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigurationReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void deleteConfigurationReport() throws Exception {
        // Initialize the database
        configurationReportRepository.saveAndFlush(configurationReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the configurationReport
        restConfigurationReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, configurationReport.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

    }

    protected long getRepositoryCount() {
        return configurationReportRepository.count();
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

    protected ConfigurationReport getPersistedConfigurationReport(ConfigurationReport configurationReport) {
        return configurationReportRepository.findById(configurationReport.getId()).orElseThrow();
    }

    protected void assertPersistedConfigurationReportToMatchAllProperties(ConfigurationReport expectedConfigurationReport) {
        ConfigurationReportAsserts.assertConfigurationReportAllPropertiesEquals(
            expectedConfigurationReport,
            getPersistedConfigurationReport(expectedConfigurationReport)
        );
    }

    protected void assertPersistedConfigurationReportToMatchUpdatableProperties(ConfigurationReport expectedConfigurationReport) {
        ConfigurationReportAsserts.assertConfigurationReportAllUpdatablePropertiesEquals(
            expectedConfigurationReport,
            getPersistedConfigurationReport(expectedConfigurationReport)
        );
    }
}
