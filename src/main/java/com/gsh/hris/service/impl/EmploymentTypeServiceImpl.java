package com.gsh.hris.service.impl;

import com.gsh.hris.domain.EmploymentType;
import com.gsh.hris.repository.EmploymentTypeRepository;
import com.gsh.hris.service.EmploymentTypeService;
import com.gsh.hris.service.dto.EmploymentTypeDTO;
import com.gsh.hris.service.mapper.EmploymentTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EmploymentType}.
 */
@Service
@Transactional
public class EmploymentTypeServiceImpl implements EmploymentTypeService {

    private final Logger log = LoggerFactory.getLogger(EmploymentTypeServiceImpl.class);

    private final EmploymentTypeRepository employmentTypeRepository;

    private final EmploymentTypeMapper employmentTypeMapper;

    public EmploymentTypeServiceImpl(EmploymentTypeRepository employmentTypeRepository, EmploymentTypeMapper employmentTypeMapper) {
        this.employmentTypeRepository = employmentTypeRepository;
        this.employmentTypeMapper = employmentTypeMapper;
    }

    @Override
    public EmploymentTypeDTO save(EmploymentTypeDTO employmentTypeDTO) {
        log.debug("Request to save EmploymentType : {}", employmentTypeDTO);
        EmploymentType employmentType = employmentTypeMapper.toEntity(employmentTypeDTO);
        employmentType = employmentTypeRepository.save(employmentType);
        return employmentTypeMapper.toDto(employmentType);
    }

    @Override
    public Optional<EmploymentTypeDTO> partialUpdate(EmploymentTypeDTO employmentTypeDTO) {
        log.debug("Request to partially update EmploymentType : {}", employmentTypeDTO);

        return employmentTypeRepository
            .findById(employmentTypeDTO.getId())
            .map(
                existingEmploymentType -> {
                    employmentTypeMapper.partialUpdate(existingEmploymentType, employmentTypeDTO);

                    return existingEmploymentType;
                }
            )
            .map(employmentTypeRepository::save)
            .map(employmentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmploymentTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmploymentTypes");
        return employmentTypeRepository.findAll(pageable).map(employmentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmploymentTypeDTO> findOne(Long id) {
        log.debug("Request to get EmploymentType : {}", id);
        return employmentTypeRepository.findById(id).map(employmentTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmploymentType : {}", id);
        employmentTypeRepository.deleteById(id);
    }
}
