import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductFeatureGroup, NewProductFeatureGroup } from '../product-feature-group.model';

export type PartialUpdateProductFeatureGroup = Partial<IProductFeatureGroup> & Pick<IProductFeatureGroup, 'id'>;

@Service()
export class ProductFeatureGroupsService {
  readonly productFeatureGroupsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productFeatureGroupsResource = httpResource<IProductFeatureGroup[]>(() => {
    const params = this.productFeatureGroupsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productFeatureGroup that have been fetched. It is updated when the productFeatureGroupsResource emits a new value.
   * In case of error while fetching the productFeatureGroups, the signal is set to an empty array.
   */
  readonly productFeatureGroups = computed(() =>
    this.productFeatureGroupsResource.hasValue() ? this.productFeatureGroupsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-feature-groups`;
}

@Service()
export class ProductFeatureGroupService extends ProductFeatureGroupsService {
  protected readonly http = inject(HttpClient);

  create(productFeatureGroup: NewProductFeatureGroup): Observable<IProductFeatureGroup> {
    return this.http.post<IProductFeatureGroup>(this.resourceUrl, productFeatureGroup);
  }

  update(productFeatureGroup: IProductFeatureGroup): Observable<IProductFeatureGroup> {
    return this.http.put<IProductFeatureGroup>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureGroupIdentifier(productFeatureGroup))}`,
      productFeatureGroup,
    );
  }

  partialUpdate(productFeatureGroup: PartialUpdateProductFeatureGroup): Observable<IProductFeatureGroup> {
    return this.http.patch<IProductFeatureGroup>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureGroupIdentifier(productFeatureGroup))}`,
      productFeatureGroup,
    );
  }

  find(id: number): Observable<IProductFeatureGroup> {
    return this.http.get<IProductFeatureGroup>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductFeatureGroup[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductFeatureGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductFeatureGroupIdentifier(productFeatureGroup: Pick<IProductFeatureGroup, 'id'>): number {
    return productFeatureGroup.id;
  }

  compareProductFeatureGroup(o1: Pick<IProductFeatureGroup, 'id'> | null, o2: Pick<IProductFeatureGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductFeatureGroupIdentifier(o1) === this.getProductFeatureGroupIdentifier(o2) : o1 === o2;
  }

  addProductFeatureGroupToCollectionIfMissing<Type extends Pick<IProductFeatureGroup, 'id'>>(
    productFeatureGroupCollection: Type[],
    ...productFeatureGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productFeatureGroups: Type[] = productFeatureGroupsToCheck.filter(
      productFeatureGroupItem => productFeatureGroupItem !== null && productFeatureGroupItem !== undefined,
    );
    if (productFeatureGroups.length > 0) {
      const productFeatureGroupCollectionIdentifiers = productFeatureGroupCollection.map(productFeatureGroupItem =>
        this.getProductFeatureGroupIdentifier(productFeatureGroupItem),
      );
      const productFeatureGroupsToAdd = productFeatureGroups.filter(productFeatureGroupItem => {
        const productFeatureGroupIdentifier = this.getProductFeatureGroupIdentifier(productFeatureGroupItem);
        if (productFeatureGroupCollectionIdentifiers.includes(productFeatureGroupIdentifier)) {
          return false;
        }
        productFeatureGroupCollectionIdentifiers.push(productFeatureGroupIdentifier);
        return true;
      });
      return [...productFeatureGroupsToAdd, ...productFeatureGroupCollection];
    }
    return productFeatureGroupCollection;
  }
}
