import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBenefits, Benefits } from '../benefits.model';
import { BenefitsService } from '../service/benefits.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-benefits-update',
  templateUrl: './benefits-update.component.html',
})
export class BenefitsUpdateComponent implements OnInit {
  isSaving = false;

  employeesSharedCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.maxLength(60)]],
    employee: [],
  });

  constructor(
    protected benefitsService: BenefitsService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ benefits }) => {
      this.updateForm(benefits);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const benefits = this.createFromForm();
    if (benefits.id !== undefined) {
      this.subscribeToSaveResponse(this.benefitsService.update(benefits));
    } else {
      this.subscribeToSaveResponse(this.benefitsService.create(benefits));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBenefits>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(benefits: IBenefits): void {
    this.editForm.patchValue({
      id: benefits.id,
      name: benefits.name,
      employee: benefits.employee,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      benefits.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('employee')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }

  protected createFromForm(): IBenefits {
    return {
      ...new Benefits(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
