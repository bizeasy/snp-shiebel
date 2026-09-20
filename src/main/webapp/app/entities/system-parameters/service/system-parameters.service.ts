import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ISystemParameters, NewSystemParameters } from '../system-parameters.model';

export type PartialUpdateSystemParameters = Partial<ISystemParameters> & Pick<ISystemParameters, 'id'>;

@Service()
export class SystemParametersesService {
  readonly systemParametersesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly systemParametersesResource = httpResource<ISystemParameters[]>(() => {
    const params = this.systemParametersesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of systemParameters that have been fetched. It is updated when the systemParametersesResource emits a new value.
   * In case of error while fetching the systemParameterses, the signal is set to an empty array.
   */
  readonly systemParameterses = computed(() => (this.systemParametersesResource.hasValue() ? this.systemParametersesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/system-parameters`;
}

@Service()
export class SystemParametersService extends SystemParametersesService {
  protected readonly http = inject(HttpClient);

  create(systemParameters: NewSystemParameters): Observable<ISystemParameters> {
    return this.http.post<ISystemParameters>(this.resourceUrl, systemParameters);
  }

  update(systemParameters: ISystemParameters): Observable<ISystemParameters> {
    return this.http.put<ISystemParameters>(
      `${this.resourceUrl}/${encodeURIComponent(this.getSystemParametersIdentifier(systemParameters))}`,
      systemParameters,
    );
  }

  partialUpdate(systemParameters: PartialUpdateSystemParameters): Observable<ISystemParameters> {
    return this.http.patch<ISystemParameters>(
      `${this.resourceUrl}/${encodeURIComponent(this.getSystemParametersIdentifier(systemParameters))}`,
      systemParameters,
    );
  }

  find(id: number): Observable<ISystemParameters> {
    return this.http.get<ISystemParameters>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ISystemParameters[]>> {
    const options = createRequestOption(req);
    return this.http.get<ISystemParameters[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getSystemParametersIdentifier(systemParameters: Pick<ISystemParameters, 'id'>): number {
    return systemParameters.id;
  }

  compareSystemParameters(o1: Pick<ISystemParameters, 'id'> | null, o2: Pick<ISystemParameters, 'id'> | null): boolean {
    return o1 && o2 ? this.getSystemParametersIdentifier(o1) === this.getSystemParametersIdentifier(o2) : o1 === o2;
  }

  addSystemParametersToCollectionIfMissing<Type extends Pick<ISystemParameters, 'id'>>(
    systemParametersCollection: Type[],
    ...systemParametersesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const systemParameterses: Type[] = systemParametersesToCheck.filter(
      systemParametersItem => systemParametersItem !== null && systemParametersItem !== undefined,
    );
    if (systemParameterses.length > 0) {
      const systemParametersCollectionIdentifiers = systemParametersCollection.map(systemParametersItem =>
        this.getSystemParametersIdentifier(systemParametersItem),
      );
      const systemParametersesToAdd = systemParameterses.filter(systemParametersItem => {
        const systemParametersIdentifier = this.getSystemParametersIdentifier(systemParametersItem);
        if (systemParametersCollectionIdentifiers.includes(systemParametersIdentifier)) {
          return false;
        }
        systemParametersCollectionIdentifiers.push(systemParametersIdentifier);
        return true;
      });
      return [...systemParametersesToAdd, ...systemParametersCollection];
    }
    return systemParametersCollection;
  }
}
