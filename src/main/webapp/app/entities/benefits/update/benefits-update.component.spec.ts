jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BenefitsService } from '../service/benefits.service';
import { IBenefits, Benefits } from '../benefits.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { BenefitsUpdateComponent } from './benefits-update.component';

describe('Component Tests', () => {
  describe('Benefits Management Update Component', () => {
    let comp: BenefitsUpdateComponent;
    let fixture: ComponentFixture<BenefitsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let benefitsService: BenefitsService;
    let employeeService: EmployeeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BenefitsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BenefitsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BenefitsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      benefitsService = TestBed.inject(BenefitsService);
      employeeService = TestBed.inject(EmployeeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Employee query and add missing value', () => {
        const benefits: IBenefits = { id: 456 };
        const employee: IEmployee = { id: 68742 };
        benefits.employee = employee;

        const employeeCollection: IEmployee[] = [{ id: 25679 }];
        jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
        const additionalEmployees = [employee];
        const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
        jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ benefits });
        comp.ngOnInit();

        expect(employeeService.query).toHaveBeenCalled();
        expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, ...additionalEmployees);
        expect(comp.employeesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const benefits: IBenefits = { id: 456 };
        const employee: IEmployee = { id: 82123 };
        benefits.employee = employee;

        activatedRoute.data = of({ benefits });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(benefits));
        expect(comp.employeesSharedCollection).toContain(employee);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Benefits>>();
        const benefits = { id: 123 };
        jest.spyOn(benefitsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ benefits });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: benefits }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(benefitsService.update).toHaveBeenCalledWith(benefits);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Benefits>>();
        const benefits = new Benefits();
        jest.spyOn(benefitsService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ benefits });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: benefits }));
        saveSubject.complete();

        // THEN
        expect(benefitsService.create).toHaveBeenCalledWith(benefits);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Benefits>>();
        const benefits = { id: 123 };
        jest.spyOn(benefitsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ benefits });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(benefitsService.update).toHaveBeenCalledWith(benefits);
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
