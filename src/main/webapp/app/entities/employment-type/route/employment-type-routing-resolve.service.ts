import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmploymentType, EmploymentType } from '../employment-type.model';
import { EmploymentTypeService } from '../service/employment-type.service';

@Injectable({ providedIn: 'root' })
export class EmploymentTypeRoutingResolveService implements Resolve<IEmploymentType> {
  constructor(protected service: EmploymentTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmploymentType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employmentType: HttpResponse<EmploymentType>) => {
          if (employmentType.body) {
            return of(employmentType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmploymentType());
  }
}
