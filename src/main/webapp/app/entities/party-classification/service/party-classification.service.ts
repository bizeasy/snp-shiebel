import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartyClassification, NewPartyClassification } from '../party-classification.model';

export type PartialUpdatePartyClassification = Partial<IPartyClassification> & Pick<IPartyClassification, 'id'>;

type RestOf<T extends IPartyClassification | NewPartyClassification> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestPartyClassification = RestOf<IPartyClassification>;

export type NewRestPartyClassification = RestOf<NewPartyClassification>;

export type PartialUpdateRestPartyClassification = RestOf<PartialUpdatePartyClassification>;

@Service()
export class PartyClassificationsService {
  readonly partyClassificationsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly partyClassificationsResource = httpResource<RestPartyClassification[]>(() => {
    const params = this.partyClassificationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partyClassification that have been fetched. It is updated when the partyClassificationsResource emits a new value.
   * In case of error while fetching the partyClassifications, the signal is set to an empty array.
   */
  readonly partyClassifications = computed(() =>
    (this.partyClassificationsResource.hasValue() ? this.partyClassificationsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/party-classifications`;

  protected convertValueFromServer(restPartyClassification: RestPartyClassification): IPartyClassification {
    return {
      ...restPartyClassification,
      fromDate: restPartyClassification.fromDate ? dayjs(restPartyClassification.fromDate) : undefined,
      thruDate: restPartyClassification.thruDate ? dayjs(restPartyClassification.thruDate) : undefined,
    };
  }
}

@Service()
export class PartyClassificationService extends PartyClassificationsService {
  protected readonly http = inject(HttpClient);

  create(partyClassification: NewPartyClassification): Observable<IPartyClassification> {
    const copy = this.convertValueFromClient(partyClassification);
    return this.http.post<RestPartyClassification>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(partyClassification: IPartyClassification): Observable<IPartyClassification> {
    const copy = this.convertValueFromClient(partyClassification);
    return this.http
      .put<RestPartyClassification>(
        `${this.resourceUrl}/${encodeURIComponent(this.getPartyClassificationIdentifier(partyClassification))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(partyClassification: PartialUpdatePartyClassification): Observable<IPartyClassification> {
    const copy = this.convertValueFromClient(partyClassification);
    return this.http
      .patch<RestPartyClassification>(
        `${this.resourceUrl}/${encodeURIComponent(this.getPartyClassificationIdentifier(partyClassification))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPartyClassification> {
    return this.http
      .get<RestPartyClassification>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPartyClassification[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPartyClassification[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartyClassificationIdentifier(partyClassification: Pick<IPartyClassification, 'id'>): number {
    return partyClassification.id;
  }

  comparePartyClassification(o1: Pick<IPartyClassification, 'id'> | null, o2: Pick<IPartyClassification, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartyClassificationIdentifier(o1) === this.getPartyClassificationIdentifier(o2) : o1 === o2;
  }

  addPartyClassificationToCollectionIfMissing<Type extends Pick<IPartyClassification, 'id'>>(
    partyClassificationCollection: Type[],
    ...partyClassificationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partyClassifications: Type[] = partyClassificationsToCheck.filter(
      partyClassificationItem => partyClassificationItem !== null && partyClassificationItem !== undefined,
    );
    if (partyClassifications.length > 0) {
      const partyClassificationCollectionIdentifiers = partyClassificationCollection.map(partyClassificationItem =>
        this.getPartyClassificationIdentifier(partyClassificationItem),
      );
      const partyClassificationsToAdd = partyClassifications.filter(partyClassificationItem => {
        const partyClassificationIdentifier = this.getPartyClassificationIdentifier(partyClassificationItem);
        if (partyClassificationCollectionIdentifiers.includes(partyClassificationIdentifier)) {
          return false;
        }
        partyClassificationCollectionIdentifiers.push(partyClassificationIdentifier);
        return true;
      });
      return [...partyClassificationsToAdd, ...partyClassificationCollection];
    }
    return partyClassificationCollection;
  }

  protected convertValueFromClient<T extends IPartyClassification | NewPartyClassification | PartialUpdatePartyClassification>(
    partyClassification: T,
  ): RestOf<T> {
    return {
      ...partyClassification,
      fromDate: partyClassification.fromDate?.toJSON() ?? null,
      thruDate: partyClassification.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPartyClassification): IPartyClassification {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPartyClassification[]): IPartyClassification[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
