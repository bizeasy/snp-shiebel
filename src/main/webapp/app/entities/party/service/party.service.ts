import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IParty, NewParty } from '../party.model';

export type PartialUpdateParty = Partial<IParty> & Pick<IParty, 'id'>;

type RestOf<T extends IParty | NewParty> = Omit<T, 'birthDate'> & {
  birthDate?: string | null;
};

export type RestParty = RestOf<IParty>;

export type NewRestParty = RestOf<NewParty>;

export type PartialUpdateRestParty = RestOf<PartialUpdateParty>;

@Service()
export class PartiesService {
  readonly partiesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly partiesResource = httpResource<RestParty[]>(() => {
    const params = this.partiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of party that have been fetched. It is updated when the partiesResource emits a new value.
   * In case of error while fetching the parties, the signal is set to an empty array.
   */
  readonly parties = computed(() =>
    (this.partiesResource.hasValue() ? this.partiesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/parties`;

  protected convertValueFromServer(restParty: RestParty): IParty {
    return {
      ...restParty,
      birthDate: restParty.birthDate ? dayjs(restParty.birthDate) : undefined,
    };
  }
}

@Service()
export class PartyService extends PartiesService {
  protected readonly http = inject(HttpClient);

  create(party: NewParty): Observable<IParty> {
    const copy = this.convertValueFromClient(party);
    return this.http.post<RestParty>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(party: IParty): Observable<IParty> {
    const copy = this.convertValueFromClient(party);
    return this.http
      .put<RestParty>(`${this.resourceUrl}/${encodeURIComponent(this.getPartyIdentifier(party))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(party: PartialUpdateParty): Observable<IParty> {
    const copy = this.convertValueFromClient(party);
    return this.http
      .patch<RestParty>(`${this.resourceUrl}/${encodeURIComponent(this.getPartyIdentifier(party))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IParty> {
    return this.http.get<RestParty>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IParty[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestParty[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartyIdentifier(party: Pick<IParty, 'id'>): number {
    return party.id;
  }

  compareParty(o1: Pick<IParty, 'id'> | null, o2: Pick<IParty, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartyIdentifier(o1) === this.getPartyIdentifier(o2) : o1 === o2;
  }

  addPartyToCollectionIfMissing<Type extends Pick<IParty, 'id'>>(
    partyCollection: Type[],
    ...partiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const parties: Type[] = partiesToCheck.filter(partyItem => partyItem !== null && partyItem !== undefined);
    if (parties.length > 0) {
      const partyCollectionIdentifiers = partyCollection.map(partyItem => this.getPartyIdentifier(partyItem));
      const partiesToAdd = parties.filter(partyItem => {
        const partyIdentifier = this.getPartyIdentifier(partyItem);
        if (partyCollectionIdentifiers.includes(partyIdentifier)) {
          return false;
        }
        partyCollectionIdentifiers.push(partyIdentifier);
        return true;
      });
      return [...partiesToAdd, ...partyCollection];
    }
    return partyCollection;
  }

  protected convertValueFromClient<T extends IParty | NewParty | PartialUpdateParty>(party: T): RestOf<T> {
    return {
      ...party,
      birthDate: party.birthDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestParty): IParty {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestParty[]): IParty[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
