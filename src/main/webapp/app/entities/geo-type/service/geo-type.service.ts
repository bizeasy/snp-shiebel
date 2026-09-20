import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IGeoType, NewGeoType } from '../geo-type.model';

export type PartialUpdateGeoType = Partial<IGeoType> & Pick<IGeoType, 'id'>;

@Service()
export class GeoTypesService {
  readonly geoTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly geoTypesResource = httpResource<IGeoType[]>(() => {
    const params = this.geoTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of geoType that have been fetched. It is updated when the geoTypesResource emits a new value.
   * In case of error while fetching the geoTypes, the signal is set to an empty array.
   */
  readonly geoTypes = computed(() => (this.geoTypesResource.hasValue() ? this.geoTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/geo-types`;
}

@Service()
export class GeoTypeService extends GeoTypesService {
  protected readonly http = inject(HttpClient);

  create(geoType: NewGeoType): Observable<IGeoType> {
    return this.http.post<IGeoType>(this.resourceUrl, geoType);
  }

  update(geoType: IGeoType): Observable<IGeoType> {
    return this.http.put<IGeoType>(`${this.resourceUrl}/${encodeURIComponent(this.getGeoTypeIdentifier(geoType))}`, geoType);
  }

  partialUpdate(geoType: PartialUpdateGeoType): Observable<IGeoType> {
    return this.http.patch<IGeoType>(`${this.resourceUrl}/${encodeURIComponent(this.getGeoTypeIdentifier(geoType))}`, geoType);
  }

  find(id: number): Observable<IGeoType> {
    return this.http.get<IGeoType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IGeoType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IGeoType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getGeoTypeIdentifier(geoType: Pick<IGeoType, 'id'>): number {
    return geoType.id;
  }

  compareGeoType(o1: Pick<IGeoType, 'id'> | null, o2: Pick<IGeoType, 'id'> | null): boolean {
    return o1 && o2 ? this.getGeoTypeIdentifier(o1) === this.getGeoTypeIdentifier(o2) : o1 === o2;
  }

  addGeoTypeToCollectionIfMissing<Type extends Pick<IGeoType, 'id'>>(
    geoTypeCollection: Type[],
    ...geoTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const geoTypes: Type[] = geoTypesToCheck.filter(geoTypeItem => geoTypeItem !== null && geoTypeItem !== undefined);
    if (geoTypes.length > 0) {
      const geoTypeCollectionIdentifiers = geoTypeCollection.map(geoTypeItem => this.getGeoTypeIdentifier(geoTypeItem));
      const geoTypesToAdd = geoTypes.filter(geoTypeItem => {
        const geoTypeIdentifier = this.getGeoTypeIdentifier(geoTypeItem);
        if (geoTypeCollectionIdentifiers.includes(geoTypeIdentifier)) {
          return false;
        }
        geoTypeCollectionIdentifiers.push(geoTypeIdentifier);
        return true;
      });
      return [...geoTypesToAdd, ...geoTypeCollection];
    }
    return geoTypeCollection;
  }
}
