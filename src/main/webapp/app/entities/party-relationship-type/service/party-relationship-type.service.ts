import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartyRelationshipType, NewPartyRelationshipType } from '../party-relationship-type.model';

export type PartialUpdatePartyRelationshipType = Partial<IPartyRelationshipType> & Pick<IPartyRelationshipType, 'id'>;

@Service()
export class PartyRelationshipTypesService {
  readonly partyRelationshipTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly partyRelationshipTypesResource = httpResource<IPartyRelationshipType[]>(() => {
    const params = this.partyRelationshipTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partyRelationshipType that have been fetched. It is updated when the partyRelationshipTypesResource emits a new value.
   * In case of error while fetching the partyRelationshipTypes, the signal is set to an empty array.
   */
  readonly partyRelationshipTypes = computed(() =>
    this.partyRelationshipTypesResource.hasValue() ? this.partyRelationshipTypesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/party-relationship-types`;
}

@Service()
export class PartyRelationshipTypeService extends PartyRelationshipTypesService {
  protected readonly http = inject(HttpClient);

  create(partyRelationshipType: NewPartyRelationshipType): Observable<IPartyRelationshipType> {
    return this.http.post<IPartyRelationshipType>(this.resourceUrl, partyRelationshipType);
  }

  update(partyRelationshipType: IPartyRelationshipType): Observable<IPartyRelationshipType> {
    return this.http.put<IPartyRelationshipType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartyRelationshipTypeIdentifier(partyRelationshipType))}`,
      partyRelationshipType,
    );
  }

  partialUpdate(partyRelationshipType: PartialUpdatePartyRelationshipType): Observable<IPartyRelationshipType> {
    return this.http.patch<IPartyRelationshipType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartyRelationshipTypeIdentifier(partyRelationshipType))}`,
      partyRelationshipType,
    );
  }

  find(id: number): Observable<IPartyRelationshipType> {
    return this.http.get<IPartyRelationshipType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPartyRelationshipType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPartyRelationshipType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartyRelationshipTypeIdentifier(partyRelationshipType: Pick<IPartyRelationshipType, 'id'>): number {
    return partyRelationshipType.id;
  }

  comparePartyRelationshipType(o1: Pick<IPartyRelationshipType, 'id'> | null, o2: Pick<IPartyRelationshipType, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartyRelationshipTypeIdentifier(o1) === this.getPartyRelationshipTypeIdentifier(o2) : o1 === o2;
  }

  addPartyRelationshipTypeToCollectionIfMissing<Type extends Pick<IPartyRelationshipType, 'id'>>(
    partyRelationshipTypeCollection: Type[],
    ...partyRelationshipTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partyRelationshipTypes: Type[] = partyRelationshipTypesToCheck.filter(
      partyRelationshipTypeItem => partyRelationshipTypeItem !== null && partyRelationshipTypeItem !== undefined,
    );
    if (partyRelationshipTypes.length > 0) {
      const partyRelationshipTypeCollectionIdentifiers = partyRelationshipTypeCollection.map(partyRelationshipTypeItem =>
        this.getPartyRelationshipTypeIdentifier(partyRelationshipTypeItem),
      );
      const partyRelationshipTypesToAdd = partyRelationshipTypes.filter(partyRelationshipTypeItem => {
        const partyRelationshipTypeIdentifier = this.getPartyRelationshipTypeIdentifier(partyRelationshipTypeItem);
        if (partyRelationshipTypeCollectionIdentifiers.includes(partyRelationshipTypeIdentifier)) {
          return false;
        }
        partyRelationshipTypeCollectionIdentifiers.push(partyRelationshipTypeIdentifier);
        return true;
      });
      return [...partyRelationshipTypesToAdd, ...partyRelationshipTypeCollection];
    }
    return partyRelationshipTypeCollection;
  }
}
