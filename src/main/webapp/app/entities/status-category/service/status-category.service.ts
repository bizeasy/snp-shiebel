import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IStatusCategory, NewStatusCategory } from '../status-category.model';

export type PartialUpdateStatusCategory = Partial<IStatusCategory> & Pick<IStatusCategory, 'id'>;

@Service()
export class StatusCategoriesService {
  readonly statusCategoriesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly statusCategoriesResource = httpResource<IStatusCategory[]>(() => {
    const params = this.statusCategoriesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of statusCategory that have been fetched. It is updated when the statusCategoriesResource emits a new value.
   * In case of error while fetching the statusCategories, the signal is set to an empty array.
   */
  readonly statusCategories = computed(() => (this.statusCategoriesResource.hasValue() ? this.statusCategoriesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/status-categories`;
}

@Service()
export class StatusCategoryService extends StatusCategoriesService {
  protected readonly http = inject(HttpClient);

  create(statusCategory: NewStatusCategory): Observable<IStatusCategory> {
    return this.http.post<IStatusCategory>(this.resourceUrl, statusCategory);
  }

  update(statusCategory: IStatusCategory): Observable<IStatusCategory> {
    return this.http.put<IStatusCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getStatusCategoryIdentifier(statusCategory))}`,
      statusCategory,
    );
  }

  partialUpdate(statusCategory: PartialUpdateStatusCategory): Observable<IStatusCategory> {
    return this.http.patch<IStatusCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getStatusCategoryIdentifier(statusCategory))}`,
      statusCategory,
    );
  }

  find(id: number): Observable<IStatusCategory> {
    return this.http.get<IStatusCategory>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IStatusCategory[]>> {
    const options = createRequestOption(req);
    return this.http.get<IStatusCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getStatusCategoryIdentifier(statusCategory: Pick<IStatusCategory, 'id'>): number {
    return statusCategory.id;
  }

  compareStatusCategory(o1: Pick<IStatusCategory, 'id'> | null, o2: Pick<IStatusCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getStatusCategoryIdentifier(o1) === this.getStatusCategoryIdentifier(o2) : o1 === o2;
  }

  addStatusCategoryToCollectionIfMissing<Type extends Pick<IStatusCategory, 'id'>>(
    statusCategoryCollection: Type[],
    ...statusCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const statusCategories: Type[] = statusCategoriesToCheck.filter(
      statusCategoryItem => statusCategoryItem !== null && statusCategoryItem !== undefined,
    );
    if (statusCategories.length > 0) {
      const statusCategoryCollectionIdentifiers = statusCategoryCollection.map(statusCategoryItem =>
        this.getStatusCategoryIdentifier(statusCategoryItem),
      );
      const statusCategoriesToAdd = statusCategories.filter(statusCategoryItem => {
        const statusCategoryIdentifier = this.getStatusCategoryIdentifier(statusCategoryItem);
        if (statusCategoryCollectionIdentifiers.includes(statusCategoryIdentifier)) {
          return false;
        }
        statusCategoryCollectionIdentifiers.push(statusCategoryIdentifier);
        return true;
      });
      return [...statusCategoriesToAdd, ...statusCategoryCollection];
    }
    return statusCategoryCollection;
  }
}
