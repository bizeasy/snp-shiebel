import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IUomConversion, NewUomConversion } from '../uom-conversion.model';

export type PartialUpdateUomConversion = Partial<IUomConversion> & Pick<IUomConversion, 'id'>;

type RestOf<T extends IUomConversion | NewUomConversion> = Omit<T, 'fromDate' | 'toDate'> & {
  fromDate?: string | null;
  toDate?: string | null;
};

export type RestUomConversion = RestOf<IUomConversion>;

export type NewRestUomConversion = RestOf<NewUomConversion>;

export type PartialUpdateRestUomConversion = RestOf<PartialUpdateUomConversion>;

@Service()
export class UomConversionsService {
  readonly uomConversionsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly uomConversionsResource = httpResource<RestUomConversion[]>(() => {
    const params = this.uomConversionsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of uomConversion that have been fetched. It is updated when the uomConversionsResource emits a new value.
   * In case of error while fetching the uomConversions, the signal is set to an empty array.
   */
  readonly uomConversions = computed(() =>
    (this.uomConversionsResource.hasValue() ? this.uomConversionsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/uom-conversions`;

  protected convertValueFromServer(restUomConversion: RestUomConversion): IUomConversion {
    return {
      ...restUomConversion,
      fromDate: restUomConversion.fromDate ? dayjs(restUomConversion.fromDate) : undefined,
      toDate: restUomConversion.toDate ? dayjs(restUomConversion.toDate) : undefined,
    };
  }
}

@Service()
export class UomConversionService extends UomConversionsService {
  protected readonly http = inject(HttpClient);

  create(uomConversion: NewUomConversion): Observable<IUomConversion> {
    const copy = this.convertValueFromClient(uomConversion);
    return this.http.post<RestUomConversion>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(uomConversion: IUomConversion): Observable<IUomConversion> {
    const copy = this.convertValueFromClient(uomConversion);
    return this.http
      .put<RestUomConversion>(`${this.resourceUrl}/${encodeURIComponent(this.getUomConversionIdentifier(uomConversion))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(uomConversion: PartialUpdateUomConversion): Observable<IUomConversion> {
    const copy = this.convertValueFromClient(uomConversion);
    return this.http
      .patch<RestUomConversion>(`${this.resourceUrl}/${encodeURIComponent(this.getUomConversionIdentifier(uomConversion))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IUomConversion> {
    return this.http
      .get<RestUomConversion>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IUomConversion[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUomConversion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getUomConversionIdentifier(uomConversion: Pick<IUomConversion, 'id'>): number {
    return uomConversion.id;
  }

  compareUomConversion(o1: Pick<IUomConversion, 'id'> | null, o2: Pick<IUomConversion, 'id'> | null): boolean {
    return o1 && o2 ? this.getUomConversionIdentifier(o1) === this.getUomConversionIdentifier(o2) : o1 === o2;
  }

  addUomConversionToCollectionIfMissing<Type extends Pick<IUomConversion, 'id'>>(
    uomConversionCollection: Type[],
    ...uomConversionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const uomConversions: Type[] = uomConversionsToCheck.filter(
      uomConversionItem => uomConversionItem !== null && uomConversionItem !== undefined,
    );
    if (uomConversions.length > 0) {
      const uomConversionCollectionIdentifiers = uomConversionCollection.map(uomConversionItem =>
        this.getUomConversionIdentifier(uomConversionItem),
      );
      const uomConversionsToAdd = uomConversions.filter(uomConversionItem => {
        const uomConversionIdentifier = this.getUomConversionIdentifier(uomConversionItem);
        if (uomConversionCollectionIdentifiers.includes(uomConversionIdentifier)) {
          return false;
        }
        uomConversionCollectionIdentifiers.push(uomConversionIdentifier);
        return true;
      });
      return [...uomConversionsToAdd, ...uomConversionCollection];
    }
    return uomConversionCollection;
  }

  protected convertValueFromClient<T extends IUomConversion | NewUomConversion | PartialUpdateUomConversion>(uomConversion: T): RestOf<T> {
    return {
      ...uomConversion,
      fromDate: uomConversion.fromDate?.toJSON() ?? null,
      toDate: uomConversion.toDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestUomConversion): IUomConversion {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestUomConversion[]): IUomConversion[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
