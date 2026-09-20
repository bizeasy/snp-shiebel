import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IUomType, NewUomType } from '../uom-type.model';

export type PartialUpdateUomType = Partial<IUomType> & Pick<IUomType, 'id'>;

@Service()
export class UomTypesService {
  readonly uomTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly uomTypesResource = httpResource<IUomType[]>(() => {
    const params = this.uomTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of uomType that have been fetched. It is updated when the uomTypesResource emits a new value.
   * In case of error while fetching the uomTypes, the signal is set to an empty array.
   */
  readonly uomTypes = computed(() => (this.uomTypesResource.hasValue() ? this.uomTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/uom-types`;
}

@Service()
export class UomTypeService extends UomTypesService {
  protected readonly http = inject(HttpClient);

  create(uomType: NewUomType): Observable<IUomType> {
    return this.http.post<IUomType>(this.resourceUrl, uomType);
  }

  update(uomType: IUomType): Observable<IUomType> {
    return this.http.put<IUomType>(`${this.resourceUrl}/${encodeURIComponent(this.getUomTypeIdentifier(uomType))}`, uomType);
  }

  partialUpdate(uomType: PartialUpdateUomType): Observable<IUomType> {
    return this.http.patch<IUomType>(`${this.resourceUrl}/${encodeURIComponent(this.getUomTypeIdentifier(uomType))}`, uomType);
  }

  find(id: number): Observable<IUomType> {
    return this.http.get<IUomType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IUomType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IUomType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getUomTypeIdentifier(uomType: Pick<IUomType, 'id'>): number {
    return uomType.id;
  }

  compareUomType(o1: Pick<IUomType, 'id'> | null, o2: Pick<IUomType, 'id'> | null): boolean {
    return o1 && o2 ? this.getUomTypeIdentifier(o1) === this.getUomTypeIdentifier(o2) : o1 === o2;
  }

  addUomTypeToCollectionIfMissing<Type extends Pick<IUomType, 'id'>>(
    uomTypeCollection: Type[],
    ...uomTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const uomTypes: Type[] = uomTypesToCheck.filter(uomTypeItem => uomTypeItem !== null && uomTypeItem !== undefined);
    if (uomTypes.length > 0) {
      const uomTypeCollectionIdentifiers = uomTypeCollection.map(uomTypeItem => this.getUomTypeIdentifier(uomTypeItem));
      const uomTypesToAdd = uomTypes.filter(uomTypeItem => {
        const uomTypeIdentifier = this.getUomTypeIdentifier(uomTypeItem);
        if (uomTypeCollectionIdentifiers.includes(uomTypeIdentifier)) {
          return false;
        }
        uomTypeCollectionIdentifiers.push(uomTypeIdentifier);
        return true;
      });
      return [...uomTypesToAdd, ...uomTypeCollection];
    }
    return uomTypeCollection;
  }
}
