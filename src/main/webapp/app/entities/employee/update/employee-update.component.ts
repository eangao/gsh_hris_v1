import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEmployee, Employee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IEmploymentType } from 'app/entities/employment-type/employment-type.model';
import { EmploymentTypeService } from 'app/entities/employment-type/service/employment-type.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  departmentsSharedCollection: IDepartment[] = [];
  employmentTypesSharedCollection: IEmploymentType[] = [];

  editForm = this.fb.group({
    id: [],
    employeeId: [null, [Validators.required]],
    username: [null, [Validators.required, Validators.minLength(6), Validators.maxLength(30)]],
    firstName: [null, [Validators.maxLength(50)]],
    middleName: [null, [Validators.maxLength(50)]],
    lastName: [null, [Validators.maxLength(50)]],
    nameSuffix: [null, [Validators.maxLength(15)]],
    birthdate: [],
    sex: [],
    mobileNumber: [],
    email: [null, [Validators.maxLength(50)]],
    isNotLocked: [],
    dateHired: [],
    dateDeno: [],
    sickLeaveYearlyCredit: [],
    sickLeaveYearlyCreditUsed: [],
    leaveYearlyCredit: [],
    leaveYearlyCreditUsed: [],
    user: [],
    department: [null, Validators.required],
    employmentType: [null, Validators.required],
  });

  constructor(
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected departmentService: DepartmentService,
    protected employmentTypeService: EmploymentTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackDepartmentById(index: number, item: IDepartment): number {
    return item.id!;
  }

  trackEmploymentTypeById(index: number, item: IEmploymentType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      employeeId: employee.employeeId,
      username: employee.username,
      firstName: employee.firstName,
      middleName: employee.middleName,
      lastName: employee.lastName,
      nameSuffix: employee.nameSuffix,
      birthdate: employee.birthdate,
      sex: employee.sex,
      mobileNumber: employee.mobileNumber,
      email: employee.email,
      isNotLocked: employee.isNotLocked,
      dateHired: employee.dateHired,
      dateDeno: employee.dateDeno,
      sickLeaveYearlyCredit: employee.sickLeaveYearlyCredit,
      sickLeaveYearlyCreditUsed: employee.sickLeaveYearlyCreditUsed,
      leaveYearlyCredit: employee.leaveYearlyCredit,
      leaveYearlyCreditUsed: employee.leaveYearlyCreditUsed,
      user: employee.user,
      department: employee.department,
      employmentType: employee.employmentType,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, employee.user);
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing(
      this.departmentsSharedCollection,
      employee.department
    );
    this.employmentTypesSharedCollection = this.employmentTypeService.addEmploymentTypeToCollectionIfMissing(
      this.employmentTypesSharedCollection,
      employee.employmentType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing(departments, this.editForm.get('department')!.value)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    this.employmentTypeService
      .query()
      .pipe(map((res: HttpResponse<IEmploymentType[]>) => res.body ?? []))
      .pipe(
        map((employmentTypes: IEmploymentType[]) =>
          this.employmentTypeService.addEmploymentTypeToCollectionIfMissing(employmentTypes, this.editForm.get('employmentType')!.value)
        )
      )
      .subscribe((employmentTypes: IEmploymentType[]) => (this.employmentTypesSharedCollection = employmentTypes));
  }

  protected createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
      username: this.editForm.get(['username'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      middleName: this.editForm.get(['middleName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      nameSuffix: this.editForm.get(['nameSuffix'])!.value,
      birthdate: this.editForm.get(['birthdate'])!.value,
      sex: this.editForm.get(['sex'])!.value,
      mobileNumber: this.editForm.get(['mobileNumber'])!.value,
      email: this.editForm.get(['email'])!.value,
      isNotLocked: this.editForm.get(['isNotLocked'])!.value,
      dateHired: this.editForm.get(['dateHired'])!.value,
      dateDeno: this.editForm.get(['dateDeno'])!.value,
      sickLeaveYearlyCredit: this.editForm.get(['sickLeaveYearlyCredit'])!.value,
      sickLeaveYearlyCreditUsed: this.editForm.get(['sickLeaveYearlyCreditUsed'])!.value,
      leaveYearlyCredit: this.editForm.get(['leaveYearlyCredit'])!.value,
      leaveYearlyCreditUsed: this.editForm.get(['leaveYearlyCreditUsed'])!.value,
      user: this.editForm.get(['user'])!.value,
      department: this.editForm.get(['department'])!.value,
      employmentType: this.editForm.get(['employmentType'])!.value,
    };
  }
}
