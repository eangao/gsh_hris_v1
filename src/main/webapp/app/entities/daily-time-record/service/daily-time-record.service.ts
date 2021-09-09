import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDailyTimeRecord, getDailyTimeRecordIdentifier } from '../daily-time-record.model';

export type EntityResponseType = HttpResponse<IDailyTimeRecord>;
export type EntityArrayResponseType = HttpResponse<IDailyTimeRecord[]>;

@Injectable({ providedIn: 'root' })
export class DailyTimeRecordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/daily-time-records');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dailyTimeRecord: IDailyTimeRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dailyTimeRecord);
    return this.http
      .post<IDailyTimeRecord>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dailyTimeRecord: IDailyTimeRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dailyTimeRecord);
    return this.http
      .put<IDailyTimeRecord>(`${this.resourceUrl}/${getDailyTimeRecordIdentifier(dailyTimeRecord) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dailyTimeRecord: IDailyTimeRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dailyTimeRecord);
    return this.http
      .patch<IDailyTimeRecord>(`${this.resourceUrl}/${getDailyTimeRecordIdentifier(dailyTimeRecord) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDailyTimeRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDailyTimeRecord[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDailyTimeRecordToCollectionIfMissing(
    dailyTimeRecordCollection: IDailyTimeRecord[],
    ...dailyTimeRecordsToCheck: (IDailyTimeRecord | null | undefined)[]
  ): IDailyTimeRecord[] {
    const dailyTimeRecords: IDailyTimeRecord[] = dailyTimeRecordsToCheck.filter(isPresent);
    if (dailyTimeRecords.length > 0) {
      const dailyTimeRecordCollectionIdentifiers = dailyTimeRecordCollection.map(
        dailyTimeRecordItem => getDailyTimeRecordIdentifier(dailyTimeRecordItem)!
      );
      const dailyTimeRecordsToAdd = dailyTimeRecords.filter(dailyTimeRecordItem => {
        const dailyTimeRecordIdentifier = getDailyTimeRecordIdentifier(dailyTimeRecordItem);
        if (dailyTimeRecordIdentifier == null || dailyTimeRecordCollectionIdentifiers.includes(dailyTimeRecordIdentifier)) {
          return false;
        }
        dailyTimeRecordCollectionIdentifiers.push(dailyTimeRecordIdentifier);
        return true;
      });
      return [...dailyTimeRecordsToAdd, ...dailyTimeRecordCollection];
    }
    return dailyTimeRecordCollection;
  }

  protected convertDateFromClient(dailyTimeRecord: IDailyTimeRecord): IDailyTimeRecord {
    return Object.assign({}, dailyTimeRecord, {
      dateTimeIn: dailyTimeRecord.dateTimeIn?.isValid() ? dailyTimeRecord.dateTimeIn.toJSON() : undefined,
      dateTimeOut: dailyTimeRecord.dateTimeOut?.isValid() ? dailyTimeRecord.dateTimeOut.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateTimeIn = res.body.dateTimeIn ? dayjs(res.body.dateTimeIn) : undefined;
      res.body.dateTimeOut = res.body.dateTimeOut ? dayjs(res.body.dateTimeOut) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((dailyTimeRecord: IDailyTimeRecord) => {
        dailyTimeRecord.dateTimeIn = dailyTimeRecord.dateTimeIn ? dayjs(dailyTimeRecord.dateTimeIn) : undefined;
        dailyTimeRecord.dateTimeOut = dailyTimeRecord.dateTimeOut ? dayjs(dailyTimeRecord.dateTimeOut) : undefined;
      });
    }
    return res;
  }
}
