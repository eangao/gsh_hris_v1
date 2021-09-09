package com.gsh.hris.repository;

import com.gsh.hris.domain.DutySchedule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DutySchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DutyScheduleRepository extends JpaRepository<DutySchedule, Long>, JpaSpecificationExecutor<DutySchedule> {}
