import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICustRequestCategory, NewCustRequestCategory } from '../cust-request-category.model';

export type PartialUpdateCustRequestCategory = Partial<ICustRequestCategory> & Pick<ICustRequestCategory, 'id'>;

@Service()
export class CustRequestCategoriesService {
  readonly custRequestCategoriesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly custRequestCategoriesResource = httpResource<ICustRequestCategory[]>(() => {
    const params = this.custRequestCategoriesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of custRequestCategory that have been fetched. It is updated when the custRequestCategoriesResource emits a new value.
   * In case of error while fetching the custRequestCategories, the signal is set to an empty array.
   */
  readonly custRequestCategories = computed(() =>
    this.custRequestCategoriesResource.hasValue() ? this.custRequestCategoriesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/cust-request-categories`;
}

@Service()
export class CustRequestCategoryService extends CustRequestCategoriesService {
  protected readonly http = inject(HttpClient);

  create(custRequestCategory: NewCustRequestCategory): Observable<ICustRequestCategory> {
    return this.http.post<ICustRequestCategory>(this.resourceUrl, custRequestCategory);
  }

  update(custRequestCategory: ICustRequestCategory): Observable<ICustRequestCategory> {
    return this.http.put<ICustRequestCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustRequestCategoryIdentifier(custRequestCategory))}`,
      custRequestCategory,
    );
  }

  partialUpdate(custRequestCategory: PartialUpdateCustRequestCategory): Observable<ICustRequestCategory> {
    return this.http.patch<ICustRequestCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustRequestCategoryIdentifier(custRequestCategory))}`,
      custRequestCategory,
    );
  }

  find(id: number): Observable<ICustRequestCategory> {
    return this.http.get<ICustRequestCategory>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICustRequestCategory[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICustRequestCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCustRequestCategoryIdentifier(custRequestCategory: Pick<ICustRequestCategory, 'id'>): number {
    return custRequestCategory.id;
  }

  compareCustRequestCategory(o1: Pick<ICustRequestCategory, 'id'> | null, o2: Pick<ICustRequestCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustRequestCategoryIdentifier(o1) === this.getCustRequestCategoryIdentifier(o2) : o1 === o2;
  }

  addCustRequestCategoryToCollectionIfMissing<Type extends Pick<ICustRequestCategory, 'id'>>(
    custRequestCategoryCollection: Type[],
    ...custRequestCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const custRequestCategories: Type[] = custRequestCategoriesToCheck.filter(
      custRequestCategoryItem => custRequestCategoryItem !== null && custRequestCategoryItem !== undefined,
    );
    if (custRequestCategories.length > 0) {
      const custRequestCategoryCollectionIdentifiers = custRequestCategoryCollection.map(custRequestCategoryItem =>
        this.getCustRequestCategoryIdentifier(custRequestCategoryItem),
      );
      const custRequestCategoriesToAdd = custRequestCategories.filter(custRequestCategoryItem => {
        const custRequestCategoryIdentifier = this.getCustRequestCategoryIdentifier(custRequestCategoryItem);
        if (custRequestCategoryCollectionIdentifiers.includes(custRequestCategoryIdentifier)) {
          return false;
        }
        custRequestCategoryCollectionIdentifiers.push(custRequestCategoryIdentifier);
        return true;
      });
      return [...custRequestCategoriesToAdd, ...custRequestCategoryCollection];
    }
    return custRequestCategoryCollection;
  }
}
