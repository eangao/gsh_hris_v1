package com.gsh.hris.repository;

import com.gsh.hris.domain.Education;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Education entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationRepository extends JpaRepository<Education, Long>, JpaSpecificationExecutor<Education> {}
