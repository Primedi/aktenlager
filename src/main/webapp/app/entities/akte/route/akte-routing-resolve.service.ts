import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAkte, Akte } from '../akte.model';
import { AkteService } from '../service/akte.service';

@Injectable({ providedIn: 'root' })
export class AkteRoutingResolveService implements Resolve<IAkte> {
  constructor(protected service: AkteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAkte> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((akte: HttpResponse<Akte>) => {
          if (akte.body) {
            return of(akte.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Akte());
  }
}
