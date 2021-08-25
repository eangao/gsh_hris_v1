package com.gsh.hris.repository;

import com.gsh.hris.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Position entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PositionRepository extends JpaRepository<Position, Long>, JpaSpecificationExecutor<Position> {}
