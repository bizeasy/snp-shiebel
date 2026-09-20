import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartyIdentification, NewPartyIdentification } from '../party-identification.model';

export type PartialUpdatePartyIdentification = Partial<IPartyIdentification> & Pick<IPartyIdentification, 'id'>;

@Service()
export class PartyIdentificationsService {
  readonly partyIdentificationsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly partyIdentificationsResource = httpResource<IPartyIdentification[]>(() => {
    const params = this.partyIdentificationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partyIdentification that have been fetched. It is updated when the partyIdentificationsResource emits a new value.
   * In case of error while fetching the partyIdentifications, the signal is set to an empty array.
   */
  readonly partyIdentifications = computed(() =>
    this.partyIdentificationsResource.hasValue() ? this.partyIdentificationsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/party-identifications`;
}

@Service()
export class PartyIdentificationService extends PartyIdentificationsService {
  protected readonly http = inject(HttpClient);

  create(partyIdentification: NewPartyIdentification): Observable<IPartyIdentification> {
    return this.http.post<IPartyIdentification>(this.resourceUrl, partyIdentification);
  }

  update(partyIdentification: IPartyIdentification): Observable<IPartyIdentification> {
    return this.http.put<IPartyIdentification>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartyIdentificationIdentifier(partyIdentification))}`,
      partyIdentification,
    );
  }

  partialUpdate(partyIdentification: PartialUpdatePartyIdentification): Observable<IPartyIdentification> {
    return this.http.patch<IPartyIdentification>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartyIdentificationIdentifier(partyIdentification))}`,
      partyIdentification,
    );
  }

  find(id: number): Observable<IPartyIdentification> {
    return this.http.get<IPartyIdentification>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPartyIdentification[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPartyIdentification[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartyIdentificationIdentifier(partyIdentification: Pick<IPartyIdentification, 'id'>): number {
    return partyIdentification.id;
  }

  comparePartyIdentification(o1: Pick<IPartyIdentification, 'id'> | null, o2: Pick<IPartyIdentification, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartyIdentificationIdentifier(o1) === this.getPartyIdentificationIdentifier(o2) : o1 === o2;
  }

  addPartyIdentificationToCollectionIfMissing<Type extends Pick<IPartyIdentification, 'id'>>(
    partyIdentificationCollection: Type[],
    ...partyIdentificationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partyIdentifications: Type[] = partyIdentificationsToCheck.filter(
      partyIdentificationItem => partyIdentificationItem !== null && partyIdentificationItem !== undefined,
    );
    if (partyIdentifications.length > 0) {
      const partyIdentificationCollectionIdentifiers = partyIdentificationCollection.map(partyIdentificationItem =>
        this.getPartyIdentificationIdentifier(partyIdentificationItem),
      );
      const partyIdentificationsToAdd = partyIdentifications.filter(partyIdentificationItem => {
        const partyIdentificationIdentifier = this.getPartyIdentificationIdentifier(partyIdentificationItem);
        if (partyIdentificationCollectionIdentifiers.includes(partyIdentificationIdentifier)) {
          return false;
        }
        partyIdentificationCollectionIdentifiers.push(partyIdentificationIdentifier);
        return true;
      });
      return [...partyIdentificationsToAdd, ...partyIdentificationCollection];
    }
    return partyIdentificationCollection;
  }
}
