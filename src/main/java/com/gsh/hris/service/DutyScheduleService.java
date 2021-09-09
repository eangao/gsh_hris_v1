package com.gsh.hris.service;

import com.gsh.hris.service.dto.DutyScheduleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.gsh.hris.domain.DutySchedule}.
 */
public interface DutyScheduleService {
    /**
     * Save a dutySchedule.
     *
     * @param dutyScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    DutyScheduleDTO save(DutyScheduleDTO dutyScheduleDTO);

    /**
     * Partially updates a dutySchedule.
     *
     * @param dutyScheduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DutyScheduleDTO> partialUpdate(DutyScheduleDTO dutyScheduleDTO);

    /**
     * Get all the dutySchedules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DutyScheduleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dutySchedule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DutyScheduleDTO> findOne(Long id);

    /**
     * Delete the "id" dutySchedule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
