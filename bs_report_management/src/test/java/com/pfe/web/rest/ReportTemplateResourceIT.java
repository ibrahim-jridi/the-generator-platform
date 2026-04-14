package com.pfe.web.rest;

import static com.pfe.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.IntegrationTest;
import com.pfe.domain.ReportTemplate;
import com.pfe.repository.ReportTemplateRepository;
import com.pfe.service.dto.ReportTemplateDTO;
import com.pfe.service.mapper.ReportTemplateMapper;
import com.pfe.domain.ReportTemplateAsserts;
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
 * Integration tests for the {@link ReportTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
class ReportTemplateResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/v1/report-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportTemplateRepository reportTemplateRepository;

    @Autowired
    private ReportTemplateMapper reportTemplateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportTemplateMockMvc;

    private ReportTemplate reportTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportTemplate createEntity(EntityManager em) {
        ReportTemplate reportTemplate = new ReportTemplate().type(DEFAULT_TYPE).path(DEFAULT_PATH);
        return reportTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportTemplate createUpdatedEntity(EntityManager em) {
        ReportTemplate reportTemplate = new ReportTemplate().type(UPDATED_TYPE).path(UPDATED_PATH);
        return reportTemplate;
    }

    @BeforeEach
    public void initTest() {
        reportTemplate = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void createReportTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);
        var returnedReportTemplateDTO = om.readValue(
            restReportTemplateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportTemplateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportTemplateDTO.class
        );

        // Validate the ReportTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportTemplate = reportTemplateMapper.toEntity(returnedReportTemplateDTO);
        ReportTemplateAsserts.assertReportTemplateUpdatableFieldsEquals(returnedReportTemplate, getPersistedReportTemplate(returnedReportTemplate));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void createReportTemplateWithExistingId() throws Exception {
        // Create the ReportTemplate with an existing ID
        reportTemplateRepository.saveAndFlush(reportTemplate);
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllReportTemplates() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportTemplate.getId().toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getReportTemplate() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get the reportTemplate
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, reportTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportTemplate.getId().toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getReportTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        UUID id = reportTemplate.getId();

        defaultReportTemplateFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllReportTemplatesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where type equals to
        defaultReportTemplateFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllReportTemplatesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where type in
        defaultReportTemplateFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllReportTemplatesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where type is not null
        defaultReportTemplateFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllReportTemplatesByTypeContainsSomething() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where type contains
        defaultReportTemplateFiltering("type.contains=" + DEFAULT_TYPE, "type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllReportTemplatesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where type does not contain
        defaultReportTemplateFiltering("type.doesNotContain=" + UPDATED_TYPE, "type.doesNotContain=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllReportTemplatesByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where path equals to
        defaultReportTemplateFiltering("path.equals=" + DEFAULT_PATH, "path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllReportTemplatesByPathIsInShouldWork() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where path in
        defaultReportTemplateFiltering("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH, "path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllReportTemplatesByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where path is not null
        defaultReportTemplateFiltering("path.specified=true", "path.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllReportTemplatesByPathContainsSomething() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where path contains
        defaultReportTemplateFiltering("path.contains=" + DEFAULT_PATH, "path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllReportTemplatesByPathNotContainsSomething() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        // Get all the reportTemplateList where path does not contain
        defaultReportTemplateFiltering("path.doesNotContain=" + UPDATED_PATH, "path.doesNotContain=" + DEFAULT_PATH);
    }

    private void defaultReportTemplateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReportTemplateShouldBeFound(shouldBeFound);
        defaultReportTemplateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportTemplateShouldBeFound(String filter) throws Exception {
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportTemplate.getId().toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)));

        // Check, that the count call also returns 1
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportTemplateShouldNotBeFound(String filter) throws Exception {
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getNonExistingReportTemplate() throws Exception {
        // Get the reportTemplate
        restReportTemplateMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void putExistingReportTemplate() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportTemplate
        ReportTemplate updatedReportTemplate = reportTemplateRepository.findById(reportTemplate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportTemplate are not directly saved in db
        em.detach(updatedReportTemplate);
        updatedReportTemplate.type(UPDATED_TYPE).path(UPDATED_PATH);
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(updatedReportTemplate);

        restReportTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportTemplateToMatchAllProperties(updatedReportTemplate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void putNonExistingReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(UUID.randomUUID());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void putWithIdMismatchReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(UUID.randomUUID());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void putWithMissingIdPathParamReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(UUID.randomUUID());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void partialUpdateReportTemplateWithPatch() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportTemplate using partial update
        ReportTemplate partialUpdatedReportTemplate = new ReportTemplate();
        partialUpdatedReportTemplate.setId(reportTemplate.getId());

        partialUpdatedReportTemplate.type(UPDATED_TYPE);

        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportTemplate))
            )
            .andExpect(status().isOk());

        // Validate the ReportTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        ReportTemplateAsserts.assertReportTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportTemplate, reportTemplate),
            getPersistedReportTemplate(reportTemplate)
        );
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void fullUpdateReportTemplateWithPatch() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportTemplate using partial update
        ReportTemplate partialUpdatedReportTemplate = new ReportTemplate();
        partialUpdatedReportTemplate.setId(reportTemplate.getId());

        partialUpdatedReportTemplate.type(UPDATED_TYPE).path(UPDATED_PATH);

        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportTemplate))
            )
            .andExpect(status().isOk());

        // Validate the ReportTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        ReportTemplateAsserts.assertReportTemplateUpdatableFieldsEquals(partialUpdatedReportTemplate, getPersistedReportTemplate(partialUpdatedReportTemplate));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void patchNonExistingReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(UUID.randomUUID());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportTemplateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void patchWithIdMismatchReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(UUID.randomUUID());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void patchWithMissingIdPathParamReportTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportTemplate.setId(UUID.randomUUID());

        // Create the ReportTemplate
        ReportTemplateDTO reportTemplateDTO = reportTemplateMapper.toDto(reportTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void deleteReportTemplate() throws Exception {
        // Initialize the database
        reportTemplateRepository.saveAndFlush(reportTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportTemplate
        restReportTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportTemplate.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

    }

    protected long getRepositoryCount() {
        return reportTemplateRepository.count();
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

    protected ReportTemplate getPersistedReportTemplate(ReportTemplate reportTemplate) {
        return reportTemplateRepository.findById(reportTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedReportTemplateToMatchAllProperties(ReportTemplate expectedReportTemplate) {
        ReportTemplateAsserts.assertReportTemplateAllPropertiesEquals(expectedReportTemplate, getPersistedReportTemplate(expectedReportTemplate));
    }

    protected void assertPersistedReportTemplateToMatchUpdatableProperties(ReportTemplate expectedReportTemplate) {
        ReportTemplateAsserts.assertReportTemplateAllUpdatablePropertiesEquals(expectedReportTemplate, getPersistedReportTemplate(expectedReportTemplate));
    }
}
