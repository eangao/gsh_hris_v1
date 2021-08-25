import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRouteSnapshot, Resolve, Router} from '@angular/router';
import {EMPTY, Observable, of} from 'rxjs';
import {mergeMap} from 'rxjs/operators';

import {Benefits, IBenefits} from '../benefits.model';
import {BenefitsService} from '../service/benefits.service';

@Injectable({ providedIn: 'root' })
export class BenefitsRoutingResolveService implements Resolve<IBenefits> {
  constructor(protected service: BenefitsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBenefits> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((benefits: HttpResponse<Benefits>) => {
          if (benefits.body) {
            return of(benefits.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Benefits());
  }
}
