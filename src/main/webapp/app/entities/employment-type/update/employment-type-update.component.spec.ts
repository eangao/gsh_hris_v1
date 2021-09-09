jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EmploymentTypeService } from '../service/employment-type.service';
import { IEmploymentType, EmploymentType } from '../employment-type.model';

import { EmploymentTypeUpdateComponent } from './employment-type-update.component';

describe('Component Tests', () => {
  describe('EmploymentType Management Update Component', () => {
    let comp: EmploymentTypeUpdateComponent;
    let fixture: ComponentFixture<EmploymentTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let employmentTypeService: EmploymentTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EmploymentTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EmploymentTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmploymentTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      employmentTypeService = TestBed.inject(EmploymentTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const employmentType: IEmploymentType = { id: 456 };

        activatedRoute.data = of({ employmentType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(employmentType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EmploymentType>>();
        const employmentType = { id: 123 };
        jest.spyOn(employmentTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ employmentType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: employmentType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(employmentTypeService.update).toHaveBeenCalledWith(employmentType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EmploymentType>>();
        const employmentType = new EmploymentType();
        jest.spyOn(employmentTypeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ employmentType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: employmentType }));
        saveSubject.complete();

        // THEN
        expect(employmentTypeService.create).toHaveBeenCalledWith(employmentType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EmploymentType>>();
        const employmentType = { id: 123 };
        jest.spyOn(employmentTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ employmentType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(employmentTypeService.update).toHaveBeenCalledWith(employmentType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
