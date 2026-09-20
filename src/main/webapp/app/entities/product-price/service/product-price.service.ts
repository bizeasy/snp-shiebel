import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductPrice, NewProductPrice } from '../product-price.model';

export type PartialUpdateProductPrice = Partial<IProductPrice> & Pick<IProductPrice, 'id'>;

type RestOf<T extends IProductPrice | NewProductPrice> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestProductPrice = RestOf<IProductPrice>;

export type NewRestProductPrice = RestOf<NewProductPrice>;

export type PartialUpdateRestProductPrice = RestOf<PartialUpdateProductPrice>;

@Service()
export class ProductPricesService {
  readonly productPricesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productPricesResource = httpResource<RestProductPrice[]>(() => {
    const params = this.productPricesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productPrice that have been fetched. It is updated when the productPricesResource emits a new value.
   * In case of error while fetching the productPrices, the signal is set to an empty array.
   */
  readonly productPrices = computed(() =>
    (this.productPricesResource.hasValue() ? this.productPricesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-prices`;

  protected convertValueFromServer(restProductPrice: RestProductPrice): IProductPrice {
    return {
      ...restProductPrice,
      fromDate: restProductPrice.fromDate ? dayjs(restProductPrice.fromDate) : undefined,
      thruDate: restProductPrice.thruDate ? dayjs(restProductPrice.thruDate) : undefined,
    };
  }
}

@Service()
export class ProductPriceService extends ProductPricesService {
  protected readonly http = inject(HttpClient);

  create(productPrice: NewProductPrice): Observable<IProductPrice> {
    const copy = this.convertValueFromClient(productPrice);
    return this.http.post<RestProductPrice>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productPrice: IProductPrice): Observable<IProductPrice> {
    const copy = this.convertValueFromClient(productPrice);
    return this.http
      .put<RestProductPrice>(`${this.resourceUrl}/${encodeURIComponent(this.getProductPriceIdentifier(productPrice))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productPrice: PartialUpdateProductPrice): Observable<IProductPrice> {
    const copy = this.convertValueFromClient(productPrice);
    return this.http
      .patch<RestProductPrice>(`${this.resourceUrl}/${encodeURIComponent(this.getProductPriceIdentifier(productPrice))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IProductPrice> {
    return this.http
      .get<RestProductPrice>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IProductPrice[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductPrice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductPriceIdentifier(productPrice: Pick<IProductPrice, 'id'>): number {
    return productPrice.id;
  }

  compareProductPrice(o1: Pick<IProductPrice, 'id'> | null, o2: Pick<IProductPrice, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductPriceIdentifier(o1) === this.getProductPriceIdentifier(o2) : o1 === o2;
  }

  addProductPriceToCollectionIfMissing<Type extends Pick<IProductPrice, 'id'>>(
    productPriceCollection: Type[],
    ...productPricesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productPrices: Type[] = productPricesToCheck.filter(
      productPriceItem => productPriceItem !== null && productPriceItem !== undefined,
    );
    if (productPrices.length > 0) {
      const productPriceCollectionIdentifiers = productPriceCollection.map(productPriceItem =>
        this.getProductPriceIdentifier(productPriceItem),
      );
      const productPricesToAdd = productPrices.filter(productPriceItem => {
        const productPriceIdentifier = this.getProductPriceIdentifier(productPriceItem);
        if (productPriceCollectionIdentifiers.includes(productPriceIdentifier)) {
          return false;
        }
        productPriceCollectionIdentifiers.push(productPriceIdentifier);
        return true;
      });
      return [...productPricesToAdd, ...productPriceCollection];
    }
    return productPriceCollection;
  }

  protected convertValueFromClient<T extends IProductPrice | NewProductPrice | PartialUpdateProductPrice>(productPrice: T): RestOf<T> {
    return {
      ...productPrice,
      fromDate: productPrice.fromDate?.toJSON() ?? null,
      thruDate: productPrice.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestProductPrice): IProductPrice {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestProductPrice[]): IProductPrice[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
