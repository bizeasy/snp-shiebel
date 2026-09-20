import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ILot, NewLot } from '../lot.model';

export type PartialUpdateLot = Partial<ILot> & Pick<ILot, 'id'>;

type RestOf<T extends ILot | NewLot> = Omit<T, 'creationDate' | 'expirationDate' | 'retestDate'> & {
  creationDate?: string | null;
  expirationDate?: string | null;
  retestDate?: string | null;
};

export type RestLot = RestOf<ILot>;

export type NewRestLot = RestOf<NewLot>;

export type PartialUpdateRestLot = RestOf<PartialUpdateLot>;

@Service()
export class LotsService {
  readonly lotsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly lotsResource = httpResource<RestLot[]>(() => {
    const params = this.lotsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of lot that have been fetched. It is updated when the lotsResource emits a new value.
   * In case of error while fetching the lots, the signal is set to an empty array.
   */
  readonly lots = computed(() =>
    (this.lotsResource.hasValue() ? this.lotsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/lots`;

  protected convertValueFromServer(restLot: RestLot): ILot {
    return {
      ...restLot,
      creationDate: restLot.creationDate ? dayjs(restLot.creationDate) : undefined,
      expirationDate: restLot.expirationDate ? dayjs(restLot.expirationDate) : undefined,
      retestDate: restLot.retestDate ? dayjs(restLot.retestDate) : undefined,
    };
  }
}

@Service()
export class LotService extends LotsService {
  protected readonly http = inject(HttpClient);

  create(lot: NewLot): Observable<ILot> {
    const copy = this.convertValueFromClient(lot);
    return this.http.post<RestLot>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(lot: ILot): Observable<ILot> {
    const copy = this.convertValueFromClient(lot);
    return this.http
      .put<RestLot>(`${this.resourceUrl}/${encodeURIComponent(this.getLotIdentifier(lot))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(lot: PartialUpdateLot): Observable<ILot> {
    const copy = this.convertValueFromClient(lot);
    return this.http
      .patch<RestLot>(`${this.resourceUrl}/${encodeURIComponent(this.getLotIdentifier(lot))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ILot> {
    return this.http.get<RestLot>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ILot[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLot[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getLotIdentifier(lot: Pick<ILot, 'id'>): number {
    return lot.id;
  }

  compareLot(o1: Pick<ILot, 'id'> | null, o2: Pick<ILot, 'id'> | null): boolean {
    return o1 && o2 ? this.getLotIdentifier(o1) === this.getLotIdentifier(o2) : o1 === o2;
  }

  addLotToCollectionIfMissing<Type extends Pick<ILot, 'id'>>(lotCollection: Type[], ...lotsToCheck: (Type | null | undefined)[]): Type[] {
    const lots: Type[] = lotsToCheck.filter(lotItem => lotItem !== null && lotItem !== undefined);
    if (lots.length > 0) {
      const lotCollectionIdentifiers = lotCollection.map(lotItem => this.getLotIdentifier(lotItem));
      const lotsToAdd = lots.filter(lotItem => {
        const lotIdentifier = this.getLotIdentifier(lotItem);
        if (lotCollectionIdentifiers.includes(lotIdentifier)) {
          return false;
        }
        lotCollectionIdentifiers.push(lotIdentifier);
        return true;
      });
      return [...lotsToAdd, ...lotCollection];
    }
    return lotCollection;
  }

  protected convertValueFromClient<T extends ILot | NewLot | PartialUpdateLot>(lot: T): RestOf<T> {
    return {
      ...lot,
      creationDate: lot.creationDate?.toJSON() ?? null,
      expirationDate: lot.expirationDate?.toJSON() ?? null,
      retestDate: lot.retestDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestLot): ILot {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestLot[]): ILot[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
