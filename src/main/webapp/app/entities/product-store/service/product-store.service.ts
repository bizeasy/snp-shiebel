import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductStore, NewProductStore } from '../product-store.model';

export type PartialUpdateProductStore = Partial<IProductStore> & Pick<IProductStore, 'id'>;

@Service()
export class ProductStoresService {
  readonly productStoresParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productStoresResource = httpResource<IProductStore[]>(() => {
    const params = this.productStoresParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productStore that have been fetched. It is updated when the productStoresResource emits a new value.
   * In case of error while fetching the productStores, the signal is set to an empty array.
   */
  readonly productStores = computed(() => (this.productStoresResource.hasValue() ? this.productStoresResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/product-stores`;
}

@Service()
export class ProductStoreService extends ProductStoresService {
  protected readonly http = inject(HttpClient);

  create(productStore: NewProductStore): Observable<IProductStore> {
    return this.http.post<IProductStore>(this.resourceUrl, productStore);
  }

  update(productStore: IProductStore): Observable<IProductStore> {
    return this.http.put<IProductStore>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductStoreIdentifier(productStore))}`,
      productStore,
    );
  }

  partialUpdate(productStore: PartialUpdateProductStore): Observable<IProductStore> {
    return this.http.patch<IProductStore>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductStoreIdentifier(productStore))}`,
      productStore,
    );
  }

  find(id: number): Observable<IProductStore> {
    return this.http.get<IProductStore>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductStore[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductStore[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductStoreIdentifier(productStore: Pick<IProductStore, 'id'>): number {
    return productStore.id;
  }

  compareProductStore(o1: Pick<IProductStore, 'id'> | null, o2: Pick<IProductStore, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductStoreIdentifier(o1) === this.getProductStoreIdentifier(o2) : o1 === o2;
  }

  addProductStoreToCollectionIfMissing<Type extends Pick<IProductStore, 'id'>>(
    productStoreCollection: Type[],
    ...productStoresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productStores: Type[] = productStoresToCheck.filter(
      productStoreItem => productStoreItem !== null && productStoreItem !== undefined,
    );
    if (productStores.length > 0) {
      const productStoreCollectionIdentifiers = productStoreCollection.map(productStoreItem =>
        this.getProductStoreIdentifier(productStoreItem),
      );
      const productStoresToAdd = productStores.filter(productStoreItem => {
        const productStoreIdentifier = this.getProductStoreIdentifier(productStoreItem);
        if (productStoreCollectionIdentifiers.includes(productStoreIdentifier)) {
          return false;
        }
        productStoreCollectionIdentifiers.push(productStoreIdentifier);
        return true;
      });
      return [...productStoresToAdd, ...productStoreCollection];
    }
    return productStoreCollection;
  }
}
