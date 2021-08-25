package com.gsh.hris.repository;

import com.gsh.hris.domain.HolidayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HolidayType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HolidayTypeRepository extends JpaRepository<HolidayType, Long>, JpaSpecificationExecutor<HolidayType> {}
