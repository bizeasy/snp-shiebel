import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ITransactionCategory, NewTransactionCategory } from '../transaction-category.model';

export type PartialUpdateTransactionCategory = Partial<ITransactionCategory> & Pick<ITransactionCategory, 'id'>;

@Service()
export class TransactionCategoriesService {
  readonly transactionCategoriesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly transactionCategoriesResource = httpResource<ITransactionCategory[]>(() => {
    const params = this.transactionCategoriesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of transactionCategory that have been fetched. It is updated when the transactionCategoriesResource emits a new value.
   * In case of error while fetching the transactionCategories, the signal is set to an empty array.
   */
  readonly transactionCategories = computed(() =>
    this.transactionCategoriesResource.hasValue() ? this.transactionCategoriesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/transaction-categories`;
}

@Service()
export class TransactionCategoryService extends TransactionCategoriesService {
  protected readonly http = inject(HttpClient);

  create(transactionCategory: NewTransactionCategory): Observable<ITransactionCategory> {
    return this.http.post<ITransactionCategory>(this.resourceUrl, transactionCategory);
  }

  update(transactionCategory: ITransactionCategory): Observable<ITransactionCategory> {
    return this.http.put<ITransactionCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTransactionCategoryIdentifier(transactionCategory))}`,
      transactionCategory,
    );
  }

  partialUpdate(transactionCategory: PartialUpdateTransactionCategory): Observable<ITransactionCategory> {
    return this.http.patch<ITransactionCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTransactionCategoryIdentifier(transactionCategory))}`,
      transactionCategory,
    );
  }

  find(id: number): Observable<ITransactionCategory> {
    return this.http.get<ITransactionCategory>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITransactionCategory[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITransactionCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTransactionCategoryIdentifier(transactionCategory: Pick<ITransactionCategory, 'id'>): number {
    return transactionCategory.id;
  }

  compareTransactionCategory(o1: Pick<ITransactionCategory, 'id'> | null, o2: Pick<ITransactionCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransactionCategoryIdentifier(o1) === this.getTransactionCategoryIdentifier(o2) : o1 === o2;
  }

  addTransactionCategoryToCollectionIfMissing<Type extends Pick<ITransactionCategory, 'id'>>(
    transactionCategoryCollection: Type[],
    ...transactionCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transactionCategories: Type[] = transactionCategoriesToCheck.filter(
      transactionCategoryItem => transactionCategoryItem !== null && transactionCategoryItem !== undefined,
    );
    if (transactionCategories.length > 0) {
      const transactionCategoryCollectionIdentifiers = transactionCategoryCollection.map(transactionCategoryItem =>
        this.getTransactionCategoryIdentifier(transactionCategoryItem),
      );
      const transactionCategoriesToAdd = transactionCategories.filter(transactionCategoryItem => {
        const transactionCategoryIdentifier = this.getTransactionCategoryIdentifier(transactionCategoryItem);
        if (transactionCategoryCollectionIdentifiers.includes(transactionCategoryIdentifier)) {
          return false;
        }
        transactionCategoryCollectionIdentifiers.push(transactionCategoryIdentifier);
        return true;
      });
      return [...transactionCategoriesToAdd, ...transactionCategoryCollection];
    }
    return transactionCategoryCollection;
  }
}
