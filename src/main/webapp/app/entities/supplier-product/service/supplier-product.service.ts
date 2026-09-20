import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ISupplierProduct, NewSupplierProduct } from '../supplier-product.model';

export type PartialUpdateSupplierProduct = Partial<ISupplierProduct> & Pick<ISupplierProduct, 'id'>;

type RestOf<T extends ISupplierProduct | NewSupplierProduct> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestSupplierProduct = RestOf<ISupplierProduct>;

export type NewRestSupplierProduct = RestOf<NewSupplierProduct>;

export type PartialUpdateRestSupplierProduct = RestOf<PartialUpdateSupplierProduct>;

@Service()
export class SupplierProductsService {
  readonly supplierProductsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly supplierProductsResource = httpResource<RestSupplierProduct[]>(() => {
    const params = this.supplierProductsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of supplierProduct that have been fetched. It is updated when the supplierProductsResource emits a new value.
   * In case of error while fetching the supplierProducts, the signal is set to an empty array.
   */
  readonly supplierProducts = computed(() =>
    (this.supplierProductsResource.hasValue() ? this.supplierProductsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/supplier-products`;

  protected convertValueFromServer(restSupplierProduct: RestSupplierProduct): ISupplierProduct {
    return {
      ...restSupplierProduct,
      fromDate: restSupplierProduct.fromDate ? dayjs(restSupplierProduct.fromDate) : undefined,
      thruDate: restSupplierProduct.thruDate ? dayjs(restSupplierProduct.thruDate) : undefined,
    };
  }
}

@Service()
export class SupplierProductService extends SupplierProductsService {
  protected readonly http = inject(HttpClient);

  create(supplierProduct: NewSupplierProduct): Observable<ISupplierProduct> {
    const copy = this.convertValueFromClient(supplierProduct);
    return this.http.post<RestSupplierProduct>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(supplierProduct: ISupplierProduct): Observable<ISupplierProduct> {
    const copy = this.convertValueFromClient(supplierProduct);
    return this.http
      .put<RestSupplierProduct>(`${this.resourceUrl}/${encodeURIComponent(this.getSupplierProductIdentifier(supplierProduct))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(supplierProduct: PartialUpdateSupplierProduct): Observable<ISupplierProduct> {
    const copy = this.convertValueFromClient(supplierProduct);
    return this.http
      .patch<RestSupplierProduct>(`${this.resourceUrl}/${encodeURIComponent(this.getSupplierProductIdentifier(supplierProduct))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ISupplierProduct> {
    return this.http
      .get<RestSupplierProduct>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ISupplierProduct[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSupplierProduct[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getSupplierProductIdentifier(supplierProduct: Pick<ISupplierProduct, 'id'>): number {
    return supplierProduct.id;
  }

  compareSupplierProduct(o1: Pick<ISupplierProduct, 'id'> | null, o2: Pick<ISupplierProduct, 'id'> | null): boolean {
    return o1 && o2 ? this.getSupplierProductIdentifier(o1) === this.getSupplierProductIdentifier(o2) : o1 === o2;
  }

  addSupplierProductToCollectionIfMissing<Type extends Pick<ISupplierProduct, 'id'>>(
    supplierProductCollection: Type[],
    ...supplierProductsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const supplierProducts: Type[] = supplierProductsToCheck.filter(
      supplierProductItem => supplierProductItem !== null && supplierProductItem !== undefined,
    );
    if (supplierProducts.length > 0) {
      const supplierProductCollectionIdentifiers = supplierProductCollection.map(supplierProductItem =>
        this.getSupplierProductIdentifier(supplierProductItem),
      );
      const supplierProductsToAdd = supplierProducts.filter(supplierProductItem => {
        const supplierProductIdentifier = this.getSupplierProductIdentifier(supplierProductItem);
        if (supplierProductCollectionIdentifiers.includes(supplierProductIdentifier)) {
          return false;
        }
        supplierProductCollectionIdentifiers.push(supplierProductIdentifier);
        return true;
      });
      return [...supplierProductsToAdd, ...supplierProductCollection];
    }
    return supplierProductCollection;
  }

  protected convertValueFromClient<T extends ISupplierProduct | NewSupplierProduct | PartialUpdateSupplierProduct>(
    supplierProduct: T,
  ): RestOf<T> {
    return {
      ...supplierProduct,
      fromDate: supplierProduct.fromDate?.toJSON() ?? null,
      thruDate: supplierProduct.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestSupplierProduct): ISupplierProduct {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestSupplierProduct[]): ISupplierProduct[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
