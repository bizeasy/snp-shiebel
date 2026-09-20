import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IGeoAssoc, NewGeoAssoc } from '../geo-assoc.model';

export type PartialUpdateGeoAssoc = Partial<IGeoAssoc> & Pick<IGeoAssoc, 'id'>;

@Service()
export class GeoAssocsService {
  readonly geoAssocsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly geoAssocsResource = httpResource<IGeoAssoc[]>(() => {
    const params = this.geoAssocsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of geoAssoc that have been fetched. It is updated when the geoAssocsResource emits a new value.
   * In case of error while fetching the geoAssocs, the signal is set to an empty array.
   */
  readonly geoAssocs = computed(() => (this.geoAssocsResource.hasValue() ? this.geoAssocsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/geo-assocs`;
}

@Service()
export class GeoAssocService extends GeoAssocsService {
  protected readonly http = inject(HttpClient);

  create(geoAssoc: NewGeoAssoc): Observable<IGeoAssoc> {
    return this.http.post<IGeoAssoc>(this.resourceUrl, geoAssoc);
  }

  update(geoAssoc: IGeoAssoc): Observable<IGeoAssoc> {
    return this.http.put<IGeoAssoc>(`${this.resourceUrl}/${encodeURIComponent(this.getGeoAssocIdentifier(geoAssoc))}`, geoAssoc);
  }

  partialUpdate(geoAssoc: PartialUpdateGeoAssoc): Observable<IGeoAssoc> {
    return this.http.patch<IGeoAssoc>(`${this.resourceUrl}/${encodeURIComponent(this.getGeoAssocIdentifier(geoAssoc))}`, geoAssoc);
  }

  find(id: number): Observable<IGeoAssoc> {
    return this.http.get<IGeoAssoc>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IGeoAssoc[]>> {
    const options = createRequestOption(req);
    return this.http.get<IGeoAssoc[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getGeoAssocIdentifier(geoAssoc: Pick<IGeoAssoc, 'id'>): number {
    return geoAssoc.id;
  }

  compareGeoAssoc(o1: Pick<IGeoAssoc, 'id'> | null, o2: Pick<IGeoAssoc, 'id'> | null): boolean {
    return o1 && o2 ? this.getGeoAssocIdentifier(o1) === this.getGeoAssocIdentifier(o2) : o1 === o2;
  }

  addGeoAssocToCollectionIfMissing<Type extends Pick<IGeoAssoc, 'id'>>(
    geoAssocCollection: Type[],
    ...geoAssocsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const geoAssocs: Type[] = geoAssocsToCheck.filter(geoAssocItem => geoAssocItem !== null && geoAssocItem !== undefined);
    if (geoAssocs.length > 0) {
      const geoAssocCollectionIdentifiers = geoAssocCollection.map(geoAssocItem => this.getGeoAssocIdentifier(geoAssocItem));
      const geoAssocsToAdd = geoAssocs.filter(geoAssocItem => {
        const geoAssocIdentifier = this.getGeoAssocIdentifier(geoAssocItem);
        if (geoAssocCollectionIdentifiers.includes(geoAssocIdentifier)) {
          return false;
        }
        geoAssocCollectionIdentifiers.push(geoAssocIdentifier);
        return true;
      });
      return [...geoAssocsToAdd, ...geoAssocCollection];
    }
    return geoAssocCollection;
  }
}
