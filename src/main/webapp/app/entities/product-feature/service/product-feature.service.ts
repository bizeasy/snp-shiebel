import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductFeature, NewProductFeature } from '../product-feature.model';

export type PartialUpdateProductFeature = Partial<IProductFeature> & Pick<IProductFeature, 'id'>;

@Service()
export class ProductFeaturesService {
  readonly productFeaturesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productFeaturesResource = httpResource<IProductFeature[]>(() => {
    const params = this.productFeaturesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productFeature that have been fetched. It is updated when the productFeaturesResource emits a new value.
   * In case of error while fetching the productFeatures, the signal is set to an empty array.
   */
  readonly productFeatures = computed(() => (this.productFeaturesResource.hasValue() ? this.productFeaturesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/product-features`;
}

@Service()
export class ProductFeatureService extends ProductFeaturesService {
  protected readonly http = inject(HttpClient);

  create(productFeature: NewProductFeature): Observable<IProductFeature> {
    return this.http.post<IProductFeature>(this.resourceUrl, productFeature);
  }

  update(productFeature: IProductFeature): Observable<IProductFeature> {
    return this.http.put<IProductFeature>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureIdentifier(productFeature))}`,
      productFeature,
    );
  }

  partialUpdate(productFeature: PartialUpdateProductFeature): Observable<IProductFeature> {
    return this.http.patch<IProductFeature>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureIdentifier(productFeature))}`,
      productFeature,
    );
  }

  find(id: number): Observable<IProductFeature> {
    return this.http.get<IProductFeature>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductFeature[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductFeature[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductFeatureIdentifier(productFeature: Pick<IProductFeature, 'id'>): number {
    return productFeature.id;
  }

  compareProductFeature(o1: Pick<IProductFeature, 'id'> | null, o2: Pick<IProductFeature, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductFeatureIdentifier(o1) === this.getProductFeatureIdentifier(o2) : o1 === o2;
  }

  addProductFeatureToCollectionIfMissing<Type extends Pick<IProductFeature, 'id'>>(
    productFeatureCollection: Type[],
    ...productFeaturesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productFeatures: Type[] = productFeaturesToCheck.filter(
      productFeatureItem => productFeatureItem !== null && productFeatureItem !== undefined,
    );
    if (productFeatures.length > 0) {
      const productFeatureCollectionIdentifiers = productFeatureCollection.map(productFeatureItem =>
        this.getProductFeatureIdentifier(productFeatureItem),
      );
      const productFeaturesToAdd = productFeatures.filter(productFeatureItem => {
        const productFeatureIdentifier = this.getProductFeatureIdentifier(productFeatureItem);
        if (productFeatureCollectionIdentifiers.includes(productFeatureIdentifier)) {
          return false;
        }
        productFeatureCollectionIdentifiers.push(productFeatureIdentifier);
        return true;
      });
      return [...productFeaturesToAdd, ...productFeatureCollection];
    }
    return productFeatureCollection;
  }
}
