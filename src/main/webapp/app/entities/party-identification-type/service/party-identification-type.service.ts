import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartyIdentificationType, NewPartyIdentificationType } from '../party-identification-type.model';

export type PartialUpdatePartyIdentificationType = Partial<IPartyIdentificationType> & Pick<IPartyIdentificationType, 'id'>;

@Service()
export class PartyIdentificationTypesService {
  readonly partyIdentificationTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly partyIdentificationTypesResource = httpResource<IPartyIdentificationType[]>(() => {
    const params = this.partyIdentificationTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partyIdentificationType that have been fetched. It is updated when the partyIdentificationTypesResource emits a new value.
   * In case of error while fetching the partyIdentificationTypes, the signal is set to an empty array.
   */
  readonly partyIdentificationTypes = computed(() =>
    this.partyIdentificationTypesResource.hasValue() ? this.partyIdentificationTypesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/party-identification-types`;
}

@Service()
export class PartyIdentificationTypeService extends PartyIdentificationTypesService {
  protected readonly http = inject(HttpClient);

  create(partyIdentificationType: NewPartyIdentificationType): Observable<IPartyIdentificationType> {
    return this.http.post<IPartyIdentificationType>(this.resourceUrl, partyIdentificationType);
  }

  update(partyIdentificationType: IPartyIdentificationType): Observable<IPartyIdentificationType> {
    return this.http.put<IPartyIdentificationType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartyIdentificationTypeIdentifier(partyIdentificationType))}`,
      partyIdentificationType,
    );
  }

  partialUpdate(partyIdentificationType: PartialUpdatePartyIdentificationType): Observable<IPartyIdentificationType> {
    return this.http.patch<IPartyIdentificationType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartyIdentificationTypeIdentifier(partyIdentificationType))}`,
      partyIdentificationType,
    );
  }

  find(id: number): Observable<IPartyIdentificationType> {
    return this.http.get<IPartyIdentificationType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPartyIdentificationType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPartyIdentificationType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartyIdentificationTypeIdentifier(partyIdentificationType: Pick<IPartyIdentificationType, 'id'>): number {
    return partyIdentificationType.id;
  }

  comparePartyIdentificationType(
    o1: Pick<IPartyIdentificationType, 'id'> | null,
    o2: Pick<IPartyIdentificationType, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getPartyIdentificationTypeIdentifier(o1) === this.getPartyIdentificationTypeIdentifier(o2) : o1 === o2;
  }

  addPartyIdentificationTypeToCollectionIfMissing<Type extends Pick<IPartyIdentificationType, 'id'>>(
    partyIdentificationTypeCollection: Type[],
    ...partyIdentificationTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partyIdentificationTypes: Type[] = partyIdentificationTypesToCheck.filter(
      partyIdentificationTypeItem => partyIdentificationTypeItem !== null && partyIdentificationTypeItem !== undefined,
    );
    if (partyIdentificationTypes.length > 0) {
      const partyIdentificationTypeCollectionIdentifiers = partyIdentificationTypeCollection.map(partyIdentificationTypeItem =>
        this.getPartyIdentificationTypeIdentifier(partyIdentificationTypeItem),
      );
      const partyIdentificationTypesToAdd = partyIdentificationTypes.filter(partyIdentificationTypeItem => {
        const partyIdentificationTypeIdentifier = this.getPartyIdentificationTypeIdentifier(partyIdentificationTypeItem);
        if (partyIdentificationTypeCollectionIdentifiers.includes(partyIdentificationTypeIdentifier)) {
          return false;
        }
        partyIdentificationTypeCollectionIdentifiers.push(partyIdentificationTypeIdentifier);
        return true;
      });
      return [...partyIdentificationTypesToAdd, ...partyIdentificationTypeCollection];
    }
    return partyIdentificationTypeCollection;
  }
}
