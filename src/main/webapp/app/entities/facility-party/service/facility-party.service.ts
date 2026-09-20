import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFacilityParty, NewFacilityParty } from '../facility-party.model';

export type PartialUpdateFacilityParty = Partial<IFacilityParty> & Pick<IFacilityParty, 'id'>;

type RestOf<T extends IFacilityParty | NewFacilityParty> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestFacilityParty = RestOf<IFacilityParty>;

export type NewRestFacilityParty = RestOf<NewFacilityParty>;

export type PartialUpdateRestFacilityParty = RestOf<PartialUpdateFacilityParty>;

@Service()
export class FacilityPartiesService {
  readonly facilityPartiesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly facilityPartiesResource = httpResource<RestFacilityParty[]>(() => {
    const params = this.facilityPartiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of facilityParty that have been fetched. It is updated when the facilityPartiesResource emits a new value.
   * In case of error while fetching the facilityParties, the signal is set to an empty array.
   */
  readonly facilityParties = computed(() =>
    (this.facilityPartiesResource.hasValue() ? this.facilityPartiesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/facility-parties`;

  protected convertValueFromServer(restFacilityParty: RestFacilityParty): IFacilityParty {
    return {
      ...restFacilityParty,
      fromDate: restFacilityParty.fromDate ? dayjs(restFacilityParty.fromDate) : undefined,
      thruDate: restFacilityParty.thruDate ? dayjs(restFacilityParty.thruDate) : undefined,
    };
  }
}

@Service()
export class FacilityPartyService extends FacilityPartiesService {
  protected readonly http = inject(HttpClient);

  create(facilityParty: NewFacilityParty): Observable<IFacilityParty> {
    const copy = this.convertValueFromClient(facilityParty);
    return this.http.post<RestFacilityParty>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(facilityParty: IFacilityParty): Observable<IFacilityParty> {
    const copy = this.convertValueFromClient(facilityParty);
    return this.http
      .put<RestFacilityParty>(`${this.resourceUrl}/${encodeURIComponent(this.getFacilityPartyIdentifier(facilityParty))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(facilityParty: PartialUpdateFacilityParty): Observable<IFacilityParty> {
    const copy = this.convertValueFromClient(facilityParty);
    return this.http
      .patch<RestFacilityParty>(`${this.resourceUrl}/${encodeURIComponent(this.getFacilityPartyIdentifier(facilityParty))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFacilityParty> {
    return this.http
      .get<RestFacilityParty>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFacilityParty[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFacilityParty[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFacilityPartyIdentifier(facilityParty: Pick<IFacilityParty, 'id'>): number {
    return facilityParty.id;
  }

  compareFacilityParty(o1: Pick<IFacilityParty, 'id'> | null, o2: Pick<IFacilityParty, 'id'> | null): boolean {
    return o1 && o2 ? this.getFacilityPartyIdentifier(o1) === this.getFacilityPartyIdentifier(o2) : o1 === o2;
  }

  addFacilityPartyToCollectionIfMissing<Type extends Pick<IFacilityParty, 'id'>>(
    facilityPartyCollection: Type[],
    ...facilityPartiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facilityParties: Type[] = facilityPartiesToCheck.filter(
      facilityPartyItem => facilityPartyItem !== null && facilityPartyItem !== undefined,
    );
    if (facilityParties.length > 0) {
      const facilityPartyCollectionIdentifiers = facilityPartyCollection.map(facilityPartyItem =>
        this.getFacilityPartyIdentifier(facilityPartyItem),
      );
      const facilityPartiesToAdd = facilityParties.filter(facilityPartyItem => {
        const facilityPartyIdentifier = this.getFacilityPartyIdentifier(facilityPartyItem);
        if (facilityPartyCollectionIdentifiers.includes(facilityPartyIdentifier)) {
          return false;
        }
        facilityPartyCollectionIdentifiers.push(facilityPartyIdentifier);
        return true;
      });
      return [...facilityPartiesToAdd, ...facilityPartyCollection];
    }
    return facilityPartyCollection;
  }

  protected convertValueFromClient<T extends IFacilityParty | NewFacilityParty | PartialUpdateFacilityParty>(facilityParty: T): RestOf<T> {
    return {
      ...facilityParty,
      fromDate: facilityParty.fromDate?.toJSON() ?? null,
      thruDate: facilityParty.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFacilityParty): IFacilityParty {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFacilityParty[]): IFacilityParty[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
