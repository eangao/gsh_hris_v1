import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRouteSnapshot, Resolve, Router} from '@angular/router';
import {EMPTY, Observable, of} from 'rxjs';
import {mergeMap} from 'rxjs/operators';

import {Cluster, ICluster} from '../cluster.model';
import {ClusterService} from '../service/cluster.service';

@Injectable({ providedIn: 'root' })
export class ClusterRoutingResolveService implements Resolve<ICluster> {
  constructor(protected service: ClusterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICluster> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cluster: HttpResponse<Cluster>) => {
          if (cluster.body) {
            return of(cluster.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cluster());
  }
}
