package com.gsh.hris.service.impl;

import com.gsh.hris.domain.Dependents;
import com.gsh.hris.repository.DependentsRepository;
import com.gsh.hris.service.DependentsService;
import com.gsh.hris.service.dto.DependentsDTO;
import com.gsh.hris.service.mapper.DependentsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dependents}.
 */
@Service
@Transactional
public class DependentsServiceImpl implements DependentsService {

    private final Logger log = LoggerFactory.getLogger(DependentsServiceImpl.class);

    private final DependentsRepository dependentsRepository;

    private final DependentsMapper dependentsMapper;

    public DependentsServiceImpl(DependentsRepository dependentsRepository, DependentsMapper dependentsMapper) {
        this.dependentsRepository = dependentsRepository;
        this.dependentsMapper = dependentsMapper;
    }

    @Override
    public DependentsDTO save(DependentsDTO dependentsDTO) {
        log.debug("Request to save Dependents : {}", dependentsDTO);
        Dependents dependents = dependentsMapper.toEntity(dependentsDTO);
        dependents = dependentsRepository.save(dependents);
        return dependentsMapper.toDto(dependents);
    }

    @Override
    public Optional<DependentsDTO> partialUpdate(DependentsDTO dependentsDTO) {
        log.debug("Request to partially update Dependents : {}", dependentsDTO);

        return dependentsRepository
            .findById(dependentsDTO.getId())
            .map(
                existingDependents -> {
                    dependentsMapper.partialUpdate(existingDependents, dependentsDTO);

                    return existingDependents;
                }
            )
            .map(dependentsRepository::save)
            .map(dependentsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DependentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dependents");
        return dependentsRepository.findAll(pageable).map(dependentsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DependentsDTO> findOne(Long id) {
        log.debug("Request to get Dependents : {}", id);
        return dependentsRepository.findById(id).map(dependentsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dependents : {}", id);
        dependentsRepository.deleteById(id);
    }
}
