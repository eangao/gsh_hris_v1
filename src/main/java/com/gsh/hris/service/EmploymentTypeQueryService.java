package com.gsh.hris.service;

import com.gsh.hris.domain.Employee_;
import com.gsh.hris.domain.EmploymentType;
import com.gsh.hris.domain.EmploymentType_;
import com.gsh.hris.repository.EmploymentTypeRepository;
import com.gsh.hris.service.criteria.EmploymentTypeCriteria;
import com.gsh.hris.service.dto.EmploymentTypeDTO;
import com.gsh.hris.service.mapper.EmploymentTypeMapper;
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
 * Service for executing complex queries for {@link EmploymentType} entities in the database.
 * The main input is a {@link EmploymentTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmploymentTypeDTO} or a {@link Page} of {@link EmploymentTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmploymentTypeQueryService extends QueryService<EmploymentType> {

    private final Logger log = LoggerFactory.getLogger(EmploymentTypeQueryService.class);

    private final EmploymentTypeRepository employmentTypeRepository;

    private final EmploymentTypeMapper employmentTypeMapper;

    public EmploymentTypeQueryService(EmploymentTypeRepository employmentTypeRepository, EmploymentTypeMapper employmentTypeMapper) {
        this.employmentTypeRepository = employmentTypeRepository;
        this.employmentTypeMapper = employmentTypeMapper;
    }

    /**
     * Return a {@link List} of {@link EmploymentTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmploymentTypeDTO> findByCriteria(EmploymentTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmploymentType> specification = createSpecification(criteria);
        return employmentTypeMapper.toDto(employmentTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmploymentTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmploymentTypeDTO> findByCriteria(EmploymentTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmploymentType> specification = createSpecification(criteria);
        return employmentTypeRepository.findAll(specification, page).map(employmentTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmploymentTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmploymentType> specification = createSpecification(criteria);
        return employmentTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link EmploymentTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmploymentType> createSpecification(EmploymentTypeCriteria criteria) {
        Specification<EmploymentType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmploymentType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EmploymentType_.name));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(EmploymentType_.employees, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
