import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductAssoc, NewProductAssoc } from '../product-assoc.model';

export type PartialUpdateProductAssoc = Partial<IProductAssoc> & Pick<IProductAssoc, 'id'>;

type RestOf<T extends IProductAssoc | NewProductAssoc> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestProductAssoc = RestOf<IProductAssoc>;

export type NewRestProductAssoc = RestOf<NewProductAssoc>;

export type PartialUpdateRestProductAssoc = RestOf<PartialUpdateProductAssoc>;

@Service()
export class ProductAssocsService {
  readonly productAssocsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productAssocsResource = httpResource<RestProductAssoc[]>(() => {
    const params = this.productAssocsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productAssoc that have been fetched. It is updated when the productAssocsResource emits a new value.
   * In case of error while fetching the productAssocs, the signal is set to an empty array.
   */
  readonly productAssocs = computed(() =>
    (this.productAssocsResource.hasValue() ? this.productAssocsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-assocs`;

  protected convertValueFromServer(restProductAssoc: RestProductAssoc): IProductAssoc {
    return {
      ...restProductAssoc,
      fromDate: restProductAssoc.fromDate ? dayjs(restProductAssoc.fromDate) : undefined,
      thruDate: restProductAssoc.thruDate ? dayjs(restProductAssoc.thruDate) : undefined,
    };
  }
}

@Service()
export class ProductAssocService extends ProductAssocsService {
  protected readonly http = inject(HttpClient);

  create(productAssoc: NewProductAssoc): Observable<IProductAssoc> {
    const copy = this.convertValueFromClient(productAssoc);
    return this.http.post<RestProductAssoc>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productAssoc: IProductAssoc): Observable<IProductAssoc> {
    const copy = this.convertValueFromClient(productAssoc);
    return this.http
      .put<RestProductAssoc>(`${this.resourceUrl}/${encodeURIComponent(this.getProductAssocIdentifier(productAssoc))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productAssoc: PartialUpdateProductAssoc): Observable<IProductAssoc> {
    const copy = this.convertValueFromClient(productAssoc);
    return this.http
      .patch<RestProductAssoc>(`${this.resourceUrl}/${encodeURIComponent(this.getProductAssocIdentifier(productAssoc))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IProductAssoc> {
    return this.http
      .get<RestProductAssoc>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IProductAssoc[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductAssoc[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductAssocIdentifier(productAssoc: Pick<IProductAssoc, 'id'>): number {
    return productAssoc.id;
  }

  compareProductAssoc(o1: Pick<IProductAssoc, 'id'> | null, o2: Pick<IProductAssoc, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductAssocIdentifier(o1) === this.getProductAssocIdentifier(o2) : o1 === o2;
  }

  addProductAssocToCollectionIfMissing<Type extends Pick<IProductAssoc, 'id'>>(
    productAssocCollection: Type[],
    ...productAssocsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productAssocs: Type[] = productAssocsToCheck.filter(
      productAssocItem => productAssocItem !== null && productAssocItem !== undefined,
    );
    if (productAssocs.length > 0) {
      const productAssocCollectionIdentifiers = productAssocCollection.map(productAssocItem =>
        this.getProductAssocIdentifier(productAssocItem),
      );
      const productAssocsToAdd = productAssocs.filter(productAssocItem => {
        const productAssocIdentifier = this.getProductAssocIdentifier(productAssocItem);
        if (productAssocCollectionIdentifiers.includes(productAssocIdentifier)) {
          return false;
        }
        productAssocCollectionIdentifiers.push(productAssocIdentifier);
        return true;
      });
      return [...productAssocsToAdd, ...productAssocCollection];
    }
    return productAssocCollection;
  }

  protected convertValueFromClient<T extends IProductAssoc | NewProductAssoc | PartialUpdateProductAssoc>(productAssoc: T): RestOf<T> {
    return {
      ...productAssoc,
      fromDate: productAssoc.fromDate?.toJSON() ?? null,
      thruDate: productAssoc.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestProductAssoc): IProductAssoc {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestProductAssoc[]): IProductAssoc[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
