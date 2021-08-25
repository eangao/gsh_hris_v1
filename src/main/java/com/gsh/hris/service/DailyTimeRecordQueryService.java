package com.gsh.hris.service;

import com.gsh.hris.domain.DailyTimeRecord;
import com.gsh.hris.domain.DailyTimeRecord_;
import com.gsh.hris.domain.Employee_;
import com.gsh.hris.repository.DailyTimeRecordRepository;
import com.gsh.hris.service.criteria.DailyTimeRecordCriteria;
import com.gsh.hris.service.dto.DailyTimeRecordDTO;
import com.gsh.hris.service.mapper.DailyTimeRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for {@link DailyTimeRecord} entities in the database.
 * The main input is a {@link DailyTimeRecordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DailyTimeRecordDTO} or a {@link Page} of {@link DailyTimeRecordDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DailyTimeRecordQueryService extends QueryService<DailyTimeRecord> {

    private final Logger log = LoggerFactory.getLogger(DailyTimeRecordQueryService.class);

    private final DailyTimeRecordRepository dailyTimeRecordRepository;

    private final DailyTimeRecordMapper dailyTimeRecordMapper;

    public DailyTimeRecordQueryService(DailyTimeRecordRepository dailyTimeRecordRepository, DailyTimeRecordMapper dailyTimeRecordMapper) {
        this.dailyTimeRecordRepository = dailyTimeRecordRepository;
        this.dailyTimeRecordMapper = dailyTimeRecordMapper;
    }

    /**
     * Return a {@link List} of {@link DailyTimeRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DailyTimeRecordDTO> findByCriteria(DailyTimeRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DailyTimeRecord> specification = createSpecification(criteria);
        return dailyTimeRecordMapper.toDto(dailyTimeRecordRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DailyTimeRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DailyTimeRecordDTO> findByCriteria(DailyTimeRecordCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DailyTimeRecord> specification = createSpecification(criteria);
        return dailyTimeRecordRepository.findAll(specification, page).map(dailyTimeRecordMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DailyTimeRecordCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DailyTimeRecord> specification = createSpecification(criteria);
        return dailyTimeRecordRepository.count(specification);
    }

    /**
     * Function to convert {@link DailyTimeRecordCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DailyTimeRecord> createSpecification(DailyTimeRecordCriteria criteria) {
        Specification<DailyTimeRecord> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DailyTimeRecord_.id));
            }
            if (criteria.getDateTimeIn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTimeIn(), DailyTimeRecord_.dateTimeIn));
            }
            if (criteria.getDateTimeOut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTimeOut(), DailyTimeRecord_.dateTimeOut));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(DailyTimeRecord_.employee, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
