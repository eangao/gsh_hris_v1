package com.gsh.hris.web.rest;

import com.gsh.hris.repository.HolidayRepository;
import com.gsh.hris.service.HolidayQueryService;
import com.gsh.hris.service.HolidayService;
import com.gsh.hris.service.criteria.HolidayCriteria;
import com.gsh.hris.service.dto.HolidayDTO;
import com.gsh.hris.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.gsh.hris.domain.Holiday}.
 */
@RestController
@RequestMapping("/api")
public class HolidayResource {

    private final Logger log = LoggerFactory.getLogger(HolidayResource.class);

    private static final String ENTITY_NAME = "holiday";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HolidayService holidayService;

    private final HolidayRepository holidayRepository;

    private final HolidayQueryService holidayQueryService;

    public HolidayResource(HolidayService holidayService, HolidayRepository holidayRepository, HolidayQueryService holidayQueryService) {
        this.holidayService = holidayService;
        this.holidayRepository = holidayRepository;
        this.holidayQueryService = holidayQueryService;
    }

    /**
     * {@code POST  /holidays} : Create a new holiday.
     *
     * @param holidayDTO the holidayDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new holidayDTO, or with status {@code 400 (Bad Request)} if the holiday has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/holidays")
    public ResponseEntity<HolidayDTO> createHoliday(@RequestBody HolidayDTO holidayDTO) throws URISyntaxException {
        log.debug("REST request to save Holiday : {}", holidayDTO);
        if (holidayDTO.getId() != null) {
            throw new BadRequestAlertException("A new holiday cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HolidayDTO result = holidayService.save(holidayDTO);
        return ResponseEntity
            .created(new URI("/api/holidays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /holidays/:id} : Updates an existing holiday.
     *
     * @param id the id of the holidayDTO to save.
     * @param holidayDTO the holidayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated holidayDTO,
     * or with status {@code 400 (Bad Request)} if the holidayDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the holidayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/holidays/{id}")
    public ResponseEntity<HolidayDTO> updateHoliday(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HolidayDTO holidayDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Holiday : {}, {}", id, holidayDTO);
        if (holidayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, holidayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!holidayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HolidayDTO result = holidayService.save(holidayDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, holidayDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /holidays/:id} : Partial updates given fields of an existing holiday, field will ignore if it is null
     *
     * @param id the id of the holidayDTO to save.
     * @param holidayDTO the holidayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated holidayDTO,
     * or with status {@code 400 (Bad Request)} if the holidayDTO is not valid,
     * or with status {@code 404 (Not Found)} if the holidayDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the holidayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/holidays/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HolidayDTO> partialUpdateHoliday(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HolidayDTO holidayDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Holiday partially : {}, {}", id, holidayDTO);
        if (holidayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, holidayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!holidayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HolidayDTO> result = holidayService.partialUpdate(holidayDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, holidayDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /holidays} : get all the holidays.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of holidays in body.
     */
    @GetMapping("/holidays")
    public ResponseEntity<List<HolidayDTO>> getAllHolidays(HolidayCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Holidays by criteria: {}", criteria);
        Page<HolidayDTO> page = holidayQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /holidays/count} : count all the holidays.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/holidays/count")
    public ResponseEntity<Long> countHolidays(HolidayCriteria criteria) {
        log.debug("REST request to count Holidays by criteria: {}", criteria);
        return ResponseEntity.ok().body(holidayQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /holidays/:id} : get the "id" holiday.
     *
     * @param id the id of the holidayDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the holidayDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/holidays/{id}")
    public ResponseEntity<HolidayDTO> getHoliday(@PathVariable Long id) {
        log.debug("REST request to get Holiday : {}", id);
        Optional<HolidayDTO> holidayDTO = holidayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(holidayDTO);
    }

    /**
     * {@code DELETE  /holidays/:id} : delete the "id" holiday.
     *
     * @param id the id of the holidayDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable Long id) {
        log.debug("REST request to delete Holiday : {}", id);
        holidayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
