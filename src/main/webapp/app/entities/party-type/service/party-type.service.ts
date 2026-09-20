import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartyType, NewPartyType } from '../party-type.model';

export type PartialUpdatePartyType = Partial<IPartyType> & Pick<IPartyType, 'id'>;

@Service()
export class PartyTypesService {
  readonly partyTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly partyTypesResource = httpResource<IPartyType[]>(() => {
    const params = this.partyTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partyType that have been fetched. It is updated when the partyTypesResource emits a new value.
   * In case of error while fetching the partyTypes, the signal is set to an empty array.
   */
  readonly partyTypes = computed(() => (this.partyTypesResource.hasValue() ? this.partyTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/party-types`;
}

@Service()
export class PartyTypeService extends PartyTypesService {
  protected readonly http = inject(HttpClient);

  create(partyType: NewPartyType): Observable<IPartyType> {
    return this.http.post<IPartyType>(this.resourceUrl, partyType);
  }

  update(partyType: IPartyType): Observable<IPartyType> {
    return this.http.put<IPartyType>(`${this.resourceUrl}/${encodeURIComponent(this.getPartyTypeIdentifier(partyType))}`, partyType);
  }

  partialUpdate(partyType: PartialUpdatePartyType): Observable<IPartyType> {
    return this.http.patch<IPartyType>(`${this.resourceUrl}/${encodeURIComponent(this.getPartyTypeIdentifier(partyType))}`, partyType);
  }

  find(id: number): Observable<IPartyType> {
    return this.http.get<IPartyType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPartyType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPartyType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartyTypeIdentifier(partyType: Pick<IPartyType, 'id'>): number {
    return partyType.id;
  }

  comparePartyType(o1: Pick<IPartyType, 'id'> | null, o2: Pick<IPartyType, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartyTypeIdentifier(o1) === this.getPartyTypeIdentifier(o2) : o1 === o2;
  }

  addPartyTypeToCollectionIfMissing<Type extends Pick<IPartyType, 'id'>>(
    partyTypeCollection: Type[],
    ...partyTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partyTypes: Type[] = partyTypesToCheck.filter(partyTypeItem => partyTypeItem !== null && partyTypeItem !== undefined);
    if (partyTypes.length > 0) {
      const partyTypeCollectionIdentifiers = partyTypeCollection.map(partyTypeItem => this.getPartyTypeIdentifier(partyTypeItem));
      const partyTypesToAdd = partyTypes.filter(partyTypeItem => {
        const partyTypeIdentifier = this.getPartyTypeIdentifier(partyTypeItem);
        if (partyTypeCollectionIdentifiers.includes(partyTypeIdentifier)) {
          return false;
        }
        partyTypeCollectionIdentifiers.push(partyTypeIdentifier);
        return true;
      });
      return [...partyTypesToAdd, ...partyTypeCollection];
    }
    return partyTypeCollection;
  }
}
