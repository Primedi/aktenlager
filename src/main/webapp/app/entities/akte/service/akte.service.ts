import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAkte, getAkteIdentifier } from '../akte.model';

export type EntityResponseType = HttpResponse<IAkte>;
export type EntityArrayResponseType = HttpResponse<IAkte[]>;

@Injectable({ providedIn: 'root' })
export class AkteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aktes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(akte: IAkte): Observable<EntityResponseType> {
    return this.http.post<IAkte>(this.resourceUrl, akte, { observe: 'response' });
  }

  update(akte: IAkte): Observable<EntityResponseType> {
    return this.http.put<IAkte>(`${this.resourceUrl}/${getAkteIdentifier(akte) as number}`, akte, { observe: 'response' });
  }

  partialUpdate(akte: IAkte): Observable<EntityResponseType> {
    return this.http.patch<IAkte>(`${this.resourceUrl}/${getAkteIdentifier(akte) as number}`, akte, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAkte>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAkte[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getGesamtmeter(): Observable<EntityResponseType> {
    return this.http.get<IAkte>(`${this.resourceUrl}/summe`, { observe: 'response' });
  }

  getHaengend(): Observable<EntityResponseType> {
    return this.http.get<IAkte>(`${this.resourceUrl}/haengend`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAkteToCollectionIfMissing(akteCollection: IAkte[], ...aktesToCheck: (IAkte | null | undefined)[]): IAkte[] {
    const aktes: IAkte[] = aktesToCheck.filter(isPresent);
    if (aktes.length > 0) {
      const akteCollectionIdentifiers = akteCollection.map(akteItem => getAkteIdentifier(akteItem)!);
      const aktesToAdd = aktes.filter(akteItem => {
        const akteIdentifier = getAkteIdentifier(akteItem);
        if (akteIdentifier == null || akteCollectionIdentifiers.includes(akteIdentifier)) {
          return false;
        }
        akteCollectionIdentifiers.push(akteIdentifier);
        return true;
      });
      return [...aktesToAdd, ...akteCollection];
    }
    return akteCollection;
  }
}
