package com.gsh.hris.repository;

import com.gsh.hris.domain.EmploymentType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EmploymentType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmploymentTypeRepository extends JpaRepository<EmploymentType, Long>, JpaSpecificationExecutor<EmploymentType> {}
