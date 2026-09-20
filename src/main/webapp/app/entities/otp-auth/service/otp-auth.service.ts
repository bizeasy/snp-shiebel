import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOtpAuth, NewOtpAuth } from '../otp-auth.model';

export type PartialUpdateOtpAuth = Partial<IOtpAuth> & Pick<IOtpAuth, 'id'>;

type RestOf<T extends IOtpAuth | NewOtpAuth> = Omit<T, 'otpSentTime'> & {
  otpSentTime?: string | null;
};

export type RestOtpAuth = RestOf<IOtpAuth>;

export type NewRestOtpAuth = RestOf<NewOtpAuth>;

export type PartialUpdateRestOtpAuth = RestOf<PartialUpdateOtpAuth>;

@Service()
export class OtpAuthsService {
  readonly otpAuthsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly otpAuthsResource = httpResource<RestOtpAuth[]>(() => {
    const params = this.otpAuthsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of otpAuth that have been fetched. It is updated when the otpAuthsResource emits a new value.
   * In case of error while fetching the otpAuths, the signal is set to an empty array.
   */
  readonly otpAuths = computed(() =>
    (this.otpAuthsResource.hasValue() ? this.otpAuthsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/otp-auths`;

  protected convertValueFromServer(restOtpAuth: RestOtpAuth): IOtpAuth {
    return {
      ...restOtpAuth,
      otpSentTime: restOtpAuth.otpSentTime ? dayjs(restOtpAuth.otpSentTime) : undefined,
    };
  }
}

@Service()
export class OtpAuthService extends OtpAuthsService {
  protected readonly http = inject(HttpClient);

  create(otpAuth: NewOtpAuth): Observable<IOtpAuth> {
    const copy = this.convertValueFromClient(otpAuth);
    return this.http.post<RestOtpAuth>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(otpAuth: IOtpAuth): Observable<IOtpAuth> {
    const copy = this.convertValueFromClient(otpAuth);
    return this.http
      .put<RestOtpAuth>(`${this.resourceUrl}/${encodeURIComponent(this.getOtpAuthIdentifier(otpAuth))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(otpAuth: PartialUpdateOtpAuth): Observable<IOtpAuth> {
    const copy = this.convertValueFromClient(otpAuth);
    return this.http
      .patch<RestOtpAuth>(`${this.resourceUrl}/${encodeURIComponent(this.getOtpAuthIdentifier(otpAuth))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IOtpAuth> {
    return this.http
      .get<RestOtpAuth>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IOtpAuth[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOtpAuth[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOtpAuthIdentifier(otpAuth: Pick<IOtpAuth, 'id'>): number {
    return otpAuth.id;
  }

  compareOtpAuth(o1: Pick<IOtpAuth, 'id'> | null, o2: Pick<IOtpAuth, 'id'> | null): boolean {
    return o1 && o2 ? this.getOtpAuthIdentifier(o1) === this.getOtpAuthIdentifier(o2) : o1 === o2;
  }

  addOtpAuthToCollectionIfMissing<Type extends Pick<IOtpAuth, 'id'>>(
    otpAuthCollection: Type[],
    ...otpAuthsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const otpAuths: Type[] = otpAuthsToCheck.filter(otpAuthItem => otpAuthItem !== null && otpAuthItem !== undefined);
    if (otpAuths.length > 0) {
      const otpAuthCollectionIdentifiers = otpAuthCollection.map(otpAuthItem => this.getOtpAuthIdentifier(otpAuthItem));
      const otpAuthsToAdd = otpAuths.filter(otpAuthItem => {
        const otpAuthIdentifier = this.getOtpAuthIdentifier(otpAuthItem);
        if (otpAuthCollectionIdentifiers.includes(otpAuthIdentifier)) {
          return false;
        }
        otpAuthCollectionIdentifiers.push(otpAuthIdentifier);
        return true;
      });
      return [...otpAuthsToAdd, ...otpAuthCollection];
    }
    return otpAuthCollection;
  }

  protected convertValueFromClient<T extends IOtpAuth | NewOtpAuth | PartialUpdateOtpAuth>(otpAuth: T): RestOf<T> {
    return {
      ...otpAuth,
      otpSentTime: otpAuth.otpSentTime?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestOtpAuth): IOtpAuth {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestOtpAuth[]): IOtpAuth[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
