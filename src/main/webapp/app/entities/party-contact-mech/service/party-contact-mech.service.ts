import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartyContactMech, NewPartyContactMech } from '../party-contact-mech.model';

export type PartialUpdatePartyContactMech = Partial<IPartyContactMech> & Pick<IPartyContactMech, 'id'>;

type RestOf<T extends IPartyContactMech | NewPartyContactMech> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestPartyContactMech = RestOf<IPartyContactMech>;

export type NewRestPartyContactMech = RestOf<NewPartyContactMech>;

export type PartialUpdateRestPartyContactMech = RestOf<PartialUpdatePartyContactMech>;

@Service()
export class PartyContactMechesService {
  readonly partyContactMechesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly partyContactMechesResource = httpResource<RestPartyContactMech[]>(() => {
    const params = this.partyContactMechesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partyContactMech that have been fetched. It is updated when the partyContactMechesResource emits a new value.
   * In case of error while fetching the partyContactMeches, the signal is set to an empty array.
   */
  readonly partyContactMeches = computed(() =>
    (this.partyContactMechesResource.hasValue() ? this.partyContactMechesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/party-contact-meches`;

  protected convertValueFromServer(restPartyContactMech: RestPartyContactMech): IPartyContactMech {
    return {
      ...restPartyContactMech,
      fromDate: restPartyContactMech.fromDate ? dayjs(restPartyContactMech.fromDate) : undefined,
      thruDate: restPartyContactMech.thruDate ? dayjs(restPartyContactMech.thruDate) : undefined,
    };
  }
}

@Service()
export class PartyContactMechService extends PartyContactMechesService {
  protected readonly http = inject(HttpClient);

  create(partyContactMech: NewPartyContactMech): Observable<IPartyContactMech> {
    const copy = this.convertValueFromClient(partyContactMech);
    return this.http.post<RestPartyContactMech>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(partyContactMech: IPartyContactMech): Observable<IPartyContactMech> {
    const copy = this.convertValueFromClient(partyContactMech);
    return this.http
      .put<RestPartyContactMech>(`${this.resourceUrl}/${encodeURIComponent(this.getPartyContactMechIdentifier(partyContactMech))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(partyContactMech: PartialUpdatePartyContactMech): Observable<IPartyContactMech> {
    const copy = this.convertValueFromClient(partyContactMech);
    return this.http
      .patch<RestPartyContactMech>(`${this.resourceUrl}/${encodeURIComponent(this.getPartyContactMechIdentifier(partyContactMech))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPartyContactMech> {
    return this.http
      .get<RestPartyContactMech>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPartyContactMech[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPartyContactMech[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartyContactMechIdentifier(partyContactMech: Pick<IPartyContactMech, 'id'>): number {
    return partyContactMech.id;
  }

  comparePartyContactMech(o1: Pick<IPartyContactMech, 'id'> | null, o2: Pick<IPartyContactMech, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartyContactMechIdentifier(o1) === this.getPartyContactMechIdentifier(o2) : o1 === o2;
  }

  addPartyContactMechToCollectionIfMissing<Type extends Pick<IPartyContactMech, 'id'>>(
    partyContactMechCollection: Type[],
    ...partyContactMechesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partyContactMeches: Type[] = partyContactMechesToCheck.filter(
      partyContactMechItem => partyContactMechItem !== null && partyContactMechItem !== undefined,
    );
    if (partyContactMeches.length > 0) {
      const partyContactMechCollectionIdentifiers = partyContactMechCollection.map(partyContactMechItem =>
        this.getPartyContactMechIdentifier(partyContactMechItem),
      );
      const partyContactMechesToAdd = partyContactMeches.filter(partyContactMechItem => {
        const partyContactMechIdentifier = this.getPartyContactMechIdentifier(partyContactMechItem);
        if (partyContactMechCollectionIdentifiers.includes(partyContactMechIdentifier)) {
          return false;
        }
        partyContactMechCollectionIdentifiers.push(partyContactMechIdentifier);
        return true;
      });
      return [...partyContactMechesToAdd, ...partyContactMechCollection];
    }
    return partyContactMechCollection;
  }

  protected convertValueFromClient<T extends IPartyContactMech | NewPartyContactMech | PartialUpdatePartyContactMech>(
    partyContactMech: T,
  ): RestOf<T> {
    return {
      ...partyContactMech,
      fromDate: partyContactMech.fromDate?.toJSON() ?? null,
      thruDate: partyContactMech.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPartyContactMech): IPartyContactMech {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPartyContactMech[]): IPartyContactMech[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
