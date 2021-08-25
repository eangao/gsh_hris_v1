package com.gsh.hris.web.rest;

import com.gsh.hris.IntegrationTest;
import com.gsh.hris.domain.DailyTimeRecord;
import com.gsh.hris.domain.Employee;
import com.gsh.hris.repository.DailyTimeRecordRepository;
import com.gsh.hris.service.dto.DailyTimeRecordDTO;
import com.gsh.hris.service.mapper.DailyTimeRecordMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DailyTimeRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DailyTimeRecordResourceIT {

    private static final Instant DEFAULT_DATE_TIME_IN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME_IN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_TIME_OUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME_OUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/daily-time-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DailyTimeRecordRepository dailyTimeRecordRepository;

    @Autowired
    private DailyTimeRecordMapper dailyTimeRecordMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDailyTimeRecordMockMvc;

    private DailyTimeRecord dailyTimeRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyTimeRecord createEntity(EntityManager em) {
        DailyTimeRecord dailyTimeRecord = new DailyTimeRecord().dateTimeIn(DEFAULT_DATE_TIME_IN).dateTimeOut(DEFAULT_DATE_TIME_OUT);
        return dailyTimeRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyTimeRecord createUpdatedEntity(EntityManager em) {
        DailyTimeRecord dailyTimeRecord = new DailyTimeRecord().dateTimeIn(UPDATED_DATE_TIME_IN).dateTimeOut(UPDATED_DATE_TIME_OUT);
        return dailyTimeRecord;
    }

    @BeforeEach
    public void initTest() {
        dailyTimeRecord = createEntity(em);
    }

    @Test
    @Transactional
    void createDailyTimeRecord() throws Exception {
        int databaseSizeBeforeCreate = dailyTimeRecordRepository.findAll().size();
        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);
        restDailyTimeRecordMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeCreate + 1);
        DailyTimeRecord testDailyTimeRecord = dailyTimeRecordList.get(dailyTimeRecordList.size() - 1);
        assertThat(testDailyTimeRecord.getDateTimeIn()).isEqualTo(DEFAULT_DATE_TIME_IN);
        assertThat(testDailyTimeRecord.getDateTimeOut()).isEqualTo(DEFAULT_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void createDailyTimeRecordWithExistingId() throws Exception {
        // Create the DailyTimeRecord with an existing ID
        dailyTimeRecord.setId(1L);
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        int databaseSizeBeforeCreate = dailyTimeRecordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDailyTimeRecordMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecords() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyTimeRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTimeIn").value(hasItem(DEFAULT_DATE_TIME_IN.toString())))
            .andExpect(jsonPath("$.[*].dateTimeOut").value(hasItem(DEFAULT_DATE_TIME_OUT.toString())));
    }

    @Test
    @Transactional
    void getDailyTimeRecord() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get the dailyTimeRecord
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, dailyTimeRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dailyTimeRecord.getId().intValue()))
            .andExpect(jsonPath("$.dateTimeIn").value(DEFAULT_DATE_TIME_IN.toString()))
            .andExpect(jsonPath("$.dateTimeOut").value(DEFAULT_DATE_TIME_OUT.toString()));
    }

    @Test
    @Transactional
    void getDailyTimeRecordsByIdFiltering() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        Long id = dailyTimeRecord.getId();

        defaultDailyTimeRecordShouldBeFound("id.equals=" + id);
        defaultDailyTimeRecordShouldNotBeFound("id.notEquals=" + id);

        defaultDailyTimeRecordShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDailyTimeRecordShouldNotBeFound("id.greaterThan=" + id);

        defaultDailyTimeRecordShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDailyTimeRecordShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByDateTimeInIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where dateTimeIn equals to DEFAULT_DATE_TIME_IN
        defaultDailyTimeRecordShouldBeFound("dateTimeIn.equals=" + DEFAULT_DATE_TIME_IN);

        // Get all the dailyTimeRecordList where dateTimeIn equals to UPDATED_DATE_TIME_IN
        defaultDailyTimeRecordShouldNotBeFound("dateTimeIn.equals=" + UPDATED_DATE_TIME_IN);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByDateTimeInIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where dateTimeIn not equals to DEFAULT_DATE_TIME_IN
        defaultDailyTimeRecordShouldNotBeFound("dateTimeIn.notEquals=" + DEFAULT_DATE_TIME_IN);

        // Get all the dailyTimeRecordList where dateTimeIn not equals to UPDATED_DATE_TIME_IN
        defaultDailyTimeRecordShouldBeFound("dateTimeIn.notEquals=" + UPDATED_DATE_TIME_IN);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByDateTimeInIsInShouldWork() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where dateTimeIn in DEFAULT_DATE_TIME_IN or UPDATED_DATE_TIME_IN
        defaultDailyTimeRecordShouldBeFound("dateTimeIn.in=" + DEFAULT_DATE_TIME_IN + "," + UPDATED_DATE_TIME_IN);

        // Get all the dailyTimeRecordList where dateTimeIn equals to UPDATED_DATE_TIME_IN
        defaultDailyTimeRecordShouldNotBeFound("dateTimeIn.in=" + UPDATED_DATE_TIME_IN);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByDateTimeInIsNullOrNotNull() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where dateTimeIn is not null
        defaultDailyTimeRecordShouldBeFound("dateTimeIn.specified=true");

        // Get all the dailyTimeRecordList where dateTimeIn is null
        defaultDailyTimeRecordShouldNotBeFound("dateTimeIn.specified=false");
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByDateTimeOutIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where dateTimeOut equals to DEFAULT_DATE_TIME_OUT
        defaultDailyTimeRecordShouldBeFound("dateTimeOut.equals=" + DEFAULT_DATE_TIME_OUT);

        // Get all the dailyTimeRecordList where dateTimeOut equals to UPDATED_DATE_TIME_OUT
        defaultDailyTimeRecordShouldNotBeFound("dateTimeOut.equals=" + UPDATED_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByDateTimeOutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where dateTimeOut not equals to DEFAULT_DATE_TIME_OUT
        defaultDailyTimeRecordShouldNotBeFound("dateTimeOut.notEquals=" + DEFAULT_DATE_TIME_OUT);

        // Get all the dailyTimeRecordList where dateTimeOut not equals to UPDATED_DATE_TIME_OUT
        defaultDailyTimeRecordShouldBeFound("dateTimeOut.notEquals=" + UPDATED_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByDateTimeOutIsInShouldWork() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where dateTimeOut in DEFAULT_DATE_TIME_OUT or UPDATED_DATE_TIME_OUT
        defaultDailyTimeRecordShouldBeFound("dateTimeOut.in=" + DEFAULT_DATE_TIME_OUT + "," + UPDATED_DATE_TIME_OUT);

        // Get all the dailyTimeRecordList where dateTimeOut equals to UPDATED_DATE_TIME_OUT
        defaultDailyTimeRecordShouldNotBeFound("dateTimeOut.in=" + UPDATED_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByDateTimeOutIsNullOrNotNull() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where dateTimeOut is not null
        defaultDailyTimeRecordShouldBeFound("dateTimeOut.specified=true");

        // Get all the dailyTimeRecordList where dateTimeOut is null
        defaultDailyTimeRecordShouldNotBeFound("dateTimeOut.specified=false");
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);
        Employee employee = EmployeeResourceIT.createEntity(em);
        em.persist(employee);
        em.flush();
        dailyTimeRecord.setEmployee(employee);
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);
        Long employeeId = employee.getId();

        // Get all the dailyTimeRecordList where employee equals to employeeId
        defaultDailyTimeRecordShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the dailyTimeRecordList where employee equals to (employeeId + 1)
        defaultDailyTimeRecordShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDailyTimeRecordShouldBeFound(String filter) throws Exception {
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyTimeRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTimeIn").value(hasItem(DEFAULT_DATE_TIME_IN.toString())))
            .andExpect(jsonPath("$.[*].dateTimeOut").value(hasItem(DEFAULT_DATE_TIME_OUT.toString())));

        // Check, that the count call also returns 1
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDailyTimeRecordShouldNotBeFound(String filter) throws Exception {
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDailyTimeRecord() throws Exception {
        // Get the dailyTimeRecord
        restDailyTimeRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDailyTimeRecord() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();

        // Update the dailyTimeRecord
        DailyTimeRecord updatedDailyTimeRecord = dailyTimeRecordRepository.findById(dailyTimeRecord.getId()).get();
        // Disconnect from session so that the updates on updatedDailyTimeRecord are not directly saved in db
        em.detach(updatedDailyTimeRecord);
        updatedDailyTimeRecord.dateTimeIn(UPDATED_DATE_TIME_IN).dateTimeOut(UPDATED_DATE_TIME_OUT);
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(updatedDailyTimeRecord);

        restDailyTimeRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dailyTimeRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
        DailyTimeRecord testDailyTimeRecord = dailyTimeRecordList.get(dailyTimeRecordList.size() - 1);
        assertThat(testDailyTimeRecord.getDateTimeIn()).isEqualTo(UPDATED_DATE_TIME_IN);
        assertThat(testDailyTimeRecord.getDateTimeOut()).isEqualTo(UPDATED_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void putNonExistingDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dailyTimeRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDailyTimeRecordWithPatch() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();

        // Update the dailyTimeRecord using partial update
        DailyTimeRecord partialUpdatedDailyTimeRecord = new DailyTimeRecord();
        partialUpdatedDailyTimeRecord.setId(dailyTimeRecord.getId());

        partialUpdatedDailyTimeRecord.dateTimeIn(UPDATED_DATE_TIME_IN).dateTimeOut(UPDATED_DATE_TIME_OUT);

        restDailyTimeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDailyTimeRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDailyTimeRecord))
            )
            .andExpect(status().isOk());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
        DailyTimeRecord testDailyTimeRecord = dailyTimeRecordList.get(dailyTimeRecordList.size() - 1);
        assertThat(testDailyTimeRecord.getDateTimeIn()).isEqualTo(UPDATED_DATE_TIME_IN);
        assertThat(testDailyTimeRecord.getDateTimeOut()).isEqualTo(UPDATED_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void fullUpdateDailyTimeRecordWithPatch() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();

        // Update the dailyTimeRecord using partial update
        DailyTimeRecord partialUpdatedDailyTimeRecord = new DailyTimeRecord();
        partialUpdatedDailyTimeRecord.setId(dailyTimeRecord.getId());

        partialUpdatedDailyTimeRecord.dateTimeIn(UPDATED_DATE_TIME_IN).dateTimeOut(UPDATED_DATE_TIME_OUT);

        restDailyTimeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDailyTimeRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDailyTimeRecord))
            )
            .andExpect(status().isOk());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
        DailyTimeRecord testDailyTimeRecord = dailyTimeRecordList.get(dailyTimeRecordList.size() - 1);
        assertThat(testDailyTimeRecord.getDateTimeIn()).isEqualTo(UPDATED_DATE_TIME_IN);
        assertThat(testDailyTimeRecord.getDateTimeOut()).isEqualTo(UPDATED_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void patchNonExistingDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dailyTimeRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDailyTimeRecord() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        int databaseSizeBeforeDelete = dailyTimeRecordRepository.findAll().size();

        // Delete the dailyTimeRecord
        restDailyTimeRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, dailyTimeRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
