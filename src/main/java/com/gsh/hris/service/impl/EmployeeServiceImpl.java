package com.gsh.hris.service.impl;

import com.gsh.hris.domain.Authority;
import com.gsh.hris.domain.Employee;
import com.gsh.hris.domain.User;
import com.gsh.hris.repository.EmployeeRepository;
import com.gsh.hris.repository.UserRepository;
import com.gsh.hris.service.EmployeeService;
import com.gsh.hris.service.MailService;
import com.gsh.hris.service.UserService;
import com.gsh.hris.service.dto.AdminUserDTO;
import com.gsh.hris.service.dto.EmployeeDTO;
import com.gsh.hris.service.mapper.EmployeeMapper;
import com.gsh.hris.web.rest.errors.BadRequestAlertException;
import com.gsh.hris.web.rest.errors.EmailAlreadyUsedException;
import com.gsh.hris.web.rest.errors.LoginAlreadyUsedException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    public EmployeeServiceImpl(
        EmployeeRepository employeeRepository,
        EmployeeMapper employeeMapper,
        UserService userService,
        UserRepository userRepository,
        MailService mailService
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @Override
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        log.debug("Request to save Employee : {}", employeeDTO);

        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setLogin(employeeDTO.getUsername());
        userDTO.setEmail(employeeDTO.getEmail());
        userDTO.setFirstName(employeeDTO.getFirstName());
        userDTO.setLastName(employeeDTO.getLastName());

        //to set the default user role
        Set<String> role = new HashSet<>();
        role.add("ROLE_USER");
        userDTO.setAuthorities(role);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);

            Employee employee = employeeMapper.toEntity(employeeDTO);
            employee.setUser(newUser); //to map user to employee
            employee = employeeRepository.save(employee);

            mailService.sendCreationEmail(newUser);

            return employeeMapper.toDto(employee);
        }
    }

    @Override
    public EmployeeDTO update(EmployeeDTO employeeDTO) {
        log.debug("Request to update Employee : {}", employeeDTO);

        Optional<User> userToUpdate = userRepository.findById(employeeDTO.getUser().getId());

        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setId(employeeDTO.getUser().getId());
        userDTO.setLogin(employeeDTO.getUsername());
        userDTO.setEmail(employeeDTO.getEmail());
        userDTO.setFirstName(employeeDTO.getFirstName());
        userDTO.setLastName(employeeDTO.getLastName());
        userDTO.setImageUrl(userToUpdate.get().getImageUrl());
        userDTO.setActivated(userToUpdate.get().isActivated());
        userDTO.setLangKey(userToUpdate.get().getLangKey());
        userDTO.setAuthorities(userToUpdate.get().getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));

        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(employeeDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(employeeDTO.getUser().getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(employeeDTO.getUsername().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(employeeDTO.getUser().getId()))) {
            throw new LoginAlreadyUsedException();
        }

        Optional<AdminUserDTO> updatedUser = userService.updateUser(userDTO);

        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee = employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    @Override
    public Optional<EmployeeDTO> partialUpdate(EmployeeDTO employeeDTO) {
        log.debug("Request to partially update Employee : {}", employeeDTO);

        return employeeRepository
            .findById(employeeDTO.getId())
            .map(
                existingEmployee -> {
                    employeeMapper.partialUpdate(existingEmployee, employeeDTO);

                    return existingEmployee;
                }
            )
            .map(employeeRepository::save)
            .map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll(pageable).map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id).map(employeeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }
}
