jest.mock('@angular/router');

import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpResponse} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {of, Subject} from 'rxjs';

import {EmployeeService} from '../service/employee.service';
import {Employee, IEmployee} from '../employee.model';

import {IUser} from 'app/entities/user/user.model';
import {UserService} from 'app/entities/user/user.service';
import {IDepartment} from 'app/entities/department/department.model';
import {DepartmentService} from 'app/entities/department/service/department.service';
import {IEmploymentType} from 'app/entities/employment-type/employment-type.model';
import {EmploymentTypeService} from 'app/entities/employment-type/service/employment-type.service';

import {EmployeeUpdateComponent} from './employee-update.component';

describe('Component Tests', () => {
  describe('Employee Management Update Component', () => {
    let comp: EmployeeUpdateComponent;
    let fixture: ComponentFixture<EmployeeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let employeeService: EmployeeService;
    let userService: UserService;
    let departmentService: DepartmentService;
    let employmentTypeService: EmploymentTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EmployeeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EmployeeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmployeeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      employeeService = TestBed.inject(EmployeeService);
      userService = TestBed.inject(UserService);
      departmentService = TestBed.inject(DepartmentService);
      employmentTypeService = TestBed.inject(EmploymentTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const employee: IEmployee = { id: 456 };
        const user: IUser = { id: 84478 };
        employee.user = user;

        const userCollection: IUser[] = [{ id: 1447 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ employee });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Department query and add missing value', () => {
        const employee: IEmployee = { id: 456 };
        const department: IDepartment = { id: 35363 };
        employee.department = department;

        const departmentCollection: IDepartment[] = [{ id: 78278 }];
        jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
        const additionalDepartments = [department];
        const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
        jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ employee });
        comp.ngOnInit();

        expect(departmentService.query).toHaveBeenCalled();
        expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(departmentCollection, ...additionalDepartments);
        expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call EmploymentType query and add missing value', () => {
        const employee: IEmployee = { id: 456 };
        const employmentType: IEmploymentType = { id: 53601 };
        employee.employmentType = employmentType;

        const employmentTypeCollection: IEmploymentType[] = [{ id: 3465 }];
        jest.spyOn(employmentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: employmentTypeCollection })));
        const additionalEmploymentTypes = [employmentType];
        const expectedCollection: IEmploymentType[] = [...additionalEmploymentTypes, ...employmentTypeCollection];
        jest.spyOn(employmentTypeService, 'addEmploymentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ employee });
        comp.ngOnInit();

        expect(employmentTypeService.query).toHaveBeenCalled();
        expect(employmentTypeService.addEmploymentTypeToCollectionIfMissing).toHaveBeenCalledWith(
          employmentTypeCollection,
          ...additionalEmploymentTypes
        );
        expect(comp.employmentTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const employee: IEmployee = { id: 456 };
        const user: IUser = { id: 66698 };
        employee.user = user;
        const department: IDepartment = { id: 60127 };
        employee.department = department;
        const employmentType: IEmploymentType = { id: 95120 };
        employee.employmentType = employmentType;

        activatedRoute.data = of({ employee });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(employee));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.departmentsSharedCollection).toContain(department);
        expect(comp.employmentTypesSharedCollection).toContain(employmentType);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Employee>>();
        const employee = { id: 123 };
        jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ employee });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: employee }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(employeeService.update).toHaveBeenCalledWith(employee);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Employee>>();
        const employee = new Employee();
        jest.spyOn(employeeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ employee });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: employee }));
        saveSubject.complete();

        // THEN
        expect(employeeService.create).toHaveBeenCalledWith(employee);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Employee>>();
        const employee = { id: 123 };
        jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ employee });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(employeeService.update).toHaveBeenCalledWith(employee);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDepartmentById', () => {
        it('Should return tracked Department primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDepartmentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEmploymentTypeById', () => {
        it('Should return tracked EmploymentType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEmploymentTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
