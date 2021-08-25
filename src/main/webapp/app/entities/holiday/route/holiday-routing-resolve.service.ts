import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRouteSnapshot, Resolve, Router} from '@angular/router';
import {EMPTY, Observable, of} from 'rxjs';
import {mergeMap} from 'rxjs/operators';

import {Holiday, IHoliday} from '../holiday.model';
import {HolidayService} from '../service/holiday.service';

@Injectable({ providedIn: 'root' })
export class HolidayRoutingResolveService implements Resolve<IHoliday> {
  constructor(protected service: HolidayService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHoliday> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((holiday: HttpResponse<Holiday>) => {
          if (holiday.body) {
            return of(holiday.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Holiday());
  }
}
