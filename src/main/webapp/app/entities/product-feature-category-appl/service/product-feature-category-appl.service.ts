import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductFeatureCategoryAppl, NewProductFeatureCategoryAppl } from '../product-feature-category-appl.model';

export type PartialUpdateProductFeatureCategoryAppl = Partial<IProductFeatureCategoryAppl> & Pick<IProductFeatureCategoryAppl, 'id'>;

type RestOf<T extends IProductFeatureCategoryAppl | NewProductFeatureCategoryAppl> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestProductFeatureCategoryAppl = RestOf<IProductFeatureCategoryAppl>;

export type NewRestProductFeatureCategoryAppl = RestOf<NewProductFeatureCategoryAppl>;

export type PartialUpdateRestProductFeatureCategoryAppl = RestOf<PartialUpdateProductFeatureCategoryAppl>;

@Service()
export class ProductFeatureCategoryApplsService {
  readonly productFeatureCategoryApplsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productFeatureCategoryApplsResource = httpResource<RestProductFeatureCategoryAppl[]>(() => {
    const params = this.productFeatureCategoryApplsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productFeatureCategoryAppl that have been fetched. It is updated when the productFeatureCategoryApplsResource emits a new value.
   * In case of error while fetching the productFeatureCategoryAppls, the signal is set to an empty array.
   */
  readonly productFeatureCategoryAppls = computed(() =>
    (this.productFeatureCategoryApplsResource.hasValue() ? this.productFeatureCategoryApplsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-feature-category-appls`;

  protected convertValueFromServer(restProductFeatureCategoryAppl: RestProductFeatureCategoryAppl): IProductFeatureCategoryAppl {
    return {
      ...restProductFeatureCategoryAppl,
      fromDate: restProductFeatureCategoryAppl.fromDate ? dayjs(restProductFeatureCategoryAppl.fromDate) : undefined,
      thruDate: restProductFeatureCategoryAppl.thruDate ? dayjs(restProductFeatureCategoryAppl.thruDate) : undefined,
    };
  }
}

@Service()
export class ProductFeatureCategoryApplService extends ProductFeatureCategoryApplsService {
  protected readonly http = inject(HttpClient);

  create(productFeatureCategoryAppl: NewProductFeatureCategoryAppl): Observable<IProductFeatureCategoryAppl> {
    const copy = this.convertValueFromClient(productFeatureCategoryAppl);
    return this.http.post<RestProductFeatureCategoryAppl>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productFeatureCategoryAppl: IProductFeatureCategoryAppl): Observable<IProductFeatureCategoryAppl> {
    const copy = this.convertValueFromClient(productFeatureCategoryAppl);
    return this.http
      .put<RestProductFeatureCategoryAppl>(
        `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureCategoryApplIdentifier(productFeatureCategoryAppl))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productFeatureCategoryAppl: PartialUpdateProductFeatureCategoryAppl): Observable<IProductFeatureCategoryAppl> {
    const copy = this.convertValueFromClient(productFeatureCategoryAppl);
    return this.http
      .patch<RestProductFeatureCategoryAppl>(
        `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureCategoryApplIdentifier(productFeatureCategoryAppl))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IProductFeatureCategoryAppl> {
    return this.http
      .get<RestProductFeatureCategoryAppl>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IProductFeatureCategoryAppl[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductFeatureCategoryAppl[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductFeatureCategoryApplIdentifier(productFeatureCategoryAppl: Pick<IProductFeatureCategoryAppl, 'id'>): number {
    return productFeatureCategoryAppl.id;
  }

  compareProductFeatureCategoryAppl(
    o1: Pick<IProductFeatureCategoryAppl, 'id'> | null,
    o2: Pick<IProductFeatureCategoryAppl, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getProductFeatureCategoryApplIdentifier(o1) === this.getProductFeatureCategoryApplIdentifier(o2) : o1 === o2;
  }

  addProductFeatureCategoryApplToCollectionIfMissing<Type extends Pick<IProductFeatureCategoryAppl, 'id'>>(
    productFeatureCategoryApplCollection: Type[],
    ...productFeatureCategoryApplsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productFeatureCategoryAppls: Type[] = productFeatureCategoryApplsToCheck.filter(
      productFeatureCategoryApplItem => productFeatureCategoryApplItem !== null && productFeatureCategoryApplItem !== undefined,
    );
    if (productFeatureCategoryAppls.length > 0) {
      const productFeatureCategoryApplCollectionIdentifiers = productFeatureCategoryApplCollection.map(productFeatureCategoryApplItem =>
        this.getProductFeatureCategoryApplIdentifier(productFeatureCategoryApplItem),
      );
      const productFeatureCategoryApplsToAdd = productFeatureCategoryAppls.filter(productFeatureCategoryApplItem => {
        const productFeatureCategoryApplIdentifier = this.getProductFeatureCategoryApplIdentifier(productFeatureCategoryApplItem);
        if (productFeatureCategoryApplCollectionIdentifiers.includes(productFeatureCategoryApplIdentifier)) {
          return false;
        }
        productFeatureCategoryApplCollectionIdentifiers.push(productFeatureCategoryApplIdentifier);
        return true;
      });
      return [...productFeatureCategoryApplsToAdd, ...productFeatureCategoryApplCollection];
    }
    return productFeatureCategoryApplCollection;
  }

  protected convertValueFromClient<
    T extends IProductFeatureCategoryAppl | NewProductFeatureCategoryAppl | PartialUpdateProductFeatureCategoryAppl,
  >(productFeatureCategoryAppl: T): RestOf<T> {
    return {
      ...productFeatureCategoryAppl,
      fromDate: productFeatureCategoryAppl.fromDate?.toJSON() ?? null,
      thruDate: productFeatureCategoryAppl.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestProductFeatureCategoryAppl): IProductFeatureCategoryAppl {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestProductFeatureCategoryAppl[]): IProductFeatureCategoryAppl[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
