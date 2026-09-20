import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartyRelationship, NewPartyRelationship } from '../party-relationship.model';

export type PartialUpdatePartyRelationship = Partial<IPartyRelationship> & Pick<IPartyRelationship, 'id'>;

type RestOf<T extends IPartyRelationship | NewPartyRelationship> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestPartyRelationship = RestOf<IPartyRelationship>;

export type NewRestPartyRelationship = RestOf<NewPartyRelationship>;

export type PartialUpdateRestPartyRelationship = RestOf<PartialUpdatePartyRelationship>;

@Service()
export class PartyRelationshipsService {
  readonly partyRelationshipsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly partyRelationshipsResource = httpResource<RestPartyRelationship[]>(() => {
    const params = this.partyRelationshipsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partyRelationship that have been fetched. It is updated when the partyRelationshipsResource emits a new value.
   * In case of error while fetching the partyRelationships, the signal is set to an empty array.
   */
  readonly partyRelationships = computed(() =>
    (this.partyRelationshipsResource.hasValue() ? this.partyRelationshipsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/party-relationships`;

  protected convertValueFromServer(restPartyRelationship: RestPartyRelationship): IPartyRelationship {
    return {
      ...restPartyRelationship,
      fromDate: restPartyRelationship.fromDate ? dayjs(restPartyRelationship.fromDate) : undefined,
      thruDate: restPartyRelationship.thruDate ? dayjs(restPartyRelationship.thruDate) : undefined,
    };
  }
}

@Service()
export class PartyRelationshipService extends PartyRelationshipsService {
  protected readonly http = inject(HttpClient);

  create(partyRelationship: NewPartyRelationship): Observable<IPartyRelationship> {
    const copy = this.convertValueFromClient(partyRelationship);
    return this.http.post<RestPartyRelationship>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(partyRelationship: IPartyRelationship): Observable<IPartyRelationship> {
    const copy = this.convertValueFromClient(partyRelationship);
    return this.http
      .put<RestPartyRelationship>(`${this.resourceUrl}/${encodeURIComponent(this.getPartyRelationshipIdentifier(partyRelationship))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(partyRelationship: PartialUpdatePartyRelationship): Observable<IPartyRelationship> {
    const copy = this.convertValueFromClient(partyRelationship);
    return this.http
      .patch<RestPartyRelationship>(
        `${this.resourceUrl}/${encodeURIComponent(this.getPartyRelationshipIdentifier(partyRelationship))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPartyRelationship> {
    return this.http
      .get<RestPartyRelationship>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPartyRelationship[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPartyRelationship[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartyRelationshipIdentifier(partyRelationship: Pick<IPartyRelationship, 'id'>): number {
    return partyRelationship.id;
  }

  comparePartyRelationship(o1: Pick<IPartyRelationship, 'id'> | null, o2: Pick<IPartyRelationship, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartyRelationshipIdentifier(o1) === this.getPartyRelationshipIdentifier(o2) : o1 === o2;
  }

  addPartyRelationshipToCollectionIfMissing<Type extends Pick<IPartyRelationship, 'id'>>(
    partyRelationshipCollection: Type[],
    ...partyRelationshipsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partyRelationships: Type[] = partyRelationshipsToCheck.filter(
      partyRelationshipItem => partyRelationshipItem !== null && partyRelationshipItem !== undefined,
    );
    if (partyRelationships.length > 0) {
      const partyRelationshipCollectionIdentifiers = partyRelationshipCollection.map(partyRelationshipItem =>
        this.getPartyRelationshipIdentifier(partyRelationshipItem),
      );
      const partyRelationshipsToAdd = partyRelationships.filter(partyRelationshipItem => {
        const partyRelationshipIdentifier = this.getPartyRelationshipIdentifier(partyRelationshipItem);
        if (partyRelationshipCollectionIdentifiers.includes(partyRelationshipIdentifier)) {
          return false;
        }
        partyRelationshipCollectionIdentifiers.push(partyRelationshipIdentifier);
        return true;
      });
      return [...partyRelationshipsToAdd, ...partyRelationshipCollection];
    }
    return partyRelationshipCollection;
  }

  protected convertValueFromClient<T extends IPartyRelationship | NewPartyRelationship | PartialUpdatePartyRelationship>(
    partyRelationship: T,
  ): RestOf<T> {
    return {
      ...partyRelationship,
      fromDate: partyRelationship.fromDate?.toJSON() ?? null,
      thruDate: partyRelationship.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPartyRelationship): IPartyRelationship {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPartyRelationship[]): IPartyRelationship[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
