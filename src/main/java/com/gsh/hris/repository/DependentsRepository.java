package com.gsh.hris.repository;

import com.gsh.hris.domain.Dependents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dependents entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DependentsRepository extends JpaRepository<Dependents, Long>, JpaSpecificationExecutor<Dependents> {}
