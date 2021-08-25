import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {UserRouteAccessService} from 'app/core/auth/user-route-access.service';
import {EmploymentTypeComponent} from '../list/employment-type.component';
import {EmploymentTypeDetailComponent} from '../detail/employment-type-detail.component';
import {EmploymentTypeUpdateComponent} from '../update/employment-type-update.component';
import {EmploymentTypeRoutingResolveService} from './employment-type-routing-resolve.service';

const employmentTypeRoute: Routes = [
  {
    path: '',
    component: EmploymentTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmploymentTypeDetailComponent,
    resolve: {
      employmentType: EmploymentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmploymentTypeUpdateComponent,
    resolve: {
      employmentType: EmploymentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmploymentTypeUpdateComponent,
    resolve: {
      employmentType: EmploymentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employmentTypeRoute)],
  exports: [RouterModule],
})
export class EmploymentTypeRoutingModule {}
