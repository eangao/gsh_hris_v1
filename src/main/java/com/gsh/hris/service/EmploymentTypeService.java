package com.gsh.hris.service;

import com.gsh.hris.service.dto.EmploymentTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.gsh.hris.domain.EmploymentType}.
 */
public interface EmploymentTypeService {
    /**
     * Save a employmentType.
     *
     * @param employmentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    EmploymentTypeDTO save(EmploymentTypeDTO employmentTypeDTO);

    /**
     * Partially updates a employmentType.
     *
     * @param employmentTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmploymentTypeDTO> partialUpdate(EmploymentTypeDTO employmentTypeDTO);

    /**
     * Get all the employmentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmploymentTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" employmentType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmploymentTypeDTO> findOne(Long id);

    /**
     * Delete the "id" employmentType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
