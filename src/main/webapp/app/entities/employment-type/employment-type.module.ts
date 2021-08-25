import {NgModule} from '@angular/core';
import {SharedModule} from 'app/shared/shared.module';
import {EmploymentTypeComponent} from './list/employment-type.component';
import {EmploymentTypeDetailComponent} from './detail/employment-type-detail.component';
import {EmploymentTypeUpdateComponent} from './update/employment-type-update.component';
import {EmploymentTypeDeleteDialogComponent} from './delete/employment-type-delete-dialog.component';
import {EmploymentTypeRoutingModule} from './route/employment-type-routing.module';

@NgModule({
  imports: [SharedModule, EmploymentTypeRoutingModule],
  declarations: [
    EmploymentTypeComponent,
    EmploymentTypeDetailComponent,
    EmploymentTypeUpdateComponent,
    EmploymentTypeDeleteDialogComponent,
  ],
  entryComponents: [EmploymentTypeDeleteDialogComponent],
})
export class EmploymentTypeModule {}
