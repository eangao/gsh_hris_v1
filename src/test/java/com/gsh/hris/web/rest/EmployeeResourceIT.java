package com.gsh.hris.web.rest;

import com.gsh.hris.IntegrationTest;
import com.gsh.hris.domain.*;
import com.gsh.hris.repository.EmployeeRepository;
import com.gsh.hris.service.dto.EmployeeDTO;
import com.gsh.hris.service.mapper.EmployeeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final Integer DEFAULT_EMPLOYEE_ID = 1;
    private static final Integer UPDATED_EMPLOYEE_ID = 2;
    private static final Integer SMALLER_EMPLOYEE_ID = 1 - 1;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_SUFFIX = "AAAAAAAAAA";
    private static final String UPDATED_NAME_SUFFIX = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTHDATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_SEX = false;
    private static final Boolean UPDATED_SEX = true;

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_NOT_LOCKED = false;
    private static final Boolean UPDATED_IS_NOT_LOCKED = true;

    private static final LocalDate DEFAULT_DATE_HIRED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_HIRED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_HIRED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_DENO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DENO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DENO = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_SICK_LEAVE_YEARLY_CREDIT = 1;
    private static final Integer UPDATED_SICK_LEAVE_YEARLY_CREDIT = 2;
    private static final Integer SMALLER_SICK_LEAVE_YEARLY_CREDIT = 1 - 1;

    private static final Integer DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED = 1;
    private static final Integer UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED = 2;
    private static final Integer SMALLER_SICK_LEAVE_YEARLY_CREDIT_USED = 1 - 1;

    private static final Integer DEFAULT_LEAVE_YEARLY_CREDIT = 1;
    private static final Integer UPDATED_LEAVE_YEARLY_CREDIT = 2;
    private static final Integer SMALLER_LEAVE_YEARLY_CREDIT = 1 - 1;

    private static final Integer DEFAULT_LEAVE_YEARLY_CREDIT_USED = 1;
    private static final Integer UPDATED_LEAVE_YEARLY_CREDIT_USED = 2;
    private static final Integer SMALLER_LEAVE_YEARLY_CREDIT_USED = 1 - 1;

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .username(DEFAULT_USERNAME)
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .nameSuffix(DEFAULT_NAME_SUFFIX)
            .birthdate(DEFAULT_BIRTHDATE)
            .sex(DEFAULT_SEX)
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .email(DEFAULT_EMAIL)
            .isNotLocked(DEFAULT_IS_NOT_LOCKED)
            .dateHired(DEFAULT_DATE_HIRED)
            .dateDeno(DEFAULT_DATE_DENO)
            .sickLeaveYearlyCredit(DEFAULT_SICK_LEAVE_YEARLY_CREDIT)
            .sickLeaveYearlyCreditUsed(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED)
            .leaveYearlyCredit(DEFAULT_LEAVE_YEARLY_CREDIT)
            .leaveYearlyCreditUsed(DEFAULT_LEAVE_YEARLY_CREDIT_USED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employee.setUser(user);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        employee.setDepartment(department);
        // Add required entity
        EmploymentType employmentType;
        if (TestUtil.findAll(em, EmploymentType.class).isEmpty()) {
            employmentType = EmploymentTypeResourceIT.createEntity(em);
            em.persist(employmentType);
            em.flush();
        } else {
            employmentType = TestUtil.findAll(em, EmploymentType.class).get(0);
        }
        employee.setEmploymentType(employmentType);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeId(UPDATED_EMPLOYEE_ID)
            .username(UPDATED_USERNAME)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .nameSuffix(UPDATED_NAME_SUFFIX)
            .birthdate(UPDATED_BIRTHDATE)
            .sex(UPDATED_SEX)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .email(UPDATED_EMAIL)
            .isNotLocked(UPDATED_IS_NOT_LOCKED)
            .dateHired(UPDATED_DATE_HIRED)
            .dateDeno(UPDATED_DATE_DENO)
            .sickLeaveYearlyCredit(UPDATED_SICK_LEAVE_YEARLY_CREDIT)
            .sickLeaveYearlyCreditUsed(UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED)
            .leaveYearlyCredit(UPDATED_LEAVE_YEARLY_CREDIT)
            .leaveYearlyCreditUsed(UPDATED_LEAVE_YEARLY_CREDIT_USED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employee.setUser(user);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        employee.setDepartment(department);
        // Add required entity
        EmploymentType employmentType;
        if (TestUtil.findAll(em, EmploymentType.class).isEmpty()) {
            employmentType = EmploymentTypeResourceIT.createUpdatedEntity(em);
            em.persist(employmentType);
            em.flush();
        } else {
            employmentType = TestUtil.findAll(em, EmploymentType.class).get(0);
        }
        employee.setEmploymentType(employmentType);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();
        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testEmployee.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getNameSuffix()).isEqualTo(DEFAULT_NAME_SUFFIX);
        assertThat(testEmployee.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testEmployee.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testEmployee.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getIsNotLocked()).isEqualTo(DEFAULT_IS_NOT_LOCKED);
        assertThat(testEmployee.getDateHired()).isEqualTo(DEFAULT_DATE_HIRED);
        assertThat(testEmployee.getDateDeno()).isEqualTo(DEFAULT_DATE_DENO);
        assertThat(testEmployee.getSickLeaveYearlyCredit()).isEqualTo(DEFAULT_SICK_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getSickLeaveYearlyCreditUsed()).isEqualTo(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getLeaveYearlyCredit()).isEqualTo(DEFAULT_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getLeaveYearlyCreditUsed()).isEqualTo(DEFAULT_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].nameSuffix").value(hasItem(DEFAULT_NAME_SUFFIX)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.booleanValue())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isNotLocked").value(hasItem(DEFAULT_IS_NOT_LOCKED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateHired").value(hasItem(DEFAULT_DATE_HIRED.toString())))
            .andExpect(jsonPath("$.[*].dateDeno").value(hasItem(DEFAULT_DATE_DENO.toString())))
            .andExpect(jsonPath("$.[*].sickLeaveYearlyCredit").value(hasItem(DEFAULT_SICK_LEAVE_YEARLY_CREDIT)))
            .andExpect(jsonPath("$.[*].sickLeaveYearlyCreditUsed").value(hasItem(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED)))
            .andExpect(jsonPath("$.[*].leaveYearlyCredit").value(hasItem(DEFAULT_LEAVE_YEARLY_CREDIT)))
            .andExpect(jsonPath("$.[*].leaveYearlyCreditUsed").value(hasItem(DEFAULT_LEAVE_YEARLY_CREDIT_USED)));
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.nameSuffix").value(DEFAULT_NAME_SUFFIX))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.booleanValue()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isNotLocked").value(DEFAULT_IS_NOT_LOCKED.booleanValue()))
            .andExpect(jsonPath("$.dateHired").value(DEFAULT_DATE_HIRED.toString()))
            .andExpect(jsonPath("$.dateDeno").value(DEFAULT_DATE_DENO.toString()))
            .andExpect(jsonPath("$.sickLeaveYearlyCredit").value(DEFAULT_SICK_LEAVE_YEARLY_CREDIT))
            .andExpect(jsonPath("$.sickLeaveYearlyCreditUsed").value(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED))
            .andExpect(jsonPath("$.leaveYearlyCredit").value(DEFAULT_LEAVE_YEARLY_CREDIT))
            .andExpect(jsonPath("$.leaveYearlyCreditUsed").value(DEFAULT_LEAVE_YEARLY_CREDIT_USED));
    }

    @Test
    @Transactional
    void getEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        Long id = employee.getId();

        defaultEmployeeShouldBeFound("id.equals=" + id);
        defaultEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId not equals to DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.notEquals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId not equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.notEquals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the employeeList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId is not null
        defaultEmployeeShouldBeFound("employeeId.specified=true");

        // Get all the employeeList where employeeId is null
        defaultEmployeeShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username equals to DEFAULT_USERNAME
        defaultEmployeeShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the employeeList where username equals to UPDATED_USERNAME
        defaultEmployeeShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username not equals to DEFAULT_USERNAME
        defaultEmployeeShouldNotBeFound("username.notEquals=" + DEFAULT_USERNAME);

        // Get all the employeeList where username not equals to UPDATED_USERNAME
        defaultEmployeeShouldBeFound("username.notEquals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultEmployeeShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the employeeList where username equals to UPDATED_USERNAME
        defaultEmployeeShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username is not null
        defaultEmployeeShouldBeFound("username.specified=true");

        // Get all the employeeList where username is null
        defaultEmployeeShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username contains DEFAULT_USERNAME
        defaultEmployeeShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the employeeList where username contains UPDATED_USERNAME
        defaultEmployeeShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username does not contain DEFAULT_USERNAME
        defaultEmployeeShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the employeeList where username does not contain UPDATED_USERNAME
        defaultEmployeeShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName equals to DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName not equals to DEFAULT_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName not equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName is not null
        defaultEmployeeShouldBeFound("firstName.specified=true");

        // Get all the employeeList where firstName is null
        defaultEmployeeShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName contains DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName contains UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName does not contain DEFAULT_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName does not contain UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName equals to DEFAULT_MIDDLE_NAME
        defaultEmployeeShouldBeFound("middleName.equals=" + DEFAULT_MIDDLE_NAME);

        // Get all the employeeList where middleName equals to UPDATED_MIDDLE_NAME
        defaultEmployeeShouldNotBeFound("middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName not equals to DEFAULT_MIDDLE_NAME
        defaultEmployeeShouldNotBeFound("middleName.notEquals=" + DEFAULT_MIDDLE_NAME);

        // Get all the employeeList where middleName not equals to UPDATED_MIDDLE_NAME
        defaultEmployeeShouldBeFound("middleName.notEquals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName in DEFAULT_MIDDLE_NAME or UPDATED_MIDDLE_NAME
        defaultEmployeeShouldBeFound("middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME);

        // Get all the employeeList where middleName equals to UPDATED_MIDDLE_NAME
        defaultEmployeeShouldNotBeFound("middleName.in=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName is not null
        defaultEmployeeShouldBeFound("middleName.specified=true");

        // Get all the employeeList where middleName is null
        defaultEmployeeShouldNotBeFound("middleName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName contains DEFAULT_MIDDLE_NAME
        defaultEmployeeShouldBeFound("middleName.contains=" + DEFAULT_MIDDLE_NAME);

        // Get all the employeeList where middleName contains UPDATED_MIDDLE_NAME
        defaultEmployeeShouldNotBeFound("middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName does not contain DEFAULT_MIDDLE_NAME
        defaultEmployeeShouldNotBeFound("middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME);

        // Get all the employeeList where middleName does not contain UPDATED_MIDDLE_NAME
        defaultEmployeeShouldBeFound("middleName.doesNotContain=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName equals to DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName not equals to DEFAULT_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName not equals to UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName is not null
        defaultEmployeeShouldBeFound("lastName.specified=true");

        // Get all the employeeList where lastName is null
        defaultEmployeeShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName contains DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName contains UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName does not contain DEFAULT_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName does not contain UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix equals to DEFAULT_NAME_SUFFIX
        defaultEmployeeShouldBeFound("nameSuffix.equals=" + DEFAULT_NAME_SUFFIX);

        // Get all the employeeList where nameSuffix equals to UPDATED_NAME_SUFFIX
        defaultEmployeeShouldNotBeFound("nameSuffix.equals=" + UPDATED_NAME_SUFFIX);
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix not equals to DEFAULT_NAME_SUFFIX
        defaultEmployeeShouldNotBeFound("nameSuffix.notEquals=" + DEFAULT_NAME_SUFFIX);

        // Get all the employeeList where nameSuffix not equals to UPDATED_NAME_SUFFIX
        defaultEmployeeShouldBeFound("nameSuffix.notEquals=" + UPDATED_NAME_SUFFIX);
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix in DEFAULT_NAME_SUFFIX or UPDATED_NAME_SUFFIX
        defaultEmployeeShouldBeFound("nameSuffix.in=" + DEFAULT_NAME_SUFFIX + "," + UPDATED_NAME_SUFFIX);

        // Get all the employeeList where nameSuffix equals to UPDATED_NAME_SUFFIX
        defaultEmployeeShouldNotBeFound("nameSuffix.in=" + UPDATED_NAME_SUFFIX);
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix is not null
        defaultEmployeeShouldBeFound("nameSuffix.specified=true");

        // Get all the employeeList where nameSuffix is null
        defaultEmployeeShouldNotBeFound("nameSuffix.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix contains DEFAULT_NAME_SUFFIX
        defaultEmployeeShouldBeFound("nameSuffix.contains=" + DEFAULT_NAME_SUFFIX);

        // Get all the employeeList where nameSuffix contains UPDATED_NAME_SUFFIX
        defaultEmployeeShouldNotBeFound("nameSuffix.contains=" + UPDATED_NAME_SUFFIX);
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix does not contain DEFAULT_NAME_SUFFIX
        defaultEmployeeShouldNotBeFound("nameSuffix.doesNotContain=" + DEFAULT_NAME_SUFFIX);

        // Get all the employeeList where nameSuffix does not contain UPDATED_NAME_SUFFIX
        defaultEmployeeShouldBeFound("nameSuffix.doesNotContain=" + UPDATED_NAME_SUFFIX);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate equals to DEFAULT_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.equals=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate equals to UPDATED_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.equals=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate not equals to DEFAULT_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.notEquals=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate not equals to UPDATED_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.notEquals=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate in DEFAULT_BIRTHDATE or UPDATED_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.in=" + DEFAULT_BIRTHDATE + "," + UPDATED_BIRTHDATE);

        // Get all the employeeList where birthdate equals to UPDATED_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.in=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate is not null
        defaultEmployeeShouldBeFound("birthdate.specified=true");

        // Get all the employeeList where birthdate is null
        defaultEmployeeShouldNotBeFound("birthdate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate is greater than or equal to DEFAULT_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.greaterThanOrEqual=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate is greater than or equal to UPDATED_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.greaterThanOrEqual=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate is less than or equal to DEFAULT_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.lessThanOrEqual=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate is less than or equal to SMALLER_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.lessThanOrEqual=" + SMALLER_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate is less than DEFAULT_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.lessThan=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate is less than UPDATED_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.lessThan=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate is greater than DEFAULT_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.greaterThan=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate is greater than SMALLER_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.greaterThan=" + SMALLER_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesBySexIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sex equals to DEFAULT_SEX
        defaultEmployeeShouldBeFound("sex.equals=" + DEFAULT_SEX);

        // Get all the employeeList where sex equals to UPDATED_SEX
        defaultEmployeeShouldNotBeFound("sex.equals=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllEmployeesBySexIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sex not equals to DEFAULT_SEX
        defaultEmployeeShouldNotBeFound("sex.notEquals=" + DEFAULT_SEX);

        // Get all the employeeList where sex not equals to UPDATED_SEX
        defaultEmployeeShouldBeFound("sex.notEquals=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllEmployeesBySexIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sex in DEFAULT_SEX or UPDATED_SEX
        defaultEmployeeShouldBeFound("sex.in=" + DEFAULT_SEX + "," + UPDATED_SEX);

        // Get all the employeeList where sex equals to UPDATED_SEX
        defaultEmployeeShouldNotBeFound("sex.in=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllEmployeesBySexIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sex is not null
        defaultEmployeeShouldBeFound("sex.specified=true");

        // Get all the employeeList where sex is null
        defaultEmployeeShouldNotBeFound("sex.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber equals to DEFAULT_MOBILE_NUMBER
        defaultEmployeeShouldBeFound("mobileNumber.equals=" + DEFAULT_MOBILE_NUMBER);

        // Get all the employeeList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldNotBeFound("mobileNumber.equals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber not equals to DEFAULT_MOBILE_NUMBER
        defaultEmployeeShouldNotBeFound("mobileNumber.notEquals=" + DEFAULT_MOBILE_NUMBER);

        // Get all the employeeList where mobileNumber not equals to UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldBeFound("mobileNumber.notEquals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber in DEFAULT_MOBILE_NUMBER or UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldBeFound("mobileNumber.in=" + DEFAULT_MOBILE_NUMBER + "," + UPDATED_MOBILE_NUMBER);

        // Get all the employeeList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldNotBeFound("mobileNumber.in=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber is not null
        defaultEmployeeShouldBeFound("mobileNumber.specified=true");

        // Get all the employeeList where mobileNumber is null
        defaultEmployeeShouldNotBeFound("mobileNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber contains DEFAULT_MOBILE_NUMBER
        defaultEmployeeShouldBeFound("mobileNumber.contains=" + DEFAULT_MOBILE_NUMBER);

        // Get all the employeeList where mobileNumber contains UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldNotBeFound("mobileNumber.contains=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber does not contain DEFAULT_MOBILE_NUMBER
        defaultEmployeeShouldNotBeFound("mobileNumber.doesNotContain=" + DEFAULT_MOBILE_NUMBER);

        // Get all the employeeList where mobileNumber does not contain UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldBeFound("mobileNumber.doesNotContain=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email equals to DEFAULT_EMAIL
        defaultEmployeeShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the employeeList where email equals to UPDATED_EMAIL
        defaultEmployeeShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email not equals to DEFAULT_EMAIL
        defaultEmployeeShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the employeeList where email not equals to UPDATED_EMAIL
        defaultEmployeeShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEmployeeShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the employeeList where email equals to UPDATED_EMAIL
        defaultEmployeeShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email is not null
        defaultEmployeeShouldBeFound("email.specified=true");

        // Get all the employeeList where email is null
        defaultEmployeeShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email contains DEFAULT_EMAIL
        defaultEmployeeShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the employeeList where email contains UPDATED_EMAIL
        defaultEmployeeShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email does not contain DEFAULT_EMAIL
        defaultEmployeeShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the employeeList where email does not contain UPDATED_EMAIL
        defaultEmployeeShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByIsNotLockedIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where isNotLocked equals to DEFAULT_IS_NOT_LOCKED
        defaultEmployeeShouldBeFound("isNotLocked.equals=" + DEFAULT_IS_NOT_LOCKED);

        // Get all the employeeList where isNotLocked equals to UPDATED_IS_NOT_LOCKED
        defaultEmployeeShouldNotBeFound("isNotLocked.equals=" + UPDATED_IS_NOT_LOCKED);
    }

    @Test
    @Transactional
    void getAllEmployeesByIsNotLockedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where isNotLocked not equals to DEFAULT_IS_NOT_LOCKED
        defaultEmployeeShouldNotBeFound("isNotLocked.notEquals=" + DEFAULT_IS_NOT_LOCKED);

        // Get all the employeeList where isNotLocked not equals to UPDATED_IS_NOT_LOCKED
        defaultEmployeeShouldBeFound("isNotLocked.notEquals=" + UPDATED_IS_NOT_LOCKED);
    }

    @Test
    @Transactional
    void getAllEmployeesByIsNotLockedIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where isNotLocked in DEFAULT_IS_NOT_LOCKED or UPDATED_IS_NOT_LOCKED
        defaultEmployeeShouldBeFound("isNotLocked.in=" + DEFAULT_IS_NOT_LOCKED + "," + UPDATED_IS_NOT_LOCKED);

        // Get all the employeeList where isNotLocked equals to UPDATED_IS_NOT_LOCKED
        defaultEmployeeShouldNotBeFound("isNotLocked.in=" + UPDATED_IS_NOT_LOCKED);
    }

    @Test
    @Transactional
    void getAllEmployeesByIsNotLockedIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where isNotLocked is not null
        defaultEmployeeShouldBeFound("isNotLocked.specified=true");

        // Get all the employeeList where isNotLocked is null
        defaultEmployeeShouldNotBeFound("isNotLocked.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired equals to DEFAULT_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.equals=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired equals to UPDATED_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.equals=" + UPDATED_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired not equals to DEFAULT_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.notEquals=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired not equals to UPDATED_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.notEquals=" + UPDATED_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired in DEFAULT_DATE_HIRED or UPDATED_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.in=" + DEFAULT_DATE_HIRED + "," + UPDATED_DATE_HIRED);

        // Get all the employeeList where dateHired equals to UPDATED_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.in=" + UPDATED_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired is not null
        defaultEmployeeShouldBeFound("dateHired.specified=true");

        // Get all the employeeList where dateHired is null
        defaultEmployeeShouldNotBeFound("dateHired.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired is greater than or equal to DEFAULT_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.greaterThanOrEqual=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired is greater than or equal to UPDATED_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.greaterThanOrEqual=" + UPDATED_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired is less than or equal to DEFAULT_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.lessThanOrEqual=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired is less than or equal to SMALLER_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.lessThanOrEqual=" + SMALLER_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired is less than DEFAULT_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.lessThan=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired is less than UPDATED_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.lessThan=" + UPDATED_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired is greater than DEFAULT_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.greaterThan=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired is greater than SMALLER_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.greaterThan=" + SMALLER_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno equals to DEFAULT_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.equals=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno equals to UPDATED_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.equals=" + UPDATED_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno not equals to DEFAULT_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.notEquals=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno not equals to UPDATED_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.notEquals=" + UPDATED_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno in DEFAULT_DATE_DENO or UPDATED_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.in=" + DEFAULT_DATE_DENO + "," + UPDATED_DATE_DENO);

        // Get all the employeeList where dateDeno equals to UPDATED_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.in=" + UPDATED_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno is not null
        defaultEmployeeShouldBeFound("dateDeno.specified=true");

        // Get all the employeeList where dateDeno is null
        defaultEmployeeShouldNotBeFound("dateDeno.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno is greater than or equal to DEFAULT_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.greaterThanOrEqual=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno is greater than or equal to UPDATED_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.greaterThanOrEqual=" + UPDATED_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno is less than or equal to DEFAULT_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.lessThanOrEqual=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno is less than or equal to SMALLER_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.lessThanOrEqual=" + SMALLER_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno is less than DEFAULT_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.lessThan=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno is less than UPDATED_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.lessThan=" + UPDATED_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno is greater than DEFAULT_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.greaterThan=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno is greater than SMALLER_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.greaterThan=" + SMALLER_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit equals to DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.equals=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.equals=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit not equals to DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.notEquals=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit not equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.notEquals=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit in DEFAULT_SICK_LEAVE_YEARLY_CREDIT or UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound(
            "sickLeaveYearlyCredit.in=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT + "," + UPDATED_SICK_LEAVE_YEARLY_CREDIT
        );

        // Get all the employeeList where sickLeaveYearlyCredit equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.in=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit is not null
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.specified=true");

        // Get all the employeeList where sickLeaveYearlyCredit is null
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit is greater than or equal to DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.greaterThanOrEqual=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit is greater than or equal to UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.greaterThanOrEqual=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit is less than or equal to DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.lessThanOrEqual=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit is less than or equal to SMALLER_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.lessThanOrEqual=" + SMALLER_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit is less than DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.lessThan=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit is less than UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.lessThan=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit is greater than DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.greaterThan=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit is greater than SMALLER_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.greaterThan=" + SMALLER_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed equals to DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.equals=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.equals=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed not equals to DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.notEquals=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed not equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.notEquals=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed in DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED or UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound(
            "sickLeaveYearlyCreditUsed.in=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED + "," + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        );

        // Get all the employeeList where sickLeaveYearlyCreditUsed equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.in=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is not null
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.specified=true");

        // Get all the employeeList where sickLeaveYearlyCreditUsed is null
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is greater than or equal to DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.greaterThanOrEqual=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is greater than or equal to UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.greaterThanOrEqual=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is less than or equal to DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.lessThanOrEqual=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is less than or equal to SMALLER_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.lessThanOrEqual=" + SMALLER_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is less than DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.lessThan=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is less than UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.lessThan=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is greater than DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.greaterThan=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is greater than SMALLER_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.greaterThan=" + SMALLER_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit equals to DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.equals=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit equals to UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.equals=" + UPDATED_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit not equals to DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.notEquals=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit not equals to UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.notEquals=" + UPDATED_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit in DEFAULT_LEAVE_YEARLY_CREDIT or UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.in=" + DEFAULT_LEAVE_YEARLY_CREDIT + "," + UPDATED_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit equals to UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.in=" + UPDATED_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit is not null
        defaultEmployeeShouldBeFound("leaveYearlyCredit.specified=true");

        // Get all the employeeList where leaveYearlyCredit is null
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit is greater than or equal to DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.greaterThanOrEqual=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit is greater than or equal to UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.greaterThanOrEqual=" + UPDATED_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit is less than or equal to DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.lessThanOrEqual=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit is less than or equal to SMALLER_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.lessThanOrEqual=" + SMALLER_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit is less than DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.lessThan=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit is less than UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.lessThan=" + UPDATED_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit is greater than DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.greaterThan=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit is greater than SMALLER_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.greaterThan=" + SMALLER_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed equals to DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.equals=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed equals to UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.equals=" + UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed not equals to DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.notEquals=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed not equals to UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.notEquals=" + UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed in DEFAULT_LEAVE_YEARLY_CREDIT_USED or UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound(
            "leaveYearlyCreditUsed.in=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED + "," + UPDATED_LEAVE_YEARLY_CREDIT_USED
        );

        // Get all the employeeList where leaveYearlyCreditUsed equals to UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.in=" + UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed is not null
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.specified=true");

        // Get all the employeeList where leaveYearlyCreditUsed is null
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed is greater than or equal to DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.greaterThanOrEqual=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed is greater than or equal to UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.greaterThanOrEqual=" + UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed is less than or equal to DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.lessThanOrEqual=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed is less than or equal to SMALLER_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.lessThanOrEqual=" + SMALLER_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed is less than DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.lessThan=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed is less than UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.lessThan=" + UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed is greater than DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.greaterThan=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed is greater than SMALLER_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.greaterThan=" + SMALLER_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = employee.getUser();
        employeeRepository.saveAndFlush(employee);
        Long userId = user.getId();

        // Get all the employeeList where user equals to userId
        defaultEmployeeShouldBeFound("userId.equals=" + userId);

        // Get all the employeeList where user equals to (userId + 1)
        defaultEmployeeShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Position position = PositionResourceIT.createEntity(em);
        em.persist(position);
        em.flush();
        employee.addPosition(position);
        employeeRepository.saveAndFlush(employee);
        Long positionId = position.getId();

        // Get all the employeeList where position equals to positionId
        defaultEmployeeShouldBeFound("positionId.equals=" + positionId);

        // Get all the employeeList where position equals to (positionId + 1)
        defaultEmployeeShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByDutyScheduleIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        DutySchedule dutySchedule = DutyScheduleResourceIT.createEntity(em);
        em.persist(dutySchedule);
        em.flush();
        employee.addDutySchedule(dutySchedule);
        employeeRepository.saveAndFlush(employee);
        Long dutyScheduleId = dutySchedule.getId();

        // Get all the employeeList where dutySchedule equals to dutyScheduleId
        defaultEmployeeShouldBeFound("dutyScheduleId.equals=" + dutyScheduleId);

        // Get all the employeeList where dutySchedule equals to (dutyScheduleId + 1)
        defaultEmployeeShouldNotBeFound("dutyScheduleId.equals=" + (dutyScheduleId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByDailyTimeRecordIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        DailyTimeRecord dailyTimeRecord = DailyTimeRecordResourceIT.createEntity(em);
        em.persist(dailyTimeRecord);
        em.flush();
        employee.addDailyTimeRecord(dailyTimeRecord);
        employeeRepository.saveAndFlush(employee);
        Long dailyTimeRecordId = dailyTimeRecord.getId();

        // Get all the employeeList where dailyTimeRecord equals to dailyTimeRecordId
        defaultEmployeeShouldBeFound("dailyTimeRecordId.equals=" + dailyTimeRecordId);

        // Get all the employeeList where dailyTimeRecord equals to (dailyTimeRecordId + 1)
        defaultEmployeeShouldNotBeFound("dailyTimeRecordId.equals=" + (dailyTimeRecordId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByBenefitsIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Benefits benefits = BenefitsResourceIT.createEntity(em);
        em.persist(benefits);
        em.flush();
        employee.addBenefits(benefits);
        employeeRepository.saveAndFlush(employee);
        Long benefitsId = benefits.getId();

        // Get all the employeeList where benefits equals to benefitsId
        defaultEmployeeShouldBeFound("benefitsId.equals=" + benefitsId);

        // Get all the employeeList where benefits equals to (benefitsId + 1)
        defaultEmployeeShouldNotBeFound("benefitsId.equals=" + (benefitsId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByDependentsIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Dependents dependents = DependentsResourceIT.createEntity(em);
        em.persist(dependents);
        em.flush();
        employee.addDependents(dependents);
        employeeRepository.saveAndFlush(employee);
        Long dependentsId = dependents.getId();

        // Get all the employeeList where dependents equals to dependentsId
        defaultEmployeeShouldBeFound("dependentsId.equals=" + dependentsId);

        // Get all the employeeList where dependents equals to (dependentsId + 1)
        defaultEmployeeShouldNotBeFound("dependentsId.equals=" + (dependentsId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByEducationIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Education education = EducationResourceIT.createEntity(em);
        em.persist(education);
        em.flush();
        employee.addEducation(education);
        employeeRepository.saveAndFlush(employee);
        Long educationId = education.getId();

        // Get all the employeeList where education equals to educationId
        defaultEmployeeShouldBeFound("educationId.equals=" + educationId);

        // Get all the employeeList where education equals to (educationId + 1)
        defaultEmployeeShouldNotBeFound("educationId.equals=" + (educationId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByTrainingHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        TrainingHistory trainingHistory = TrainingHistoryResourceIT.createEntity(em);
        em.persist(trainingHistory);
        em.flush();
        employee.addTrainingHistory(trainingHistory);
        employeeRepository.saveAndFlush(employee);
        Long trainingHistoryId = trainingHistory.getId();

        // Get all the employeeList where trainingHistory equals to trainingHistoryId
        defaultEmployeeShouldBeFound("trainingHistoryId.equals=" + trainingHistoryId);

        // Get all the employeeList where trainingHistory equals to (trainingHistoryId + 1)
        defaultEmployeeShouldNotBeFound("trainingHistoryId.equals=" + (trainingHistoryId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Leave leave = LeaveResourceIT.createEntity(em);
        em.persist(leave);
        em.flush();
        employee.addLeave(leave);
        employeeRepository.saveAndFlush(employee);
        Long leaveId = leave.getId();

        // Get all the employeeList where leave equals to leaveId
        defaultEmployeeShouldBeFound("leaveId.equals=" + leaveId);

        // Get all the employeeList where leave equals to (leaveId + 1)
        defaultEmployeeShouldNotBeFound("leaveId.equals=" + (leaveId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Department department = DepartmentResourceIT.createEntity(em);
        em.persist(department);
        em.flush();
        employee.setDepartment(department);
        employeeRepository.saveAndFlush(employee);
        Long departmentId = department.getId();

        // Get all the employeeList where department equals to departmentId
        defaultEmployeeShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the employeeList where department equals to (departmentId + 1)
        defaultEmployeeShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        EmploymentType employmentType = EmploymentTypeResourceIT.createEntity(em);
        em.persist(employmentType);
        em.flush();
        employee.setEmploymentType(employmentType);
        employeeRepository.saveAndFlush(employee);
        Long employmentTypeId = employmentType.getId();

        // Get all the employeeList where employmentType equals to employmentTypeId
        defaultEmployeeShouldBeFound("employmentTypeId.equals=" + employmentTypeId);

        // Get all the employeeList where employmentType equals to (employmentTypeId + 1)
        defaultEmployeeShouldNotBeFound("employmentTypeId.equals=" + (employmentTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeShouldBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].nameSuffix").value(hasItem(DEFAULT_NAME_SUFFIX)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.booleanValue())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isNotLocked").value(hasItem(DEFAULT_IS_NOT_LOCKED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateHired").value(hasItem(DEFAULT_DATE_HIRED.toString())))
            .andExpect(jsonPath("$.[*].dateDeno").value(hasItem(DEFAULT_DATE_DENO.toString())))
            .andExpect(jsonPath("$.[*].sickLeaveYearlyCredit").value(hasItem(DEFAULT_SICK_LEAVE_YEARLY_CREDIT)))
            .andExpect(jsonPath("$.[*].sickLeaveYearlyCreditUsed").value(hasItem(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED)))
            .andExpect(jsonPath("$.[*].leaveYearlyCredit").value(hasItem(DEFAULT_LEAVE_YEARLY_CREDIT)))
            .andExpect(jsonPath("$.[*].leaveYearlyCreditUsed").value(hasItem(DEFAULT_LEAVE_YEARLY_CREDIT_USED)));

        // Check, that the count call also returns 1
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeShouldNotBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .employeeId(UPDATED_EMPLOYEE_ID)
            .username(UPDATED_USERNAME)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .nameSuffix(UPDATED_NAME_SUFFIX)
            .birthdate(UPDATED_BIRTHDATE)
            .sex(UPDATED_SEX)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .email(UPDATED_EMAIL)
            .isNotLocked(UPDATED_IS_NOT_LOCKED)
            .dateHired(UPDATED_DATE_HIRED)
            .dateDeno(UPDATED_DATE_DENO)
            .sickLeaveYearlyCredit(UPDATED_SICK_LEAVE_YEARLY_CREDIT)
            .sickLeaveYearlyCreditUsed(UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED)
            .leaveYearlyCredit(UPDATED_LEAVE_YEARLY_CREDIT)
            .leaveYearlyCreditUsed(UPDATED_LEAVE_YEARLY_CREDIT_USED);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testEmployee.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getNameSuffix()).isEqualTo(UPDATED_NAME_SUFFIX);
        assertThat(testEmployee.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testEmployee.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testEmployee.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getIsNotLocked()).isEqualTo(UPDATED_IS_NOT_LOCKED);
        assertThat(testEmployee.getDateHired()).isEqualTo(UPDATED_DATE_HIRED);
        assertThat(testEmployee.getDateDeno()).isEqualTo(UPDATED_DATE_DENO);
        assertThat(testEmployee.getSickLeaveYearlyCredit()).isEqualTo(UPDATED_SICK_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getSickLeaveYearlyCreditUsed()).isEqualTo(UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getLeaveYearlyCredit()).isEqualTo(UPDATED_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getLeaveYearlyCreditUsed()).isEqualTo(UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .username(UPDATED_USERNAME)
            .firstName(UPDATED_FIRST_NAME)
            .birthdate(UPDATED_BIRTHDATE)
            .sex(UPDATED_SEX)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .email(UPDATED_EMAIL)
            .sickLeaveYearlyCredit(UPDATED_SICK_LEAVE_YEARLY_CREDIT);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testEmployee.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getNameSuffix()).isEqualTo(DEFAULT_NAME_SUFFIX);
        assertThat(testEmployee.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testEmployee.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testEmployee.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getIsNotLocked()).isEqualTo(DEFAULT_IS_NOT_LOCKED);
        assertThat(testEmployee.getDateHired()).isEqualTo(DEFAULT_DATE_HIRED);
        assertThat(testEmployee.getDateDeno()).isEqualTo(DEFAULT_DATE_DENO);
        assertThat(testEmployee.getSickLeaveYearlyCredit()).isEqualTo(UPDATED_SICK_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getSickLeaveYearlyCreditUsed()).isEqualTo(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getLeaveYearlyCredit()).isEqualTo(DEFAULT_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getLeaveYearlyCreditUsed()).isEqualTo(DEFAULT_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .employeeId(UPDATED_EMPLOYEE_ID)
            .username(UPDATED_USERNAME)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .nameSuffix(UPDATED_NAME_SUFFIX)
            .birthdate(UPDATED_BIRTHDATE)
            .sex(UPDATED_SEX)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .email(UPDATED_EMAIL)
            .isNotLocked(UPDATED_IS_NOT_LOCKED)
            .dateHired(UPDATED_DATE_HIRED)
            .dateDeno(UPDATED_DATE_DENO)
            .sickLeaveYearlyCredit(UPDATED_SICK_LEAVE_YEARLY_CREDIT)
            .sickLeaveYearlyCreditUsed(UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED)
            .leaveYearlyCredit(UPDATED_LEAVE_YEARLY_CREDIT)
            .leaveYearlyCreditUsed(UPDATED_LEAVE_YEARLY_CREDIT_USED);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testEmployee.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getNameSuffix()).isEqualTo(UPDATED_NAME_SUFFIX);
        assertThat(testEmployee.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testEmployee.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testEmployee.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getIsNotLocked()).isEqualTo(UPDATED_IS_NOT_LOCKED);
        assertThat(testEmployee.getDateHired()).isEqualTo(UPDATED_DATE_HIRED);
        assertThat(testEmployee.getDateDeno()).isEqualTo(UPDATED_DATE_DENO);
        assertThat(testEmployee.getSickLeaveYearlyCredit()).isEqualTo(UPDATED_SICK_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getSickLeaveYearlyCreditUsed()).isEqualTo(UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getLeaveYearlyCredit()).isEqualTo(UPDATED_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getLeaveYearlyCreditUsed()).isEqualTo(UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
