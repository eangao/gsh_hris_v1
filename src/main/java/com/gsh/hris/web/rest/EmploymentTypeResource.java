package com.gsh.hris.web.rest;

import com.gsh.hris.repository.EmploymentTypeRepository;
import com.gsh.hris.service.EmploymentTypeQueryService;
import com.gsh.hris.service.EmploymentTypeService;
import com.gsh.hris.service.criteria.EmploymentTypeCriteria;
import com.gsh.hris.service.dto.EmploymentTypeDTO;
import com.gsh.hris.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gsh.hris.domain.EmploymentType}.
 */
@RestController
@RequestMapping("/api")
public class EmploymentTypeResource {

    private final Logger log = LoggerFactory.getLogger(EmploymentTypeResource.class);

    private static final String ENTITY_NAME = "employmentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmploymentTypeService employmentTypeService;

    private final EmploymentTypeRepository employmentTypeRepository;

    private final EmploymentTypeQueryService employmentTypeQueryService;

    public EmploymentTypeResource(
        EmploymentTypeService employmentTypeService,
        EmploymentTypeRepository employmentTypeRepository,
        EmploymentTypeQueryService employmentTypeQueryService
    ) {
        this.employmentTypeService = employmentTypeService;
        this.employmentTypeRepository = employmentTypeRepository;
        this.employmentTypeQueryService = employmentTypeQueryService;
    }

    /**
     * {@code POST  /employment-types} : Create a new employmentType.
     *
     * @param employmentTypeDTO the employmentTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employmentTypeDTO, or with status {@code 400 (Bad Request)} if the employmentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employment-types")
    public ResponseEntity<EmploymentTypeDTO> createEmploymentType(@Valid @RequestBody EmploymentTypeDTO employmentTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmploymentType : {}", employmentTypeDTO);
        if (employmentTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new employmentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmploymentTypeDTO result = employmentTypeService.save(employmentTypeDTO);
        return ResponseEntity
            .created(new URI("/api/employment-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employment-types/:id} : Updates an existing employmentType.
     *
     * @param id the id of the employmentTypeDTO to save.
     * @param employmentTypeDTO the employmentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the employmentTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employmentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employment-types/{id}")
    public ResponseEntity<EmploymentTypeDTO> updateEmploymentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmploymentTypeDTO employmentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmploymentType : {}, {}", id, employmentTypeDTO);
        if (employmentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employmentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmploymentTypeDTO result = employmentTypeService.save(employmentTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employmentTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employment-types/:id} : Partial updates given fields of an existing employmentType, field will ignore if it is null
     *
     * @param id the id of the employmentTypeDTO to save.
     * @param employmentTypeDTO the employmentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the employmentTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employmentTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employmentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employment-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EmploymentTypeDTO> partialUpdateEmploymentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmploymentTypeDTO employmentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmploymentType partially : {}, {}", id, employmentTypeDTO);
        if (employmentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employmentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmploymentTypeDTO> result = employmentTypeService.partialUpdate(employmentTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employmentTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employment-types} : get all the employmentTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employmentTypes in body.
     */
    @GetMapping("/employment-types")
    public ResponseEntity<List<EmploymentTypeDTO>> getAllEmploymentTypes(EmploymentTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EmploymentTypes by criteria: {}", criteria);
        Page<EmploymentTypeDTO> page = employmentTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employment-types/count} : count all the employmentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/employment-types/count")
    public ResponseEntity<Long> countEmploymentTypes(EmploymentTypeCriteria criteria) {
        log.debug("REST request to count EmploymentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(employmentTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employment-types/:id} : get the "id" employmentType.
     *
     * @param id the id of the employmentTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employmentTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employment-types/{id}")
    public ResponseEntity<EmploymentTypeDTO> getEmploymentType(@PathVariable Long id) {
        log.debug("REST request to get EmploymentType : {}", id);
        Optional<EmploymentTypeDTO> employmentTypeDTO = employmentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employmentTypeDTO);
    }

    /**
     * {@code DELETE  /employment-types/:id} : delete the "id" employmentType.
     *
     * @param id the id of the employmentTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employment-types/{id}")
    public ResponseEntity<Void> deleteEmploymentType(@PathVariable Long id) {
        log.debug("REST request to delete EmploymentType : {}", id);
        employmentTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
