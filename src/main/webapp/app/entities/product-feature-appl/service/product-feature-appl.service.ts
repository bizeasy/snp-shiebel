import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductFeatureAppl, NewProductFeatureAppl } from '../product-feature-appl.model';

export type PartialUpdateProductFeatureAppl = Partial<IProductFeatureAppl> & Pick<IProductFeatureAppl, 'id'>;

type RestOf<T extends IProductFeatureAppl | NewProductFeatureAppl> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestProductFeatureAppl = RestOf<IProductFeatureAppl>;

export type NewRestProductFeatureAppl = RestOf<NewProductFeatureAppl>;

export type PartialUpdateRestProductFeatureAppl = RestOf<PartialUpdateProductFeatureAppl>;

@Service()
export class ProductFeatureApplsService {
  readonly productFeatureApplsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productFeatureApplsResource = httpResource<RestProductFeatureAppl[]>(() => {
    const params = this.productFeatureApplsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productFeatureAppl that have been fetched. It is updated when the productFeatureApplsResource emits a new value.
   * In case of error while fetching the productFeatureAppls, the signal is set to an empty array.
   */
  readonly productFeatureAppls = computed(() =>
    (this.productFeatureApplsResource.hasValue() ? this.productFeatureApplsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-feature-appls`;

  protected convertValueFromServer(restProductFeatureAppl: RestProductFeatureAppl): IProductFeatureAppl {
    return {
      ...restProductFeatureAppl,
      fromDate: restProductFeatureAppl.fromDate ? dayjs(restProductFeatureAppl.fromDate) : undefined,
      thruDate: restProductFeatureAppl.thruDate ? dayjs(restProductFeatureAppl.thruDate) : undefined,
    };
  }
}

@Service()
export class ProductFeatureApplService extends ProductFeatureApplsService {
  protected readonly http = inject(HttpClient);

  create(productFeatureAppl: NewProductFeatureAppl): Observable<IProductFeatureAppl> {
    const copy = this.convertValueFromClient(productFeatureAppl);
    return this.http.post<RestProductFeatureAppl>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productFeatureAppl: IProductFeatureAppl): Observable<IProductFeatureAppl> {
    const copy = this.convertValueFromClient(productFeatureAppl);
    return this.http
      .put<RestProductFeatureAppl>(
        `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureApplIdentifier(productFeatureAppl))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productFeatureAppl: PartialUpdateProductFeatureAppl): Observable<IProductFeatureAppl> {
    const copy = this.convertValueFromClient(productFeatureAppl);
    return this.http
      .patch<RestProductFeatureAppl>(
        `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureApplIdentifier(productFeatureAppl))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IProductFeatureAppl> {
    return this.http
      .get<RestProductFeatureAppl>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IProductFeatureAppl[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductFeatureAppl[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductFeatureApplIdentifier(productFeatureAppl: Pick<IProductFeatureAppl, 'id'>): number {
    return productFeatureAppl.id;
  }

  compareProductFeatureAppl(o1: Pick<IProductFeatureAppl, 'id'> | null, o2: Pick<IProductFeatureAppl, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductFeatureApplIdentifier(o1) === this.getProductFeatureApplIdentifier(o2) : o1 === o2;
  }

  addProductFeatureApplToCollectionIfMissing<Type extends Pick<IProductFeatureAppl, 'id'>>(
    productFeatureApplCollection: Type[],
    ...productFeatureApplsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productFeatureAppls: Type[] = productFeatureApplsToCheck.filter(
      productFeatureApplItem => productFeatureApplItem !== null && productFeatureApplItem !== undefined,
    );
    if (productFeatureAppls.length > 0) {
      const productFeatureApplCollectionIdentifiers = productFeatureApplCollection.map(productFeatureApplItem =>
        this.getProductFeatureApplIdentifier(productFeatureApplItem),
      );
      const productFeatureApplsToAdd = productFeatureAppls.filter(productFeatureApplItem => {
        const productFeatureApplIdentifier = this.getProductFeatureApplIdentifier(productFeatureApplItem);
        if (productFeatureApplCollectionIdentifiers.includes(productFeatureApplIdentifier)) {
          return false;
        }
        productFeatureApplCollectionIdentifiers.push(productFeatureApplIdentifier);
        return true;
      });
      return [...productFeatureApplsToAdd, ...productFeatureApplCollection];
    }
    return productFeatureApplCollection;
  }

  protected convertValueFromClient<T extends IProductFeatureAppl | NewProductFeatureAppl | PartialUpdateProductFeatureAppl>(
    productFeatureAppl: T,
  ): RestOf<T> {
    return {
      ...productFeatureAppl,
      fromDate: productFeatureAppl.fromDate?.toJSON() ?? null,
      thruDate: productFeatureAppl.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestProductFeatureAppl): IProductFeatureAppl {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestProductFeatureAppl[]): IProductFeatureAppl[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
