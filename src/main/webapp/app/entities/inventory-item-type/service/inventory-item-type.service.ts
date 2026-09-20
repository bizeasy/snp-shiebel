import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInventoryItemType, NewInventoryItemType } from '../inventory-item-type.model';

export type PartialUpdateInventoryItemType = Partial<IInventoryItemType> & Pick<IInventoryItemType, 'id'>;

@Service()
export class InventoryItemTypesService {
  readonly inventoryItemTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly inventoryItemTypesResource = httpResource<IInventoryItemType[]>(() => {
    const params = this.inventoryItemTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of inventoryItemType that have been fetched. It is updated when the inventoryItemTypesResource emits a new value.
   * In case of error while fetching the inventoryItemTypes, the signal is set to an empty array.
   */
  readonly inventoryItemTypes = computed(() => (this.inventoryItemTypesResource.hasValue() ? this.inventoryItemTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/inventory-item-types`;
}

@Service()
export class InventoryItemTypeService extends InventoryItemTypesService {
  protected readonly http = inject(HttpClient);

  create(inventoryItemType: NewInventoryItemType): Observable<IInventoryItemType> {
    return this.http.post<IInventoryItemType>(this.resourceUrl, inventoryItemType);
  }

  update(inventoryItemType: IInventoryItemType): Observable<IInventoryItemType> {
    return this.http.put<IInventoryItemType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInventoryItemTypeIdentifier(inventoryItemType))}`,
      inventoryItemType,
    );
  }

  partialUpdate(inventoryItemType: PartialUpdateInventoryItemType): Observable<IInventoryItemType> {
    return this.http.patch<IInventoryItemType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInventoryItemTypeIdentifier(inventoryItemType))}`,
      inventoryItemType,
    );
  }

  find(id: number): Observable<IInventoryItemType> {
    return this.http.get<IInventoryItemType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IInventoryItemType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IInventoryItemType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInventoryItemTypeIdentifier(inventoryItemType: Pick<IInventoryItemType, 'id'>): number {
    return inventoryItemType.id;
  }

  compareInventoryItemType(o1: Pick<IInventoryItemType, 'id'> | null, o2: Pick<IInventoryItemType, 'id'> | null): boolean {
    return o1 && o2 ? this.getInventoryItemTypeIdentifier(o1) === this.getInventoryItemTypeIdentifier(o2) : o1 === o2;
  }

  addInventoryItemTypeToCollectionIfMissing<Type extends Pick<IInventoryItemType, 'id'>>(
    inventoryItemTypeCollection: Type[],
    ...inventoryItemTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inventoryItemTypes: Type[] = inventoryItemTypesToCheck.filter(
      inventoryItemTypeItem => inventoryItemTypeItem !== null && inventoryItemTypeItem !== undefined,
    );
    if (inventoryItemTypes.length > 0) {
      const inventoryItemTypeCollectionIdentifiers = inventoryItemTypeCollection.map(inventoryItemTypeItem =>
        this.getInventoryItemTypeIdentifier(inventoryItemTypeItem),
      );
      const inventoryItemTypesToAdd = inventoryItemTypes.filter(inventoryItemTypeItem => {
        const inventoryItemTypeIdentifier = this.getInventoryItemTypeIdentifier(inventoryItemTypeItem);
        if (inventoryItemTypeCollectionIdentifiers.includes(inventoryItemTypeIdentifier)) {
          return false;
        }
        inventoryItemTypeCollectionIdentifiers.push(inventoryItemTypeIdentifier);
        return true;
      });
      return [...inventoryItemTypesToAdd, ...inventoryItemTypeCollection];
    }
    return inventoryItemTypeCollection;
  }
}
