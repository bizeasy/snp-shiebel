import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IReturnHeaderType, NewReturnHeaderType } from '../return-header-type.model';

export type PartialUpdateReturnHeaderType = Partial<IReturnHeaderType> & Pick<IReturnHeaderType, 'id'>;

@Service()
export class ReturnHeaderTypesService {
  readonly returnHeaderTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly returnHeaderTypesResource = httpResource<IReturnHeaderType[]>(() => {
    const params = this.returnHeaderTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of returnHeaderType that have been fetched. It is updated when the returnHeaderTypesResource emits a new value.
   * In case of error while fetching the returnHeaderTypes, the signal is set to an empty array.
   */
  readonly returnHeaderTypes = computed(() => (this.returnHeaderTypesResource.hasValue() ? this.returnHeaderTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/return-header-types`;
}

@Service()
export class ReturnHeaderTypeService extends ReturnHeaderTypesService {
  protected readonly http = inject(HttpClient);

  create(returnHeaderType: NewReturnHeaderType): Observable<IReturnHeaderType> {
    return this.http.post<IReturnHeaderType>(this.resourceUrl, returnHeaderType);
  }

  update(returnHeaderType: IReturnHeaderType): Observable<IReturnHeaderType> {
    return this.http.put<IReturnHeaderType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getReturnHeaderTypeIdentifier(returnHeaderType))}`,
      returnHeaderType,
    );
  }

  partialUpdate(returnHeaderType: PartialUpdateReturnHeaderType): Observable<IReturnHeaderType> {
    return this.http.patch<IReturnHeaderType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getReturnHeaderTypeIdentifier(returnHeaderType))}`,
      returnHeaderType,
    );
  }

  find(id: number): Observable<IReturnHeaderType> {
    return this.http.get<IReturnHeaderType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IReturnHeaderType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IReturnHeaderType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getReturnHeaderTypeIdentifier(returnHeaderType: Pick<IReturnHeaderType, 'id'>): number {
    return returnHeaderType.id;
  }

  compareReturnHeaderType(o1: Pick<IReturnHeaderType, 'id'> | null, o2: Pick<IReturnHeaderType, 'id'> | null): boolean {
    return o1 && o2 ? this.getReturnHeaderTypeIdentifier(o1) === this.getReturnHeaderTypeIdentifier(o2) : o1 === o2;
  }

  addReturnHeaderTypeToCollectionIfMissing<Type extends Pick<IReturnHeaderType, 'id'>>(
    returnHeaderTypeCollection: Type[],
    ...returnHeaderTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const returnHeaderTypes: Type[] = returnHeaderTypesToCheck.filter(
      returnHeaderTypeItem => returnHeaderTypeItem !== null && returnHeaderTypeItem !== undefined,
    );
    if (returnHeaderTypes.length > 0) {
      const returnHeaderTypeCollectionIdentifiers = returnHeaderTypeCollection.map(returnHeaderTypeItem =>
        this.getReturnHeaderTypeIdentifier(returnHeaderTypeItem),
      );
      const returnHeaderTypesToAdd = returnHeaderTypes.filter(returnHeaderTypeItem => {
        const returnHeaderTypeIdentifier = this.getReturnHeaderTypeIdentifier(returnHeaderTypeItem);
        if (returnHeaderTypeCollectionIdentifiers.includes(returnHeaderTypeIdentifier)) {
          return false;
        }
        returnHeaderTypeCollectionIdentifiers.push(returnHeaderTypeIdentifier);
        return true;
      });
      return [...returnHeaderTypesToAdd, ...returnHeaderTypeCollection];
    }
    return returnHeaderTypeCollection;
  }
}
