import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IStatus, NewStatus } from '../status.model';

export type PartialUpdateStatus = Partial<IStatus> & Pick<IStatus, 'id'>;

@Service()
export class StatusesService {
  readonly statusesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly statusesResource = httpResource<IStatus[]>(() => {
    const params = this.statusesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of status that have been fetched. It is updated when the statusesResource emits a new value.
   * In case of error while fetching the statuses, the signal is set to an empty array.
   */
  readonly statuses = computed(() => (this.statusesResource.hasValue() ? this.statusesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/statuses`;
}

@Service()
export class StatusService extends StatusesService {
  protected readonly http = inject(HttpClient);

  create(status: NewStatus): Observable<IStatus> {
    return this.http.post<IStatus>(this.resourceUrl, status);
  }

  update(status: IStatus): Observable<IStatus> {
    return this.http.put<IStatus>(`${this.resourceUrl}/${encodeURIComponent(this.getStatusIdentifier(status))}`, status);
  }

  partialUpdate(status: PartialUpdateStatus): Observable<IStatus> {
    return this.http.patch<IStatus>(`${this.resourceUrl}/${encodeURIComponent(this.getStatusIdentifier(status))}`, status);
  }

  find(id: number): Observable<IStatus> {
    return this.http.get<IStatus>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IStatus[]>> {
    const options = createRequestOption(req);
    return this.http.get<IStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getStatusIdentifier(status: Pick<IStatus, 'id'>): number {
    return status.id;
  }

  compareStatus(o1: Pick<IStatus, 'id'> | null, o2: Pick<IStatus, 'id'> | null): boolean {
    return o1 && o2 ? this.getStatusIdentifier(o1) === this.getStatusIdentifier(o2) : o1 === o2;
  }

  addStatusToCollectionIfMissing<Type extends Pick<IStatus, 'id'>>(
    statusCollection: Type[],
    ...statusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const statuses: Type[] = statusesToCheck.filter(statusItem => statusItem !== null && statusItem !== undefined);
    if (statuses.length > 0) {
      const statusCollectionIdentifiers = statusCollection.map(statusItem => this.getStatusIdentifier(statusItem));
      const statusesToAdd = statuses.filter(statusItem => {
        const statusIdentifier = this.getStatusIdentifier(statusItem);
        if (statusCollectionIdentifiers.includes(statusIdentifier)) {
          return false;
        }
        statusCollectionIdentifiers.push(statusIdentifier);
        return true;
      });
      return [...statusesToAdd, ...statusCollection];
    }
    return statusCollection;
  }
}
