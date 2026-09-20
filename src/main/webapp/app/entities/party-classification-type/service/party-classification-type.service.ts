import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartyClassificationType, NewPartyClassificationType } from '../party-classification-type.model';

export type PartialUpdatePartyClassificationType = Partial<IPartyClassificationType> & Pick<IPartyClassificationType, 'id'>;

@Service()
export class PartyClassificationTypesService {
  readonly partyClassificationTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly partyClassificationTypesResource = httpResource<IPartyClassificationType[]>(() => {
    const params = this.partyClassificationTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partyClassificationType that have been fetched. It is updated when the partyClassificationTypesResource emits a new value.
   * In case of error while fetching the partyClassificationTypes, the signal is set to an empty array.
   */
  readonly partyClassificationTypes = computed(() =>
    this.partyClassificationTypesResource.hasValue() ? this.partyClassificationTypesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/party-classification-types`;
}

@Service()
export class PartyClassificationTypeService extends PartyClassificationTypesService {
  protected readonly http = inject(HttpClient);

  create(partyClassificationType: NewPartyClassificationType): Observable<IPartyClassificationType> {
    return this.http.post<IPartyClassificationType>(this.resourceUrl, partyClassificationType);
  }

  update(partyClassificationType: IPartyClassificationType): Observable<IPartyClassificationType> {
    return this.http.put<IPartyClassificationType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartyClassificationTypeIdentifier(partyClassificationType))}`,
      partyClassificationType,
    );
  }

  partialUpdate(partyClassificationType: PartialUpdatePartyClassificationType): Observable<IPartyClassificationType> {
    return this.http.patch<IPartyClassificationType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartyClassificationTypeIdentifier(partyClassificationType))}`,
      partyClassificationType,
    );
  }

  find(id: number): Observable<IPartyClassificationType> {
    return this.http.get<IPartyClassificationType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPartyClassificationType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPartyClassificationType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartyClassificationTypeIdentifier(partyClassificationType: Pick<IPartyClassificationType, 'id'>): number {
    return partyClassificationType.id;
  }

  comparePartyClassificationType(
    o1: Pick<IPartyClassificationType, 'id'> | null,
    o2: Pick<IPartyClassificationType, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getPartyClassificationTypeIdentifier(o1) === this.getPartyClassificationTypeIdentifier(o2) : o1 === o2;
  }

  addPartyClassificationTypeToCollectionIfMissing<Type extends Pick<IPartyClassificationType, 'id'>>(
    partyClassificationTypeCollection: Type[],
    ...partyClassificationTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partyClassificationTypes: Type[] = partyClassificationTypesToCheck.filter(
      partyClassificationTypeItem => partyClassificationTypeItem !== null && partyClassificationTypeItem !== undefined,
    );
    if (partyClassificationTypes.length > 0) {
      const partyClassificationTypeCollectionIdentifiers = partyClassificationTypeCollection.map(partyClassificationTypeItem =>
        this.getPartyClassificationTypeIdentifier(partyClassificationTypeItem),
      );
      const partyClassificationTypesToAdd = partyClassificationTypes.filter(partyClassificationTypeItem => {
        const partyClassificationTypeIdentifier = this.getPartyClassificationTypeIdentifier(partyClassificationTypeItem);
        if (partyClassificationTypeCollectionIdentifiers.includes(partyClassificationTypeIdentifier)) {
          return false;
        }
        partyClassificationTypeCollectionIdentifiers.push(partyClassificationTypeIdentifier);
        return true;
      });
      return [...partyClassificationTypesToAdd, ...partyClassificationTypeCollection];
    }
    return partyClassificationTypeCollection;
  }
}
