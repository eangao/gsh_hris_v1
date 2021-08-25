import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'employee',
        data: { pageTitle: 'Employees' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'cluster',
        data: { pageTitle: 'Clusters' },
        loadChildren: () => import('./cluster/cluster.module').then(m => m.ClusterModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'Departments' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'employment-type',
        data: { pageTitle: 'EmploymentTypes' },
        loadChildren: () => import('./employment-type/employment-type.module').then(m => m.EmploymentTypeModule),
      },
      {
        path: 'position',
        data: { pageTitle: 'Positions' },
        loadChildren: () => import('./position/position.module').then(m => m.PositionModule),
      },
      {
        path: 'duty-schedule',
        data: { pageTitle: 'DutySchedules' },
        loadChildren: () => import('./duty-schedule/duty-schedule.module').then(m => m.DutyScheduleModule),
      },
      {
        path: 'daily-time-record',
        data: { pageTitle: 'DailyTimeRecords' },
        loadChildren: () => import('./daily-time-record/daily-time-record.module').then(m => m.DailyTimeRecordModule),
      },
      {
        path: 'benefits',
        data: { pageTitle: 'Benefits' },
        loadChildren: () => import('./benefits/benefits.module').then(m => m.BenefitsModule),
      },
      {
        path: 'dependents',
        data: { pageTitle: 'Dependents' },
        loadChildren: () => import('./dependents/dependents.module').then(m => m.DependentsModule),
      },
      {
        path: 'education',
        data: { pageTitle: 'Educations' },
        loadChildren: () => import('./education/education.module').then(m => m.EducationModule),
      },
      {
        path: 'training-history',
        data: { pageTitle: 'TrainingHistories' },
        loadChildren: () => import('./training-history/training-history.module').then(m => m.TrainingHistoryModule),
      },
      {
        path: 'leave',
        data: { pageTitle: 'Leaves' },
        loadChildren: () => import('./leave/leave.module').then(m => m.LeaveModule),
      },
      {
        path: 'leave-type',
        data: { pageTitle: 'LeaveTypes' },
        loadChildren: () => import('./leave-type/leave-type.module').then(m => m.LeaveTypeModule),
      },
      {
        path: 'holiday',
        data: { pageTitle: 'Holidays' },
        loadChildren: () => import('./holiday/holiday.module').then(m => m.HolidayModule),
      },
      {
        path: 'holiday-type',
        data: { pageTitle: 'HolidayTypes' },
        loadChildren: () => import('./holiday-type/holiday-type.module').then(m => m.HolidayTypeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
