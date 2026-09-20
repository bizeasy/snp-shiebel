import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInventoryItemVariance, NewInventoryItemVariance } from '../inventory-item-variance.model';

export type PartialUpdateInventoryItemVariance = Partial<IInventoryItemVariance> & Pick<IInventoryItemVariance, 'id'>;

@Service()
export class InventoryItemVariancesService {
  readonly inventoryItemVariancesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly inventoryItemVariancesResource = httpResource<IInventoryItemVariance[]>(() => {
    const params = this.inventoryItemVariancesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of inventoryItemVariance that have been fetched. It is updated when the inventoryItemVariancesResource emits a new value.
   * In case of error while fetching the inventoryItemVariances, the signal is set to an empty array.
   */
  readonly inventoryItemVariances = computed(() =>
    this.inventoryItemVariancesResource.hasValue() ? this.inventoryItemVariancesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/inventory-item-variances`;
}

@Service()
export class InventoryItemVarianceService extends InventoryItemVariancesService {
  protected readonly http = inject(HttpClient);

  create(inventoryItemVariance: NewInventoryItemVariance): Observable<IInventoryItemVariance> {
    return this.http.post<IInventoryItemVariance>(this.resourceUrl, inventoryItemVariance);
  }

  update(inventoryItemVariance: IInventoryItemVariance): Observable<IInventoryItemVariance> {
    return this.http.put<IInventoryItemVariance>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInventoryItemVarianceIdentifier(inventoryItemVariance))}`,
      inventoryItemVariance,
    );
  }

  partialUpdate(inventoryItemVariance: PartialUpdateInventoryItemVariance): Observable<IInventoryItemVariance> {
    return this.http.patch<IInventoryItemVariance>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInventoryItemVarianceIdentifier(inventoryItemVariance))}`,
      inventoryItemVariance,
    );
  }

  find(id: number): Observable<IInventoryItemVariance> {
    return this.http.get<IInventoryItemVariance>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IInventoryItemVariance[]>> {
    const options = createRequestOption(req);
    return this.http.get<IInventoryItemVariance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInventoryItemVarianceIdentifier(inventoryItemVariance: Pick<IInventoryItemVariance, 'id'>): number {
    return inventoryItemVariance.id;
  }

  compareInventoryItemVariance(o1: Pick<IInventoryItemVariance, 'id'> | null, o2: Pick<IInventoryItemVariance, 'id'> | null): boolean {
    return o1 && o2 ? this.getInventoryItemVarianceIdentifier(o1) === this.getInventoryItemVarianceIdentifier(o2) : o1 === o2;
  }

  addInventoryItemVarianceToCollectionIfMissing<Type extends Pick<IInventoryItemVariance, 'id'>>(
    inventoryItemVarianceCollection: Type[],
    ...inventoryItemVariancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inventoryItemVariances: Type[] = inventoryItemVariancesToCheck.filter(
      inventoryItemVarianceItem => inventoryItemVarianceItem !== null && inventoryItemVarianceItem !== undefined,
    );
    if (inventoryItemVariances.length > 0) {
      const inventoryItemVarianceCollectionIdentifiers = inventoryItemVarianceCollection.map(inventoryItemVarianceItem =>
        this.getInventoryItemVarianceIdentifier(inventoryItemVarianceItem),
      );
      const inventoryItemVariancesToAdd = inventoryItemVariances.filter(inventoryItemVarianceItem => {
        const inventoryItemVarianceIdentifier = this.getInventoryItemVarianceIdentifier(inventoryItemVarianceItem);
        if (inventoryItemVarianceCollectionIdentifiers.includes(inventoryItemVarianceIdentifier)) {
          return false;
        }
        inventoryItemVarianceCollectionIdentifiers.push(inventoryItemVarianceIdentifier);
        return true;
      });
      return [...inventoryItemVariancesToAdd, ...inventoryItemVarianceCollection];
    }
    return inventoryItemVarianceCollection;
  }
}
