package com.gsh.hris.service.impl;

import com.gsh.hris.domain.Position;
import com.gsh.hris.repository.PositionRepository;
import com.gsh.hris.service.PositionService;
import com.gsh.hris.service.dto.PositionDTO;
import com.gsh.hris.service.mapper.PositionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Position}.
 */
@Service
@Transactional
public class PositionServiceImpl implements PositionService {

    private final Logger log = LoggerFactory.getLogger(PositionServiceImpl.class);

    private final PositionRepository positionRepository;

    private final PositionMapper positionMapper;

    public PositionServiceImpl(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    @Override
    public PositionDTO save(PositionDTO positionDTO) {
        log.debug("Request to save Position : {}", positionDTO);
        Position position = positionMapper.toEntity(positionDTO);
        position = positionRepository.save(position);
        return positionMapper.toDto(position);
    }

    @Override
    public Optional<PositionDTO> partialUpdate(PositionDTO positionDTO) {
        log.debug("Request to partially update Position : {}", positionDTO);

        return positionRepository
            .findById(positionDTO.getId())
            .map(
                existingPosition -> {
                    positionMapper.partialUpdate(existingPosition, positionDTO);

                    return existingPosition;
                }
            )
            .map(positionRepository::save)
            .map(positionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PositionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Positions");
        return positionRepository.findAll(pageable).map(positionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PositionDTO> findOne(Long id) {
        log.debug("Request to get Position : {}", id);
        return positionRepository.findById(id).map(positionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Position : {}", id);
        positionRepository.deleteById(id);
    }
}
