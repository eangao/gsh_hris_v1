jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PositionService } from '../service/position.service';
import { IPosition, Position } from '../position.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { PositionUpdateComponent } from './position-update.component';

describe('Component Tests', () => {
  describe('Position Management Update Component', () => {
    let comp: PositionUpdateComponent;
    let fixture: ComponentFixture<PositionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let positionService: PositionService;
    let employeeService: EmployeeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PositionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PositionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PositionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      positionService = TestBed.inject(PositionService);
      employeeService = TestBed.inject(EmployeeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Employee query and add missing value', () => {
        const position: IPosition = { id: 456 };
        const employee: IEmployee = { id: 54217 };
        position.employee = employee;

        const employeeCollection: IEmployee[] = [{ id: 80971 }];
        jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
        const additionalEmployees = [employee];
        const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
        jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ position });
        comp.ngOnInit();

        expect(employeeService.query).toHaveBeenCalled();
        expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, ...additionalEmployees);
        expect(comp.employeesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const position: IPosition = { id: 456 };
        const employee: IEmployee = { id: 28452 };
        position.employee = employee;

        activatedRoute.data = of({ position });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(position));
        expect(comp.employeesSharedCollection).toContain(employee);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Position>>();
        const position = { id: 123 };
        jest.spyOn(positionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ position });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: position }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(positionService.update).toHaveBeenCalledWith(position);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Position>>();
        const position = new Position();
        jest.spyOn(positionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ position });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: position }));
        saveSubject.complete();

        // THEN
        expect(positionService.create).toHaveBeenCalledWith(position);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Position>>();
        const position = { id: 123 };
        jest.spyOn(positionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ position });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(positionService.update).toHaveBeenCalledWith(position);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEmployeeById', () => {
        it('Should return tracked Employee primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEmployeeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
