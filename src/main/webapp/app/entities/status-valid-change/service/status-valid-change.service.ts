import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IStatusValidChange, NewStatusValidChange } from '../status-valid-change.model';

export type PartialUpdateStatusValidChange = Partial<IStatusValidChange> & Pick<IStatusValidChange, 'id'>;

@Service()
export class StatusValidChangesService {
  readonly statusValidChangesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly statusValidChangesResource = httpResource<IStatusValidChange[]>(() => {
    const params = this.statusValidChangesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of statusValidChange that have been fetched. It is updated when the statusValidChangesResource emits a new value.
   * In case of error while fetching the statusValidChanges, the signal is set to an empty array.
   */
  readonly statusValidChanges = computed(() => (this.statusValidChangesResource.hasValue() ? this.statusValidChangesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/status-valid-changes`;
}

@Service()
export class StatusValidChangeService extends StatusValidChangesService {
  protected readonly http = inject(HttpClient);

  create(statusValidChange: NewStatusValidChange): Observable<IStatusValidChange> {
    return this.http.post<IStatusValidChange>(this.resourceUrl, statusValidChange);
  }

  update(statusValidChange: IStatusValidChange): Observable<IStatusValidChange> {
    return this.http.put<IStatusValidChange>(
      `${this.resourceUrl}/${encodeURIComponent(this.getStatusValidChangeIdentifier(statusValidChange))}`,
      statusValidChange,
    );
  }

  partialUpdate(statusValidChange: PartialUpdateStatusValidChange): Observable<IStatusValidChange> {
    return this.http.patch<IStatusValidChange>(
      `${this.resourceUrl}/${encodeURIComponent(this.getStatusValidChangeIdentifier(statusValidChange))}`,
      statusValidChange,
    );
  }

  find(id: number): Observable<IStatusValidChange> {
    return this.http.get<IStatusValidChange>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IStatusValidChange[]>> {
    const options = createRequestOption(req);
    return this.http.get<IStatusValidChange[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getStatusValidChangeIdentifier(statusValidChange: Pick<IStatusValidChange, 'id'>): number {
    return statusValidChange.id;
  }

  compareStatusValidChange(o1: Pick<IStatusValidChange, 'id'> | null, o2: Pick<IStatusValidChange, 'id'> | null): boolean {
    return o1 && o2 ? this.getStatusValidChangeIdentifier(o1) === this.getStatusValidChangeIdentifier(o2) : o1 === o2;
  }

  addStatusValidChangeToCollectionIfMissing<Type extends Pick<IStatusValidChange, 'id'>>(
    statusValidChangeCollection: Type[],
    ...statusValidChangesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const statusValidChanges: Type[] = statusValidChangesToCheck.filter(
      statusValidChangeItem => statusValidChangeItem !== null && statusValidChangeItem !== undefined,
    );
    if (statusValidChanges.length > 0) {
      const statusValidChangeCollectionIdentifiers = statusValidChangeCollection.map(statusValidChangeItem =>
        this.getStatusValidChangeIdentifier(statusValidChangeItem),
      );
      const statusValidChangesToAdd = statusValidChanges.filter(statusValidChangeItem => {
        const statusValidChangeIdentifier = this.getStatusValidChangeIdentifier(statusValidChangeItem);
        if (statusValidChangeCollectionIdentifiers.includes(statusValidChangeIdentifier)) {
          return false;
        }
        statusValidChangeCollectionIdentifiers.push(statusValidChangeIdentifier);
        return true;
      });
      return [...statusValidChangesToAdd, ...statusValidChangeCollection];
    }
    return statusValidChangeCollection;
  }
}
