import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductKeyword, NewProductKeyword } from '../product-keyword.model';

export type PartialUpdateProductKeyword = Partial<IProductKeyword> & Pick<IProductKeyword, 'id'>;

@Service()
export class ProductKeywordsService {
  readonly productKeywordsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productKeywordsResource = httpResource<IProductKeyword[]>(() => {
    const params = this.productKeywordsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productKeyword that have been fetched. It is updated when the productKeywordsResource emits a new value.
   * In case of error while fetching the productKeywords, the signal is set to an empty array.
   */
  readonly productKeywords = computed(() => (this.productKeywordsResource.hasValue() ? this.productKeywordsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/product-keywords`;
}

@Service()
export class ProductKeywordService extends ProductKeywordsService {
  protected readonly http = inject(HttpClient);

  create(productKeyword: NewProductKeyword): Observable<IProductKeyword> {
    return this.http.post<IProductKeyword>(this.resourceUrl, productKeyword);
  }

  update(productKeyword: IProductKeyword): Observable<IProductKeyword> {
    return this.http.put<IProductKeyword>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductKeywordIdentifier(productKeyword))}`,
      productKeyword,
    );
  }

  partialUpdate(productKeyword: PartialUpdateProductKeyword): Observable<IProductKeyword> {
    return this.http.patch<IProductKeyword>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductKeywordIdentifier(productKeyword))}`,
      productKeyword,
    );
  }

  find(id: number): Observable<IProductKeyword> {
    return this.http.get<IProductKeyword>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductKeyword[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductKeyword[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductKeywordIdentifier(productKeyword: Pick<IProductKeyword, 'id'>): number {
    return productKeyword.id;
  }

  compareProductKeyword(o1: Pick<IProductKeyword, 'id'> | null, o2: Pick<IProductKeyword, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductKeywordIdentifier(o1) === this.getProductKeywordIdentifier(o2) : o1 === o2;
  }

  addProductKeywordToCollectionIfMissing<Type extends Pick<IProductKeyword, 'id'>>(
    productKeywordCollection: Type[],
    ...productKeywordsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productKeywords: Type[] = productKeywordsToCheck.filter(
      productKeywordItem => productKeywordItem !== null && productKeywordItem !== undefined,
    );
    if (productKeywords.length > 0) {
      const productKeywordCollectionIdentifiers = productKeywordCollection.map(productKeywordItem =>
        this.getProductKeywordIdentifier(productKeywordItem),
      );
      const productKeywordsToAdd = productKeywords.filter(productKeywordItem => {
        const productKeywordIdentifier = this.getProductKeywordIdentifier(productKeywordItem);
        if (productKeywordCollectionIdentifiers.includes(productKeywordIdentifier)) {
          return false;
        }
        productKeywordCollectionIdentifiers.push(productKeywordIdentifier);
        return true;
      });
      return [...productKeywordsToAdd, ...productKeywordCollection];
    }
    return productKeywordCollection;
  }
}
