import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmploymentType, getEmploymentTypeIdentifier } from '../employment-type.model';

export type EntityResponseType = HttpResponse<IEmploymentType>;
export type EntityArrayResponseType = HttpResponse<IEmploymentType[]>;

@Injectable({ providedIn: 'root' })
export class EmploymentTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employment-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employmentType: IEmploymentType): Observable<EntityResponseType> {
    return this.http.post<IEmploymentType>(this.resourceUrl, employmentType, { observe: 'response' });
  }

  update(employmentType: IEmploymentType): Observable<EntityResponseType> {
    return this.http.put<IEmploymentType>(`${this.resourceUrl}/${getEmploymentTypeIdentifier(employmentType) as number}`, employmentType, {
      observe: 'response',
    });
  }

  partialUpdate(employmentType: IEmploymentType): Observable<EntityResponseType> {
    return this.http.patch<IEmploymentType>(
      `${this.resourceUrl}/${getEmploymentTypeIdentifier(employmentType) as number}`,
      employmentType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmploymentType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmploymentType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEmploymentTypeToCollectionIfMissing(
    employmentTypeCollection: IEmploymentType[],
    ...employmentTypesToCheck: (IEmploymentType | null | undefined)[]
  ): IEmploymentType[] {
    const employmentTypes: IEmploymentType[] = employmentTypesToCheck.filter(isPresent);
    if (employmentTypes.length > 0) {
      const employmentTypeCollectionIdentifiers = employmentTypeCollection.map(
        employmentTypeItem => getEmploymentTypeIdentifier(employmentTypeItem)!
      );
      const employmentTypesToAdd = employmentTypes.filter(employmentTypeItem => {
        const employmentTypeIdentifier = getEmploymentTypeIdentifier(employmentTypeItem);
        if (employmentTypeIdentifier == null || employmentTypeCollectionIdentifiers.includes(employmentTypeIdentifier)) {
          return false;
        }
        employmentTypeCollectionIdentifiers.push(employmentTypeIdentifier);
        return true;
      });
      return [...employmentTypesToAdd, ...employmentTypeCollection];
    }
    return employmentTypeCollection;
  }
}
