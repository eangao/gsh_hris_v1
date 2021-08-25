import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize} from 'rxjs/operators';

import {EmploymentType, IEmploymentType} from '../employment-type.model';
import {EmploymentTypeService} from '../service/employment-type.service';

@Component({
  selector: 'jhi-employment-type-update',
  templateUrl: './employment-type-update.component.html',
})
export class EmploymentTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.maxLength(50)]],
  });

  constructor(
    protected employmentTypeService: EmploymentTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentType }) => {
      this.updateForm(employmentType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employmentType = this.createFromForm();
    if (employmentType.id !== undefined) {
      this.subscribeToSaveResponse(this.employmentTypeService.update(employmentType));
    } else {
      this.subscribeToSaveResponse(this.employmentTypeService.create(employmentType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmploymentType>>): void {
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

  protected updateForm(employmentType: IEmploymentType): void {
    this.editForm.patchValue({
      id: employmentType.id,
      name: employmentType.name,
    });
  }

  protected createFromForm(): IEmploymentType {
    return {
      ...new EmploymentType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
