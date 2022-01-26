import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRaum, Raum } from '../raum.model';
import { RaumService } from '../service/raum.service';

@Injectable({ providedIn: 'root' })
export class RaumRoutingResolveService implements Resolve<IRaum> {
  constructor(protected service: RaumService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRaum> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((raum: HttpResponse<Raum>) => {
          if (raum.body) {
            return of(raum.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Raum());
  }
}
