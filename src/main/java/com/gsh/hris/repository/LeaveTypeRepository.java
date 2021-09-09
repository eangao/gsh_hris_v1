package com.gsh.hris.repository;

import com.gsh.hris.domain.LeaveType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LeaveType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long>, JpaSpecificationExecutor<LeaveType> {}
