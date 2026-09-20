import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFacilityContactMech, NewFacilityContactMech } from '../facility-contact-mech.model';

export type PartialUpdateFacilityContactMech = Partial<IFacilityContactMech> & Pick<IFacilityContactMech, 'id'>;

type RestOf<T extends IFacilityContactMech | NewFacilityContactMech> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestFacilityContactMech = RestOf<IFacilityContactMech>;

export type NewRestFacilityContactMech = RestOf<NewFacilityContactMech>;

export type PartialUpdateRestFacilityContactMech = RestOf<PartialUpdateFacilityContactMech>;

@Service()
export class FacilityContactMechesService {
  readonly facilityContactMechesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly facilityContactMechesResource = httpResource<RestFacilityContactMech[]>(() => {
    const params = this.facilityContactMechesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of facilityContactMech that have been fetched. It is updated when the facilityContactMechesResource emits a new value.
   * In case of error while fetching the facilityContactMeches, the signal is set to an empty array.
   */
  readonly facilityContactMeches = computed(() =>
    (this.facilityContactMechesResource.hasValue() ? this.facilityContactMechesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/facility-contact-meches`;

  protected convertValueFromServer(restFacilityContactMech: RestFacilityContactMech): IFacilityContactMech {
    return {
      ...restFacilityContactMech,
      fromDate: restFacilityContactMech.fromDate ? dayjs(restFacilityContactMech.fromDate) : undefined,
      thruDate: restFacilityContactMech.thruDate ? dayjs(restFacilityContactMech.thruDate) : undefined,
    };
  }
}

@Service()
export class FacilityContactMechService extends FacilityContactMechesService {
  protected readonly http = inject(HttpClient);

  create(facilityContactMech: NewFacilityContactMech): Observable<IFacilityContactMech> {
    const copy = this.convertValueFromClient(facilityContactMech);
    return this.http.post<RestFacilityContactMech>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(facilityContactMech: IFacilityContactMech): Observable<IFacilityContactMech> {
    const copy = this.convertValueFromClient(facilityContactMech);
    return this.http
      .put<RestFacilityContactMech>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFacilityContactMechIdentifier(facilityContactMech))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(facilityContactMech: PartialUpdateFacilityContactMech): Observable<IFacilityContactMech> {
    const copy = this.convertValueFromClient(facilityContactMech);
    return this.http
      .patch<RestFacilityContactMech>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFacilityContactMechIdentifier(facilityContactMech))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFacilityContactMech> {
    return this.http
      .get<RestFacilityContactMech>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFacilityContactMech[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFacilityContactMech[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFacilityContactMechIdentifier(facilityContactMech: Pick<IFacilityContactMech, 'id'>): number {
    return facilityContactMech.id;
  }

  compareFacilityContactMech(o1: Pick<IFacilityContactMech, 'id'> | null, o2: Pick<IFacilityContactMech, 'id'> | null): boolean {
    return o1 && o2 ? this.getFacilityContactMechIdentifier(o1) === this.getFacilityContactMechIdentifier(o2) : o1 === o2;
  }

  addFacilityContactMechToCollectionIfMissing<Type extends Pick<IFacilityContactMech, 'id'>>(
    facilityContactMechCollection: Type[],
    ...facilityContactMechesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facilityContactMeches: Type[] = facilityContactMechesToCheck.filter(
      facilityContactMechItem => facilityContactMechItem !== null && facilityContactMechItem !== undefined,
    );
    if (facilityContactMeches.length > 0) {
      const facilityContactMechCollectionIdentifiers = facilityContactMechCollection.map(facilityContactMechItem =>
        this.getFacilityContactMechIdentifier(facilityContactMechItem),
      );
      const facilityContactMechesToAdd = facilityContactMeches.filter(facilityContactMechItem => {
        const facilityContactMechIdentifier = this.getFacilityContactMechIdentifier(facilityContactMechItem);
        if (facilityContactMechCollectionIdentifiers.includes(facilityContactMechIdentifier)) {
          return false;
        }
        facilityContactMechCollectionIdentifiers.push(facilityContactMechIdentifier);
        return true;
      });
      return [...facilityContactMechesToAdd, ...facilityContactMechCollection];
    }
    return facilityContactMechCollection;
  }

  protected convertValueFromClient<T extends IFacilityContactMech | NewFacilityContactMech | PartialUpdateFacilityContactMech>(
    facilityContactMech: T,
  ): RestOf<T> {
    return {
      ...facilityContactMech,
      fromDate: facilityContactMech.fromDate?.toJSON() ?? null,
      thruDate: facilityContactMech.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFacilityContactMech): IFacilityContactMech {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFacilityContactMech[]): IFacilityContactMech[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
