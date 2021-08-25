package com.gsh.hris.web.rest;

import com.gsh.hris.IntegrationTest;
import com.gsh.hris.domain.Employee;
import com.gsh.hris.domain.TrainingHistory;
import com.gsh.hris.repository.TrainingHistoryRepository;
import com.gsh.hris.service.dto.TrainingHistoryDTO;
import com.gsh.hris.service.mapper.TrainingHistoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TrainingHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrainingHistoryResourceIT {

    private static final String DEFAULT_TRAINING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRAINING_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRAINING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRAINING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRAINING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/training-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrainingHistoryRepository trainingHistoryRepository;

    @Autowired
    private TrainingHistoryMapper trainingHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainingHistoryMockMvc;

    private TrainingHistory trainingHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingHistory createEntity(EntityManager em) {
        TrainingHistory trainingHistory = new TrainingHistory().trainingName(DEFAULT_TRAINING_NAME).trainingDate(DEFAULT_TRAINING_DATE);
        return trainingHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingHistory createUpdatedEntity(EntityManager em) {
        TrainingHistory trainingHistory = new TrainingHistory().trainingName(UPDATED_TRAINING_NAME).trainingDate(UPDATED_TRAINING_DATE);
        return trainingHistory;
    }

    @BeforeEach
    public void initTest() {
        trainingHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createTrainingHistory() throws Exception {
        int databaseSizeBeforeCreate = trainingHistoryRepository.findAll().size();
        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);
        restTrainingHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getTrainingName()).isEqualTo(DEFAULT_TRAINING_NAME);
        assertThat(testTrainingHistory.getTrainingDate()).isEqualTo(DEFAULT_TRAINING_DATE);
    }

    @Test
    @Transactional
    void createTrainingHistoryWithExistingId() throws Exception {
        // Create the TrainingHistory with an existing ID
        trainingHistory.setId(1L);
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        int databaseSizeBeforeCreate = trainingHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrainingHistories() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainingName").value(hasItem(DEFAULT_TRAINING_NAME)))
            .andExpect(jsonPath("$.[*].trainingDate").value(hasItem(DEFAULT_TRAINING_DATE.toString())));
    }

    @Test
    @Transactional
    void getTrainingHistory() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get the trainingHistory
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, trainingHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trainingHistory.getId().intValue()))
            .andExpect(jsonPath("$.trainingName").value(DEFAULT_TRAINING_NAME))
            .andExpect(jsonPath("$.trainingDate").value(DEFAULT_TRAINING_DATE.toString()));
    }

    @Test
    @Transactional
    void getTrainingHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        Long id = trainingHistory.getId();

        defaultTrainingHistoryShouldBeFound("id.equals=" + id);
        defaultTrainingHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultTrainingHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTrainingHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultTrainingHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTrainingHistoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingName equals to DEFAULT_TRAINING_NAME
        defaultTrainingHistoryShouldBeFound("trainingName.equals=" + DEFAULT_TRAINING_NAME);

        // Get all the trainingHistoryList where trainingName equals to UPDATED_TRAINING_NAME
        defaultTrainingHistoryShouldNotBeFound("trainingName.equals=" + UPDATED_TRAINING_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingName not equals to DEFAULT_TRAINING_NAME
        defaultTrainingHistoryShouldNotBeFound("trainingName.notEquals=" + DEFAULT_TRAINING_NAME);

        // Get all the trainingHistoryList where trainingName not equals to UPDATED_TRAINING_NAME
        defaultTrainingHistoryShouldBeFound("trainingName.notEquals=" + UPDATED_TRAINING_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingNameIsInShouldWork() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingName in DEFAULT_TRAINING_NAME or UPDATED_TRAINING_NAME
        defaultTrainingHistoryShouldBeFound("trainingName.in=" + DEFAULT_TRAINING_NAME + "," + UPDATED_TRAINING_NAME);

        // Get all the trainingHistoryList where trainingName equals to UPDATED_TRAINING_NAME
        defaultTrainingHistoryShouldNotBeFound("trainingName.in=" + UPDATED_TRAINING_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingName is not null
        defaultTrainingHistoryShouldBeFound("trainingName.specified=true");

        // Get all the trainingHistoryList where trainingName is null
        defaultTrainingHistoryShouldNotBeFound("trainingName.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingNameContainsSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingName contains DEFAULT_TRAINING_NAME
        defaultTrainingHistoryShouldBeFound("trainingName.contains=" + DEFAULT_TRAINING_NAME);

        // Get all the trainingHistoryList where trainingName contains UPDATED_TRAINING_NAME
        defaultTrainingHistoryShouldNotBeFound("trainingName.contains=" + UPDATED_TRAINING_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingNameNotContainsSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingName does not contain DEFAULT_TRAINING_NAME
        defaultTrainingHistoryShouldNotBeFound("trainingName.doesNotContain=" + DEFAULT_TRAINING_NAME);

        // Get all the trainingHistoryList where trainingName does not contain UPDATED_TRAINING_NAME
        defaultTrainingHistoryShouldBeFound("trainingName.doesNotContain=" + UPDATED_TRAINING_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingDate equals to DEFAULT_TRAINING_DATE
        defaultTrainingHistoryShouldBeFound("trainingDate.equals=" + DEFAULT_TRAINING_DATE);

        // Get all the trainingHistoryList where trainingDate equals to UPDATED_TRAINING_DATE
        defaultTrainingHistoryShouldNotBeFound("trainingDate.equals=" + UPDATED_TRAINING_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingDate not equals to DEFAULT_TRAINING_DATE
        defaultTrainingHistoryShouldNotBeFound("trainingDate.notEquals=" + DEFAULT_TRAINING_DATE);

        // Get all the trainingHistoryList where trainingDate not equals to UPDATED_TRAINING_DATE
        defaultTrainingHistoryShouldBeFound("trainingDate.notEquals=" + UPDATED_TRAINING_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingDateIsInShouldWork() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingDate in DEFAULT_TRAINING_DATE or UPDATED_TRAINING_DATE
        defaultTrainingHistoryShouldBeFound("trainingDate.in=" + DEFAULT_TRAINING_DATE + "," + UPDATED_TRAINING_DATE);

        // Get all the trainingHistoryList where trainingDate equals to UPDATED_TRAINING_DATE
        defaultTrainingHistoryShouldNotBeFound("trainingDate.in=" + UPDATED_TRAINING_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingDate is not null
        defaultTrainingHistoryShouldBeFound("trainingDate.specified=true");

        // Get all the trainingHistoryList where trainingDate is null
        defaultTrainingHistoryShouldNotBeFound("trainingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingDate is greater than or equal to DEFAULT_TRAINING_DATE
        defaultTrainingHistoryShouldBeFound("trainingDate.greaterThanOrEqual=" + DEFAULT_TRAINING_DATE);

        // Get all the trainingHistoryList where trainingDate is greater than or equal to UPDATED_TRAINING_DATE
        defaultTrainingHistoryShouldNotBeFound("trainingDate.greaterThanOrEqual=" + UPDATED_TRAINING_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingDate is less than or equal to DEFAULT_TRAINING_DATE
        defaultTrainingHistoryShouldBeFound("trainingDate.lessThanOrEqual=" + DEFAULT_TRAINING_DATE);

        // Get all the trainingHistoryList where trainingDate is less than or equal to SMALLER_TRAINING_DATE
        defaultTrainingHistoryShouldNotBeFound("trainingDate.lessThanOrEqual=" + SMALLER_TRAINING_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingDate is less than DEFAULT_TRAINING_DATE
        defaultTrainingHistoryShouldNotBeFound("trainingDate.lessThan=" + DEFAULT_TRAINING_DATE);

        // Get all the trainingHistoryList where trainingDate is less than UPDATED_TRAINING_DATE
        defaultTrainingHistoryShouldBeFound("trainingDate.lessThan=" + UPDATED_TRAINING_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByTrainingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where trainingDate is greater than DEFAULT_TRAINING_DATE
        defaultTrainingHistoryShouldNotBeFound("trainingDate.greaterThan=" + DEFAULT_TRAINING_DATE);

        // Get all the trainingHistoryList where trainingDate is greater than SMALLER_TRAINING_DATE
        defaultTrainingHistoryShouldBeFound("trainingDate.greaterThan=" + SMALLER_TRAINING_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);
        Employee employee = EmployeeResourceIT.createEntity(em);
        em.persist(employee);
        em.flush();
        trainingHistory.setEmployee(employee);
        trainingHistoryRepository.saveAndFlush(trainingHistory);
        Long employeeId = employee.getId();

        // Get all the trainingHistoryList where employee equals to employeeId
        defaultTrainingHistoryShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the trainingHistoryList where employee equals to (employeeId + 1)
        defaultTrainingHistoryShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrainingHistoryShouldBeFound(String filter) throws Exception {
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainingName").value(hasItem(DEFAULT_TRAINING_NAME)))
            .andExpect(jsonPath("$.[*].trainingDate").value(hasItem(DEFAULT_TRAINING_DATE.toString())));

        // Check, that the count call also returns 1
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrainingHistoryShouldNotBeFound(String filter) throws Exception {
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrainingHistory() throws Exception {
        // Get the trainingHistory
        restTrainingHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrainingHistory() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();

        // Update the trainingHistory
        TrainingHistory updatedTrainingHistory = trainingHistoryRepository.findById(trainingHistory.getId()).get();
        // Disconnect from session so that the updates on updatedTrainingHistory are not directly saved in db
        em.detach(updatedTrainingHistory);
        updatedTrainingHistory.trainingName(UPDATED_TRAINING_NAME).trainingDate(UPDATED_TRAINING_DATE);
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(updatedTrainingHistory);

        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getTrainingName()).isEqualTo(UPDATED_TRAINING_NAME);
        assertThat(testTrainingHistory.getTrainingDate()).isEqualTo(UPDATED_TRAINING_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainingHistoryWithPatch() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();

        // Update the trainingHistory using partial update
        TrainingHistory partialUpdatedTrainingHistory = new TrainingHistory();
        partialUpdatedTrainingHistory.setId(trainingHistory.getId());

        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingHistory))
            )
            .andExpect(status().isOk());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getTrainingName()).isEqualTo(DEFAULT_TRAINING_NAME);
        assertThat(testTrainingHistory.getTrainingDate()).isEqualTo(DEFAULT_TRAINING_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTrainingHistoryWithPatch() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();

        // Update the trainingHistory using partial update
        TrainingHistory partialUpdatedTrainingHistory = new TrainingHistory();
        partialUpdatedTrainingHistory.setId(trainingHistory.getId());

        partialUpdatedTrainingHistory.trainingName(UPDATED_TRAINING_NAME).trainingDate(UPDATED_TRAINING_DATE);

        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingHistory))
            )
            .andExpect(status().isOk());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getTrainingName()).isEqualTo(UPDATED_TRAINING_NAME);
        assertThat(testTrainingHistory.getTrainingDate()).isEqualTo(UPDATED_TRAINING_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainingHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrainingHistory() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeDelete = trainingHistoryRepository.findAll().size();

        // Delete the trainingHistory
        restTrainingHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, trainingHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
