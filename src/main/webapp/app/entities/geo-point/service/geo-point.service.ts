import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IGeoPoint, NewGeoPoint } from '../geo-point.model';

export type PartialUpdateGeoPoint = Partial<IGeoPoint> & Pick<IGeoPoint, 'id'>;

@Service()
export class GeoPointsService {
  readonly geoPointsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly geoPointsResource = httpResource<IGeoPoint[]>(() => {
    const params = this.geoPointsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of geoPoint that have been fetched. It is updated when the geoPointsResource emits a new value.
   * In case of error while fetching the geoPoints, the signal is set to an empty array.
   */
  readonly geoPoints = computed(() => (this.geoPointsResource.hasValue() ? this.geoPointsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/geo-points`;
}

@Service()
export class GeoPointService extends GeoPointsService {
  protected readonly http = inject(HttpClient);

  create(geoPoint: NewGeoPoint): Observable<IGeoPoint> {
    return this.http.post<IGeoPoint>(this.resourceUrl, geoPoint);
  }

  update(geoPoint: IGeoPoint): Observable<IGeoPoint> {
    return this.http.put<IGeoPoint>(`${this.resourceUrl}/${encodeURIComponent(this.getGeoPointIdentifier(geoPoint))}`, geoPoint);
  }

  partialUpdate(geoPoint: PartialUpdateGeoPoint): Observable<IGeoPoint> {
    return this.http.patch<IGeoPoint>(`${this.resourceUrl}/${encodeURIComponent(this.getGeoPointIdentifier(geoPoint))}`, geoPoint);
  }

  find(id: number): Observable<IGeoPoint> {
    return this.http.get<IGeoPoint>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IGeoPoint[]>> {
    const options = createRequestOption(req);
    return this.http.get<IGeoPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getGeoPointIdentifier(geoPoint: Pick<IGeoPoint, 'id'>): number {
    return geoPoint.id;
  }

  compareGeoPoint(o1: Pick<IGeoPoint, 'id'> | null, o2: Pick<IGeoPoint, 'id'> | null): boolean {
    return o1 && o2 ? this.getGeoPointIdentifier(o1) === this.getGeoPointIdentifier(o2) : o1 === o2;
  }

  addGeoPointToCollectionIfMissing<Type extends Pick<IGeoPoint, 'id'>>(
    geoPointCollection: Type[],
    ...geoPointsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const geoPoints: Type[] = geoPointsToCheck.filter(geoPointItem => geoPointItem !== null && geoPointItem !== undefined);
    if (geoPoints.length > 0) {
      const geoPointCollectionIdentifiers = geoPointCollection.map(geoPointItem => this.getGeoPointIdentifier(geoPointItem));
      const geoPointsToAdd = geoPoints.filter(geoPointItem => {
        const geoPointIdentifier = this.getGeoPointIdentifier(geoPointItem);
        if (geoPointCollectionIdentifiers.includes(geoPointIdentifier)) {
          return false;
        }
        geoPointCollectionIdentifiers.push(geoPointIdentifier);
        return true;
      });
      return [...geoPointsToAdd, ...geoPointCollection];
    }
    return geoPointCollection;
  }
}
