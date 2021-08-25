package com.gsh.hris.web.rest;

import com.gsh.hris.IntegrationTest;
import com.gsh.hris.domain.Holiday;
import com.gsh.hris.repository.HolidayRepository;
import com.gsh.hris.service.dto.HolidayDTO;
import com.gsh.hris.service.mapper.HolidayMapper;
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
 * Integration tests for the {@link HolidayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HolidayResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/holidays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private HolidayMapper holidayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHolidayMockMvc;

    private Holiday holiday;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Holiday createEntity(EntityManager em) {
        Holiday holiday = new Holiday().name(DEFAULT_NAME).date(DEFAULT_DATE);
        return holiday;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Holiday createUpdatedEntity(EntityManager em) {
        Holiday holiday = new Holiday().name(UPDATED_NAME).date(UPDATED_DATE);
        return holiday;
    }

    @BeforeEach
    public void initTest() {
        holiday = createEntity(em);
    }

    @Test
    @Transactional
    void createHoliday() throws Exception {
        int databaseSizeBeforeCreate = holidayRepository.findAll().size();
        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);
        restHolidayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
            .andExpect(status().isCreated());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeCreate + 1);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHoliday.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createHolidayWithExistingId() throws Exception {
        // Create the Holiday with an existing ID
        holiday.setId(1L);
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        int databaseSizeBeforeCreate = holidayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHolidayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHolidays() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList
        restHolidayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holiday.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get the holiday
        restHolidayMockMvc
            .perform(get(ENTITY_API_URL_ID, holiday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(holiday.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getHolidaysByIdFiltering() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        Long id = holiday.getId();

        defaultHolidayShouldBeFound("id.equals=" + id);
        defaultHolidayShouldNotBeFound("id.notEquals=" + id);

        defaultHolidayShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHolidayShouldNotBeFound("id.greaterThan=" + id);

        defaultHolidayShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHolidayShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHolidaysByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where name equals to DEFAULT_NAME
        defaultHolidayShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the holidayList where name equals to UPDATED_NAME
        defaultHolidayShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHolidaysByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where name not equals to DEFAULT_NAME
        defaultHolidayShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the holidayList where name not equals to UPDATED_NAME
        defaultHolidayShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHolidaysByNameIsInShouldWork() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHolidayShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the holidayList where name equals to UPDATED_NAME
        defaultHolidayShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHolidaysByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where name is not null
        defaultHolidayShouldBeFound("name.specified=true");

        // Get all the holidayList where name is null
        defaultHolidayShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllHolidaysByNameContainsSomething() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where name contains DEFAULT_NAME
        defaultHolidayShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the holidayList where name contains UPDATED_NAME
        defaultHolidayShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHolidaysByNameNotContainsSomething() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where name does not contain DEFAULT_NAME
        defaultHolidayShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the holidayList where name does not contain UPDATED_NAME
        defaultHolidayShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHolidaysByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where date equals to DEFAULT_DATE
        defaultHolidayShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the holidayList where date equals to UPDATED_DATE
        defaultHolidayShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllHolidaysByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where date not equals to DEFAULT_DATE
        defaultHolidayShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the holidayList where date not equals to UPDATED_DATE
        defaultHolidayShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllHolidaysByDateIsInShouldWork() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where date in DEFAULT_DATE or UPDATED_DATE
        defaultHolidayShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the holidayList where date equals to UPDATED_DATE
        defaultHolidayShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllHolidaysByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where date is not null
        defaultHolidayShouldBeFound("date.specified=true");

        // Get all the holidayList where date is null
        defaultHolidayShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllHolidaysByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where date is greater than or equal to DEFAULT_DATE
        defaultHolidayShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the holidayList where date is greater than or equal to UPDATED_DATE
        defaultHolidayShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllHolidaysByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where date is less than or equal to DEFAULT_DATE
        defaultHolidayShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the holidayList where date is less than or equal to SMALLER_DATE
        defaultHolidayShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllHolidaysByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where date is less than DEFAULT_DATE
        defaultHolidayShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the holidayList where date is less than UPDATED_DATE
        defaultHolidayShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllHolidaysByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList where date is greater than DEFAULT_DATE
        defaultHolidayShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the holidayList where date is greater than SMALLER_DATE
        defaultHolidayShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHolidayShouldBeFound(String filter) throws Exception {
        restHolidayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holiday.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restHolidayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHolidayShouldNotBeFound(String filter) throws Exception {
        restHolidayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHolidayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHoliday() throws Exception {
        // Get the holiday
        restHolidayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday
        Holiday updatedHoliday = holidayRepository.findById(holiday.getId()).get();
        // Disconnect from session so that the updates on updatedHoliday are not directly saved in db
        em.detach(updatedHoliday);
        updatedHoliday.name(UPDATED_NAME).date(UPDATED_DATE);
        HolidayDTO holidayDTO = holidayMapper.toDto(updatedHoliday);

        restHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holidayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHoliday.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holidayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHolidayWithPatch() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday using partial update
        Holiday partialUpdatedHoliday = new Holiday();
        partialUpdatedHoliday.setId(holiday.getId());

        partialUpdatedHoliday.name(UPDATED_NAME);

        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoliday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoliday))
            )
            .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHoliday.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateHolidayWithPatch() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday using partial update
        Holiday partialUpdatedHoliday = new Holiday();
        partialUpdatedHoliday.setId(holiday.getId());

        partialUpdatedHoliday.name(UPDATED_NAME).date(UPDATED_DATE);

        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoliday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoliday))
            )
            .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHoliday.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, holidayDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeDelete = holidayRepository.findAll().size();

        // Delete the holiday
        restHolidayMockMvc
            .perform(delete(ENTITY_API_URL_ID, holiday.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
