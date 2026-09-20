import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IGeoAssocType, NewGeoAssocType } from '../geo-assoc-type.model';

export type PartialUpdateGeoAssocType = Partial<IGeoAssocType> & Pick<IGeoAssocType, 'id'>;

@Service()
export class GeoAssocTypesService {
  readonly geoAssocTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly geoAssocTypesResource = httpResource<IGeoAssocType[]>(() => {
    const params = this.geoAssocTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of geoAssocType that have been fetched. It is updated when the geoAssocTypesResource emits a new value.
   * In case of error while fetching the geoAssocTypes, the signal is set to an empty array.
   */
  readonly geoAssocTypes = computed(() => (this.geoAssocTypesResource.hasValue() ? this.geoAssocTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/geo-assoc-types`;
}

@Service()
export class GeoAssocTypeService extends GeoAssocTypesService {
  protected readonly http = inject(HttpClient);

  create(geoAssocType: NewGeoAssocType): Observable<IGeoAssocType> {
    return this.http.post<IGeoAssocType>(this.resourceUrl, geoAssocType);
  }

  update(geoAssocType: IGeoAssocType): Observable<IGeoAssocType> {
    return this.http.put<IGeoAssocType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getGeoAssocTypeIdentifier(geoAssocType))}`,
      geoAssocType,
    );
  }

  partialUpdate(geoAssocType: PartialUpdateGeoAssocType): Observable<IGeoAssocType> {
    return this.http.patch<IGeoAssocType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getGeoAssocTypeIdentifier(geoAssocType))}`,
      geoAssocType,
    );
  }

  find(id: number): Observable<IGeoAssocType> {
    return this.http.get<IGeoAssocType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IGeoAssocType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IGeoAssocType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getGeoAssocTypeIdentifier(geoAssocType: Pick<IGeoAssocType, 'id'>): number {
    return geoAssocType.id;
  }

  compareGeoAssocType(o1: Pick<IGeoAssocType, 'id'> | null, o2: Pick<IGeoAssocType, 'id'> | null): boolean {
    return o1 && o2 ? this.getGeoAssocTypeIdentifier(o1) === this.getGeoAssocTypeIdentifier(o2) : o1 === o2;
  }

  addGeoAssocTypeToCollectionIfMissing<Type extends Pick<IGeoAssocType, 'id'>>(
    geoAssocTypeCollection: Type[],
    ...geoAssocTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const geoAssocTypes: Type[] = geoAssocTypesToCheck.filter(
      geoAssocTypeItem => geoAssocTypeItem !== null && geoAssocTypeItem !== undefined,
    );
    if (geoAssocTypes.length > 0) {
      const geoAssocTypeCollectionIdentifiers = geoAssocTypeCollection.map(geoAssocTypeItem =>
        this.getGeoAssocTypeIdentifier(geoAssocTypeItem),
      );
      const geoAssocTypesToAdd = geoAssocTypes.filter(geoAssocTypeItem => {
        const geoAssocTypeIdentifier = this.getGeoAssocTypeIdentifier(geoAssocTypeItem);
        if (geoAssocTypeCollectionIdentifiers.includes(geoAssocTypeIdentifier)) {
          return false;
        }
        geoAssocTypeCollectionIdentifiers.push(geoAssocTypeIdentifier);
        return true;
      });
      return [...geoAssocTypesToAdd, ...geoAssocTypeCollection];
    }
    return geoAssocTypeCollection;
  }
}
