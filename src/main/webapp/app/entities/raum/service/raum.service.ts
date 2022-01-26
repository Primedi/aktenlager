import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRaum, getRaumIdentifier } from '../raum.model';

export type EntityResponseType = HttpResponse<IRaum>;
export type EntityArrayResponseType = HttpResponse<IRaum[]>;

@Injectable({ providedIn: 'root' })
export class RaumService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/raums');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(raum: IRaum): Observable<EntityResponseType> {
    return this.http.post<IRaum>(this.resourceUrl, raum, { observe: 'response' });
  }

  update(raum: IRaum): Observable<EntityResponseType> {
    return this.http.put<IRaum>(`${this.resourceUrl}/${getRaumIdentifier(raum) as number}`, raum, { observe: 'response' });
  }

  partialUpdate(raum: IRaum): Observable<EntityResponseType> {
    return this.http.patch<IRaum>(`${this.resourceUrl}/${getRaumIdentifier(raum) as number}`, raum, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRaum>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRaum[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRaumToCollectionIfMissing(raumCollection: IRaum[], ...raumsToCheck: (IRaum | null | undefined)[]): IRaum[] {
    const raums: IRaum[] = raumsToCheck.filter(isPresent);
    if (raums.length > 0) {
      const raumCollectionIdentifiers = raumCollection.map(raumItem => getRaumIdentifier(raumItem)!);
      const raumsToAdd = raums.filter(raumItem => {
        const raumIdentifier = getRaumIdentifier(raumItem);
        if (raumIdentifier == null || raumCollectionIdentifiers.includes(raumIdentifier)) {
          return false;
        }
        raumCollectionIdentifiers.push(raumIdentifier);
        return true;
      });
      return [...raumsToAdd, ...raumCollection];
    }
    return raumCollection;
  }
}
