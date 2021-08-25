package com.gsh.hris.service;

import com.gsh.hris.service.dto.LeaveDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.gsh.hris.domain.Leave}.
 */
public interface LeaveService {
    /**
     * Save a leave.
     *
     * @param leaveDTO the entity to save.
     * @return the persisted entity.
     */
    LeaveDTO save(LeaveDTO leaveDTO);

    /**
     * Partially updates a leave.
     *
     * @param leaveDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeaveDTO> partialUpdate(LeaveDTO leaveDTO);

    /**
     * Get all the leaves.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaveDTO> findAll(Pageable pageable);

    /**
     * Get the "id" leave.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaveDTO> findOne(Long id);

    /**
     * Delete the "id" leave.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
