import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDailyTimeRecord, DailyTimeRecord } from '../daily-time-record.model';
import { DailyTimeRecordService } from '../service/daily-time-record.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-daily-time-record-update',
  templateUrl: './daily-time-record-update.component.html',
})
export class DailyTimeRecordUpdateComponent implements OnInit {
  isSaving = false;

  employeesSharedCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    dateTimeIn: [],
    dateTimeOut: [],
    employee: [],
  });

  constructor(
    protected dailyTimeRecordService: DailyTimeRecordService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dailyTimeRecord }) => {
      if (dailyTimeRecord.id === undefined) {
        const today = dayjs().startOf('day');
        dailyTimeRecord.dateTimeIn = today;
        dailyTimeRecord.dateTimeOut = today;
      }

      this.updateForm(dailyTimeRecord);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dailyTimeRecord = this.createFromForm();
    if (dailyTimeRecord.id !== undefined) {
      this.subscribeToSaveResponse(this.dailyTimeRecordService.update(dailyTimeRecord));
    } else {
      this.subscribeToSaveResponse(this.dailyTimeRecordService.create(dailyTimeRecord));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDailyTimeRecord>>): void {
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

  protected updateForm(dailyTimeRecord: IDailyTimeRecord): void {
    this.editForm.patchValue({
      id: dailyTimeRecord.id,
      dateTimeIn: dailyTimeRecord.dateTimeIn ? dailyTimeRecord.dateTimeIn.format(DATE_TIME_FORMAT) : null,
      dateTimeOut: dailyTimeRecord.dateTimeOut ? dailyTimeRecord.dateTimeOut.format(DATE_TIME_FORMAT) : null,
      employee: dailyTimeRecord.employee,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      dailyTimeRecord.employee
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

  protected createFromForm(): IDailyTimeRecord {
    return {
      ...new DailyTimeRecord(),
      id: this.editForm.get(['id'])!.value,
      dateTimeIn: this.editForm.get(['dateTimeIn'])!.value ? dayjs(this.editForm.get(['dateTimeIn'])!.value, DATE_TIME_FORMAT) : undefined,
      dateTimeOut: this.editForm.get(['dateTimeOut'])!.value
        ? dayjs(this.editForm.get(['dateTimeOut'])!.value, DATE_TIME_FORMAT)
        : undefined,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
