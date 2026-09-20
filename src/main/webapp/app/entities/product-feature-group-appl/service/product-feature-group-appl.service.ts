import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductFeatureGroupAppl, NewProductFeatureGroupAppl } from '../product-feature-group-appl.model';

export type PartialUpdateProductFeatureGroupAppl = Partial<IProductFeatureGroupAppl> & Pick<IProductFeatureGroupAppl, 'id'>;

type RestOf<T extends IProductFeatureGroupAppl | NewProductFeatureGroupAppl> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestProductFeatureGroupAppl = RestOf<IProductFeatureGroupAppl>;

export type NewRestProductFeatureGroupAppl = RestOf<NewProductFeatureGroupAppl>;

export type PartialUpdateRestProductFeatureGroupAppl = RestOf<PartialUpdateProductFeatureGroupAppl>;

@Service()
export class ProductFeatureGroupApplsService {
  readonly productFeatureGroupApplsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productFeatureGroupApplsResource = httpResource<RestProductFeatureGroupAppl[]>(() => {
    const params = this.productFeatureGroupApplsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productFeatureGroupAppl that have been fetched. It is updated when the productFeatureGroupApplsResource emits a new value.
   * In case of error while fetching the productFeatureGroupAppls, the signal is set to an empty array.
   */
  readonly productFeatureGroupAppls = computed(() =>
    (this.productFeatureGroupApplsResource.hasValue() ? this.productFeatureGroupApplsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-feature-group-appls`;

  protected convertValueFromServer(restProductFeatureGroupAppl: RestProductFeatureGroupAppl): IProductFeatureGroupAppl {
    return {
      ...restProductFeatureGroupAppl,
      fromDate: restProductFeatureGroupAppl.fromDate ? dayjs(restProductFeatureGroupAppl.fromDate) : undefined,
      thruDate: restProductFeatureGroupAppl.thruDate ? dayjs(restProductFeatureGroupAppl.thruDate) : undefined,
    };
  }
}

@Service()
export class ProductFeatureGroupApplService extends ProductFeatureGroupApplsService {
  protected readonly http = inject(HttpClient);

  create(productFeatureGroupAppl: NewProductFeatureGroupAppl): Observable<IProductFeatureGroupAppl> {
    const copy = this.convertValueFromClient(productFeatureGroupAppl);
    return this.http.post<RestProductFeatureGroupAppl>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productFeatureGroupAppl: IProductFeatureGroupAppl): Observable<IProductFeatureGroupAppl> {
    const copy = this.convertValueFromClient(productFeatureGroupAppl);
    return this.http
      .put<RestProductFeatureGroupAppl>(
        `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureGroupApplIdentifier(productFeatureGroupAppl))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productFeatureGroupAppl: PartialUpdateProductFeatureGroupAppl): Observable<IProductFeatureGroupAppl> {
    const copy = this.convertValueFromClient(productFeatureGroupAppl);
    return this.http
      .patch<RestProductFeatureGroupAppl>(
        `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureGroupApplIdentifier(productFeatureGroupAppl))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IProductFeatureGroupAppl> {
    return this.http
      .get<RestProductFeatureGroupAppl>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IProductFeatureGroupAppl[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductFeatureGroupAppl[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductFeatureGroupApplIdentifier(productFeatureGroupAppl: Pick<IProductFeatureGroupAppl, 'id'>): number {
    return productFeatureGroupAppl.id;
  }

  compareProductFeatureGroupAppl(
    o1: Pick<IProductFeatureGroupAppl, 'id'> | null,
    o2: Pick<IProductFeatureGroupAppl, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getProductFeatureGroupApplIdentifier(o1) === this.getProductFeatureGroupApplIdentifier(o2) : o1 === o2;
  }

  addProductFeatureGroupApplToCollectionIfMissing<Type extends Pick<IProductFeatureGroupAppl, 'id'>>(
    productFeatureGroupApplCollection: Type[],
    ...productFeatureGroupApplsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productFeatureGroupAppls: Type[] = productFeatureGroupApplsToCheck.filter(
      productFeatureGroupApplItem => productFeatureGroupApplItem !== null && productFeatureGroupApplItem !== undefined,
    );
    if (productFeatureGroupAppls.length > 0) {
      const productFeatureGroupApplCollectionIdentifiers = productFeatureGroupApplCollection.map(productFeatureGroupApplItem =>
        this.getProductFeatureGroupApplIdentifier(productFeatureGroupApplItem),
      );
      const productFeatureGroupApplsToAdd = productFeatureGroupAppls.filter(productFeatureGroupApplItem => {
        const productFeatureGroupApplIdentifier = this.getProductFeatureGroupApplIdentifier(productFeatureGroupApplItem);
        if (productFeatureGroupApplCollectionIdentifiers.includes(productFeatureGroupApplIdentifier)) {
          return false;
        }
        productFeatureGroupApplCollectionIdentifiers.push(productFeatureGroupApplIdentifier);
        return true;
      });
      return [...productFeatureGroupApplsToAdd, ...productFeatureGroupApplCollection];
    }
    return productFeatureGroupApplCollection;
  }

  protected convertValueFromClient<T extends IProductFeatureGroupAppl | NewProductFeatureGroupAppl | PartialUpdateProductFeatureGroupAppl>(
    productFeatureGroupAppl: T,
  ): RestOf<T> {
    return {
      ...productFeatureGroupAppl,
      fromDate: productFeatureGroupAppl.fromDate?.toJSON() ?? null,
      thruDate: productFeatureGroupAppl.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestProductFeatureGroupAppl): IProductFeatureGroupAppl {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestProductFeatureGroupAppl[]): IProductFeatureGroupAppl[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
