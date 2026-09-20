import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IItemIssuance, NewItemIssuance } from '../item-issuance.model';

export type PartialUpdateItemIssuance = Partial<IItemIssuance> & Pick<IItemIssuance, 'id'>;

type RestOf<T extends IItemIssuance | NewItemIssuance> = Omit<T, 'issuedDate'> & {
  issuedDate?: string | null;
};

export type RestItemIssuance = RestOf<IItemIssuance>;

export type NewRestItemIssuance = RestOf<NewItemIssuance>;

export type PartialUpdateRestItemIssuance = RestOf<PartialUpdateItemIssuance>;

@Service()
export class ItemIssuancesService {
  readonly itemIssuancesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly itemIssuancesResource = httpResource<RestItemIssuance[]>(() => {
    const params = this.itemIssuancesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of itemIssuance that have been fetched. It is updated when the itemIssuancesResource emits a new value.
   * In case of error while fetching the itemIssuances, the signal is set to an empty array.
   */
  readonly itemIssuances = computed(() =>
    (this.itemIssuancesResource.hasValue() ? this.itemIssuancesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/item-issuances`;

  protected convertValueFromServer(restItemIssuance: RestItemIssuance): IItemIssuance {
    return {
      ...restItemIssuance,
      issuedDate: restItemIssuance.issuedDate ? dayjs(restItemIssuance.issuedDate) : undefined,
    };
  }
}

@Service()
export class ItemIssuanceService extends ItemIssuancesService {
  protected readonly http = inject(HttpClient);

  create(itemIssuance: NewItemIssuance): Observable<IItemIssuance> {
    const copy = this.convertValueFromClient(itemIssuance);
    return this.http.post<RestItemIssuance>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(itemIssuance: IItemIssuance): Observable<IItemIssuance> {
    const copy = this.convertValueFromClient(itemIssuance);
    return this.http
      .put<RestItemIssuance>(`${this.resourceUrl}/${encodeURIComponent(this.getItemIssuanceIdentifier(itemIssuance))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(itemIssuance: PartialUpdateItemIssuance): Observable<IItemIssuance> {
    const copy = this.convertValueFromClient(itemIssuance);
    return this.http
      .patch<RestItemIssuance>(`${this.resourceUrl}/${encodeURIComponent(this.getItemIssuanceIdentifier(itemIssuance))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IItemIssuance> {
    return this.http
      .get<RestItemIssuance>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IItemIssuance[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestItemIssuance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getItemIssuanceIdentifier(itemIssuance: Pick<IItemIssuance, 'id'>): number {
    return itemIssuance.id;
  }

  compareItemIssuance(o1: Pick<IItemIssuance, 'id'> | null, o2: Pick<IItemIssuance, 'id'> | null): boolean {
    return o1 && o2 ? this.getItemIssuanceIdentifier(o1) === this.getItemIssuanceIdentifier(o2) : o1 === o2;
  }

  addItemIssuanceToCollectionIfMissing<Type extends Pick<IItemIssuance, 'id'>>(
    itemIssuanceCollection: Type[],
    ...itemIssuancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const itemIssuances: Type[] = itemIssuancesToCheck.filter(
      itemIssuanceItem => itemIssuanceItem !== null && itemIssuanceItem !== undefined,
    );
    if (itemIssuances.length > 0) {
      const itemIssuanceCollectionIdentifiers = itemIssuanceCollection.map(itemIssuanceItem =>
        this.getItemIssuanceIdentifier(itemIssuanceItem),
      );
      const itemIssuancesToAdd = itemIssuances.filter(itemIssuanceItem => {
        const itemIssuanceIdentifier = this.getItemIssuanceIdentifier(itemIssuanceItem);
        if (itemIssuanceCollectionIdentifiers.includes(itemIssuanceIdentifier)) {
          return false;
        }
        itemIssuanceCollectionIdentifiers.push(itemIssuanceIdentifier);
        return true;
      });
      return [...itemIssuancesToAdd, ...itemIssuanceCollection];
    }
    return itemIssuanceCollection;
  }

  protected convertValueFromClient<T extends IItemIssuance | NewItemIssuance | PartialUpdateItemIssuance>(itemIssuance: T): RestOf<T> {
    return {
      ...itemIssuance,
      issuedDate: itemIssuance.issuedDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestItemIssuance): IItemIssuance {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestItemIssuance[]): IItemIssuance[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
