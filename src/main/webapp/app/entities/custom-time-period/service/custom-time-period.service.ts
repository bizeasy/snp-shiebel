import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICustomTimePeriod, NewCustomTimePeriod } from '../custom-time-period.model';

export type PartialUpdateCustomTimePeriod = Partial<ICustomTimePeriod> & Pick<ICustomTimePeriod, 'id'>;

type RestOf<T extends ICustomTimePeriod | NewCustomTimePeriod> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestCustomTimePeriod = RestOf<ICustomTimePeriod>;

export type NewRestCustomTimePeriod = RestOf<NewCustomTimePeriod>;

export type PartialUpdateRestCustomTimePeriod = RestOf<PartialUpdateCustomTimePeriod>;

@Service()
export class CustomTimePeriodsService {
  readonly customTimePeriodsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly customTimePeriodsResource = httpResource<RestCustomTimePeriod[]>(() => {
    const params = this.customTimePeriodsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of customTimePeriod that have been fetched. It is updated when the customTimePeriodsResource emits a new value.
   * In case of error while fetching the customTimePeriods, the signal is set to an empty array.
   */
  readonly customTimePeriods = computed(() =>
    (this.customTimePeriodsResource.hasValue() ? this.customTimePeriodsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/custom-time-periods`;

  protected convertValueFromServer(restCustomTimePeriod: RestCustomTimePeriod): ICustomTimePeriod {
    return {
      ...restCustomTimePeriod,
      fromDate: restCustomTimePeriod.fromDate ? dayjs(restCustomTimePeriod.fromDate) : undefined,
      thruDate: restCustomTimePeriod.thruDate ? dayjs(restCustomTimePeriod.thruDate) : undefined,
    };
  }
}

@Service()
export class CustomTimePeriodService extends CustomTimePeriodsService {
  protected readonly http = inject(HttpClient);

  create(customTimePeriod: NewCustomTimePeriod): Observable<ICustomTimePeriod> {
    const copy = this.convertValueFromClient(customTimePeriod);
    return this.http.post<RestCustomTimePeriod>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(customTimePeriod: ICustomTimePeriod): Observable<ICustomTimePeriod> {
    const copy = this.convertValueFromClient(customTimePeriod);
    return this.http
      .put<RestCustomTimePeriod>(`${this.resourceUrl}/${encodeURIComponent(this.getCustomTimePeriodIdentifier(customTimePeriod))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(customTimePeriod: PartialUpdateCustomTimePeriod): Observable<ICustomTimePeriod> {
    const copy = this.convertValueFromClient(customTimePeriod);
    return this.http
      .patch<RestCustomTimePeriod>(`${this.resourceUrl}/${encodeURIComponent(this.getCustomTimePeriodIdentifier(customTimePeriod))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICustomTimePeriod> {
    return this.http
      .get<RestCustomTimePeriod>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICustomTimePeriod[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCustomTimePeriod[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCustomTimePeriodIdentifier(customTimePeriod: Pick<ICustomTimePeriod, 'id'>): number {
    return customTimePeriod.id;
  }

  compareCustomTimePeriod(o1: Pick<ICustomTimePeriod, 'id'> | null, o2: Pick<ICustomTimePeriod, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomTimePeriodIdentifier(o1) === this.getCustomTimePeriodIdentifier(o2) : o1 === o2;
  }

  addCustomTimePeriodToCollectionIfMissing<Type extends Pick<ICustomTimePeriod, 'id'>>(
    customTimePeriodCollection: Type[],
    ...customTimePeriodsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customTimePeriods: Type[] = customTimePeriodsToCheck.filter(
      customTimePeriodItem => customTimePeriodItem !== null && customTimePeriodItem !== undefined,
    );
    if (customTimePeriods.length > 0) {
      const customTimePeriodCollectionIdentifiers = customTimePeriodCollection.map(customTimePeriodItem =>
        this.getCustomTimePeriodIdentifier(customTimePeriodItem),
      );
      const customTimePeriodsToAdd = customTimePeriods.filter(customTimePeriodItem => {
        const customTimePeriodIdentifier = this.getCustomTimePeriodIdentifier(customTimePeriodItem);
        if (customTimePeriodCollectionIdentifiers.includes(customTimePeriodIdentifier)) {
          return false;
        }
        customTimePeriodCollectionIdentifiers.push(customTimePeriodIdentifier);
        return true;
      });
      return [...customTimePeriodsToAdd, ...customTimePeriodCollection];
    }
    return customTimePeriodCollection;
  }

  protected convertValueFromClient<T extends ICustomTimePeriod | NewCustomTimePeriod | PartialUpdateCustomTimePeriod>(
    customTimePeriod: T,
  ): RestOf<T> {
    return {
      ...customTimePeriod,
      fromDate: customTimePeriod.fromDate?.format(DATE_FORMAT) ?? null,
      thruDate: customTimePeriod.thruDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCustomTimePeriod): ICustomTimePeriod {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCustomTimePeriod[]): ICustomTimePeriod[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
