package com.gsh.hris.repository;

import com.gsh.hris.domain.TrainingHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TrainingHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainingHistoryRepository extends JpaRepository<TrainingHistory, Long>, JpaSpecificationExecutor<TrainingHistory> {}
