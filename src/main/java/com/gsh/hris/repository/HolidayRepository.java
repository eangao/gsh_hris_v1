package com.gsh.hris.repository;

import com.gsh.hris.domain.Holiday;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Holiday entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long>, JpaSpecificationExecutor<Holiday> {}
