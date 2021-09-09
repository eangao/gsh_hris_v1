package com.gsh.hris.repository;

import com.gsh.hris.domain.Benefits;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Benefits entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BenefitsRepository extends JpaRepository<Benefits, Long>, JpaSpecificationExecutor<Benefits> {}
