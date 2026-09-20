import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductAssocType, NewProductAssocType } from '../product-assoc-type.model';

export type PartialUpdateProductAssocType = Partial<IProductAssocType> & Pick<IProductAssocType, 'id'>;

@Service()
export class ProductAssocTypesService {
  readonly productAssocTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productAssocTypesResource = httpResource<IProductAssocType[]>(() => {
    const params = this.productAssocTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productAssocType that have been fetched. It is updated when the productAssocTypesResource emits a new value.
   * In case of error while fetching the productAssocTypes, the signal is set to an empty array.
   */
  readonly productAssocTypes = computed(() => (this.productAssocTypesResource.hasValue() ? this.productAssocTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/product-assoc-types`;
}

@Service()
export class ProductAssocTypeService extends ProductAssocTypesService {
  protected readonly http = inject(HttpClient);

  create(productAssocType: NewProductAssocType): Observable<IProductAssocType> {
    return this.http.post<IProductAssocType>(this.resourceUrl, productAssocType);
  }

  update(productAssocType: IProductAssocType): Observable<IProductAssocType> {
    return this.http.put<IProductAssocType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductAssocTypeIdentifier(productAssocType))}`,
      productAssocType,
    );
  }

  partialUpdate(productAssocType: PartialUpdateProductAssocType): Observable<IProductAssocType> {
    return this.http.patch<IProductAssocType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductAssocTypeIdentifier(productAssocType))}`,
      productAssocType,
    );
  }

  find(id: number): Observable<IProductAssocType> {
    return this.http.get<IProductAssocType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductAssocType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductAssocType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductAssocTypeIdentifier(productAssocType: Pick<IProductAssocType, 'id'>): number {
    return productAssocType.id;
  }

  compareProductAssocType(o1: Pick<IProductAssocType, 'id'> | null, o2: Pick<IProductAssocType, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductAssocTypeIdentifier(o1) === this.getProductAssocTypeIdentifier(o2) : o1 === o2;
  }

  addProductAssocTypeToCollectionIfMissing<Type extends Pick<IProductAssocType, 'id'>>(
    productAssocTypeCollection: Type[],
    ...productAssocTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productAssocTypes: Type[] = productAssocTypesToCheck.filter(
      productAssocTypeItem => productAssocTypeItem !== null && productAssocTypeItem !== undefined,
    );
    if (productAssocTypes.length > 0) {
      const productAssocTypeCollectionIdentifiers = productAssocTypeCollection.map(productAssocTypeItem =>
        this.getProductAssocTypeIdentifier(productAssocTypeItem),
      );
      const productAssocTypesToAdd = productAssocTypes.filter(productAssocTypeItem => {
        const productAssocTypeIdentifier = this.getProductAssocTypeIdentifier(productAssocTypeItem);
        if (productAssocTypeCollectionIdentifiers.includes(productAssocTypeIdentifier)) {
          return false;
        }
        productAssocTypeCollectionIdentifiers.push(productAssocTypeIdentifier);
        return true;
      });
      return [...productAssocTypesToAdd, ...productAssocTypeCollection];
    }
    return productAssocTypeCollection;
  }
}
