package com.gsh.hris.service.impl;

import com.gsh.hris.domain.TrainingHistory;
import com.gsh.hris.repository.TrainingHistoryRepository;
import com.gsh.hris.service.TrainingHistoryService;
import com.gsh.hris.service.dto.TrainingHistoryDTO;
import com.gsh.hris.service.mapper.TrainingHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TrainingHistory}.
 */
@Service
@Transactional
public class TrainingHistoryServiceImpl implements TrainingHistoryService {

    private final Logger log = LoggerFactory.getLogger(TrainingHistoryServiceImpl.class);

    private final TrainingHistoryRepository trainingHistoryRepository;

    private final TrainingHistoryMapper trainingHistoryMapper;

    public TrainingHistoryServiceImpl(TrainingHistoryRepository trainingHistoryRepository, TrainingHistoryMapper trainingHistoryMapper) {
        this.trainingHistoryRepository = trainingHistoryRepository;
        this.trainingHistoryMapper = trainingHistoryMapper;
    }

    @Override
    public TrainingHistoryDTO save(TrainingHistoryDTO trainingHistoryDTO) {
        log.debug("Request to save TrainingHistory : {}", trainingHistoryDTO);
        TrainingHistory trainingHistory = trainingHistoryMapper.toEntity(trainingHistoryDTO);
        trainingHistory = trainingHistoryRepository.save(trainingHistory);
        return trainingHistoryMapper.toDto(trainingHistory);
    }

    @Override
    public Optional<TrainingHistoryDTO> partialUpdate(TrainingHistoryDTO trainingHistoryDTO) {
        log.debug("Request to partially update TrainingHistory : {}", trainingHistoryDTO);

        return trainingHistoryRepository
            .findById(trainingHistoryDTO.getId())
            .map(
                existingTrainingHistory -> {
                    trainingHistoryMapper.partialUpdate(existingTrainingHistory, trainingHistoryDTO);

                    return existingTrainingHistory;
                }
            )
            .map(trainingHistoryRepository::save)
            .map(trainingHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TrainingHistories");
        return trainingHistoryRepository.findAll(pageable).map(trainingHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingHistoryDTO> findOne(Long id) {
        log.debug("Request to get TrainingHistory : {}", id);
        return trainingHistoryRepository.findById(id).map(trainingHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TrainingHistory : {}", id);
        trainingHistoryRepository.deleteById(id);
    }
}
