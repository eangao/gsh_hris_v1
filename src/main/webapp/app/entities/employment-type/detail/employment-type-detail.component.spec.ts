import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmploymentTypeDetailComponent } from './employment-type-detail.component';

describe('Component Tests', () => {
  describe('EmploymentType Management Detail Component', () => {
    let comp: EmploymentTypeDetailComponent;
    let fixture: ComponentFixture<EmploymentTypeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EmploymentTypeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ employmentType: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EmploymentTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmploymentTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load employmentType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.employmentType).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
