import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IGeo, NewGeo } from '../geo.model';

export type PartialUpdateGeo = Partial<IGeo> & Pick<IGeo, 'id'>;

@Service()
export class GeosService {
  readonly geosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly geosResource = httpResource<IGeo[]>(() => {
    const params = this.geosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of geo that have been fetched. It is updated when the geosResource emits a new value.
   * In case of error while fetching the geos, the signal is set to an empty array.
   */
  readonly geos = computed(() => (this.geosResource.hasValue() ? this.geosResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/geos`;
}

@Service()
export class GeoService extends GeosService {
  protected readonly http = inject(HttpClient);

  create(geo: NewGeo): Observable<IGeo> {
    return this.http.post<IGeo>(this.resourceUrl, geo);
  }

  update(geo: IGeo): Observable<IGeo> {
    return this.http.put<IGeo>(`${this.resourceUrl}/${encodeURIComponent(this.getGeoIdentifier(geo))}`, geo);
  }

  partialUpdate(geo: PartialUpdateGeo): Observable<IGeo> {
    return this.http.patch<IGeo>(`${this.resourceUrl}/${encodeURIComponent(this.getGeoIdentifier(geo))}`, geo);
  }

  find(id: number): Observable<IGeo> {
    return this.http.get<IGeo>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IGeo[]>> {
    const options = createRequestOption(req);
    return this.http.get<IGeo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getGeoIdentifier(geo: Pick<IGeo, 'id'>): number {
    return geo.id;
  }

  compareGeo(o1: Pick<IGeo, 'id'> | null, o2: Pick<IGeo, 'id'> | null): boolean {
    return o1 && o2 ? this.getGeoIdentifier(o1) === this.getGeoIdentifier(o2) : o1 === o2;
  }

  addGeoToCollectionIfMissing<Type extends Pick<IGeo, 'id'>>(geoCollection: Type[], ...geosToCheck: (Type | null | undefined)[]): Type[] {
    const geos: Type[] = geosToCheck.filter(geoItem => geoItem !== null && geoItem !== undefined);
    if (geos.length > 0) {
      const geoCollectionIdentifiers = geoCollection.map(geoItem => this.getGeoIdentifier(geoItem));
      const geosToAdd = geos.filter(geoItem => {
        const geoIdentifier = this.getGeoIdentifier(geoItem);
        if (geoCollectionIdentifiers.includes(geoIdentifier)) {
          return false;
        }
        geoCollectionIdentifiers.push(geoIdentifier);
        return true;
      });
      return [...geosToAdd, ...geoCollection];
    }
    return geoCollection;
  }
}
