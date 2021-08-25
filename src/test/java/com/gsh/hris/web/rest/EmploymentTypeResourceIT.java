package com.gsh.hris.web.rest;

import com.gsh.hris.IntegrationTest;
import com.gsh.hris.domain.Employee;
import com.gsh.hris.domain.EmploymentType;
import com.gsh.hris.repository.EmploymentTypeRepository;
import com.gsh.hris.service.dto.EmploymentTypeDTO;
import com.gsh.hris.service.mapper.EmploymentTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EmploymentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmploymentTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employment-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmploymentTypeRepository employmentTypeRepository;

    @Autowired
    private EmploymentTypeMapper employmentTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmploymentTypeMockMvc;

    private EmploymentType employmentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentType createEntity(EntityManager em) {
        EmploymentType employmentType = new EmploymentType().name(DEFAULT_NAME);
        return employmentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentType createUpdatedEntity(EntityManager em) {
        EmploymentType employmentType = new EmploymentType().name(UPDATED_NAME);
        return employmentType;
    }

    @BeforeEach
    public void initTest() {
        employmentType = createEntity(em);
    }

    @Test
    @Transactional
    void createEmploymentType() throws Exception {
        int databaseSizeBeforeCreate = employmentTypeRepository.findAll().size();
        // Create the EmploymentType
        EmploymentTypeDTO employmentTypeDTO = employmentTypeMapper.toDto(employmentType);
        restEmploymentTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EmploymentType testEmploymentType = employmentTypeList.get(employmentTypeList.size() - 1);
        assertThat(testEmploymentType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createEmploymentTypeWithExistingId() throws Exception {
        // Create the EmploymentType with an existing ID
        employmentType.setId(1L);
        EmploymentTypeDTO employmentTypeDTO = employmentTypeMapper.toDto(employmentType);

        int databaseSizeBeforeCreate = employmentTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmploymentTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmploymentTypes() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get all the employmentTypeList
        restEmploymentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getEmploymentType() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get the employmentType
        restEmploymentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, employmentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employmentType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getEmploymentTypesByIdFiltering() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        Long id = employmentType.getId();

        defaultEmploymentTypeShouldBeFound("id.equals=" + id);
        defaultEmploymentTypeShouldNotBeFound("id.notEquals=" + id);

        defaultEmploymentTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmploymentTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmploymentTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmploymentTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmploymentTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get all the employmentTypeList where name equals to DEFAULT_NAME
        defaultEmploymentTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the employmentTypeList where name equals to UPDATED_NAME
        defaultEmploymentTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmploymentTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get all the employmentTypeList where name not equals to DEFAULT_NAME
        defaultEmploymentTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the employmentTypeList where name not equals to UPDATED_NAME
        defaultEmploymentTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmploymentTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get all the employmentTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEmploymentTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the employmentTypeList where name equals to UPDATED_NAME
        defaultEmploymentTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmploymentTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get all the employmentTypeList where name is not null
        defaultEmploymentTypeShouldBeFound("name.specified=true");

        // Get all the employmentTypeList where name is null
        defaultEmploymentTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEmploymentTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get all the employmentTypeList where name contains DEFAULT_NAME
        defaultEmploymentTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the employmentTypeList where name contains UPDATED_NAME
        defaultEmploymentTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmploymentTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get all the employmentTypeList where name does not contain DEFAULT_NAME
        defaultEmploymentTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the employmentTypeList where name does not contain UPDATED_NAME
        defaultEmploymentTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmploymentTypesByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);
        Employee employee = EmployeeResourceIT.createEntity(em);
        em.persist(employee);
        em.flush();
        employmentType.addEmployee(employee);
        employmentTypeRepository.saveAndFlush(employmentType);
        Long employeeId = employee.getId();

        // Get all the employmentTypeList where employee equals to employeeId
        defaultEmploymentTypeShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the employmentTypeList where employee equals to (employeeId + 1)
        defaultEmploymentTypeShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmploymentTypeShouldBeFound(String filter) throws Exception {
        restEmploymentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restEmploymentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmploymentTypeShouldNotBeFound(String filter) throws Exception {
        restEmploymentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmploymentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmploymentType() throws Exception {
        // Get the employmentType
        restEmploymentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmploymentType() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();

        // Update the employmentType
        EmploymentType updatedEmploymentType = employmentTypeRepository.findById(employmentType.getId()).get();
        // Disconnect from session so that the updates on updatedEmploymentType are not directly saved in db
        em.detach(updatedEmploymentType);
        updatedEmploymentType.name(UPDATED_NAME);
        EmploymentTypeDTO employmentTypeDTO = employmentTypeMapper.toDto(updatedEmploymentType);

        restEmploymentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
        EmploymentType testEmploymentType = employmentTypeList.get(employmentTypeList.size() - 1);
        assertThat(testEmploymentType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // Create the EmploymentType
        EmploymentTypeDTO employmentTypeDTO = employmentTypeMapper.toDto(employmentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // Create the EmploymentType
        EmploymentTypeDTO employmentTypeDTO = employmentTypeMapper.toDto(employmentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // Create the EmploymentType
        EmploymentTypeDTO employmentTypeDTO = employmentTypeMapper.toDto(employmentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmploymentTypeWithPatch() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();

        // Update the employmentType using partial update
        EmploymentType partialUpdatedEmploymentType = new EmploymentType();
        partialUpdatedEmploymentType.setId(employmentType.getId());

        restEmploymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentType))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
        EmploymentType testEmploymentType = employmentTypeList.get(employmentTypeList.size() - 1);
        assertThat(testEmploymentType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateEmploymentTypeWithPatch() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();

        // Update the employmentType using partial update
        EmploymentType partialUpdatedEmploymentType = new EmploymentType();
        partialUpdatedEmploymentType.setId(employmentType.getId());

        partialUpdatedEmploymentType.name(UPDATED_NAME);

        restEmploymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentType))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
        EmploymentType testEmploymentType = employmentTypeList.get(employmentTypeList.size() - 1);
        assertThat(testEmploymentType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // Create the EmploymentType
        EmploymentTypeDTO employmentTypeDTO = employmentTypeMapper.toDto(employmentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employmentTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // Create the EmploymentType
        EmploymentTypeDTO employmentTypeDTO = employmentTypeMapper.toDto(employmentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // Create the EmploymentType
        EmploymentTypeDTO employmentTypeDTO = employmentTypeMapper.toDto(employmentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmploymentType() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        int databaseSizeBeforeDelete = employmentTypeRepository.findAll().size();

        // Delete the employmentType
        restEmploymentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employmentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
