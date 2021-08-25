package com.gsh.hris.service;

import com.gsh.hris.service.dto.TrainingHistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.gsh.hris.domain.TrainingHistory}.
 */
public interface TrainingHistoryService {
    /**
     * Save a trainingHistory.
     *
     * @param trainingHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    TrainingHistoryDTO save(TrainingHistoryDTO trainingHistoryDTO);

    /**
     * Partially updates a trainingHistory.
     *
     * @param trainingHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrainingHistoryDTO> partialUpdate(TrainingHistoryDTO trainingHistoryDTO);

    /**
     * Get all the trainingHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TrainingHistoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" trainingHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrainingHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" trainingHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
