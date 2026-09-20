import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICatalogue, NewCatalogue } from '../catalogue.model';

export type PartialUpdateCatalogue = Partial<ICatalogue> & Pick<ICatalogue, 'id'>;

@Service()
export class CataloguesService {
  readonly cataloguesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly cataloguesResource = httpResource<ICatalogue[]>(() => {
    const params = this.cataloguesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of catalogue that have been fetched. It is updated when the cataloguesResource emits a new value.
   * In case of error while fetching the catalogues, the signal is set to an empty array.
   */
  readonly catalogues = computed(() => (this.cataloguesResource.hasValue() ? this.cataloguesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/catalogues`;
}

@Service()
export class CatalogueService extends CataloguesService {
  protected readonly http = inject(HttpClient);

  create(catalogue: NewCatalogue): Observable<ICatalogue> {
    return this.http.post<ICatalogue>(this.resourceUrl, catalogue);
  }

  update(catalogue: ICatalogue): Observable<ICatalogue> {
    return this.http.put<ICatalogue>(`${this.resourceUrl}/${encodeURIComponent(this.getCatalogueIdentifier(catalogue))}`, catalogue);
  }

  partialUpdate(catalogue: PartialUpdateCatalogue): Observable<ICatalogue> {
    return this.http.patch<ICatalogue>(`${this.resourceUrl}/${encodeURIComponent(this.getCatalogueIdentifier(catalogue))}`, catalogue);
  }

  find(id: number): Observable<ICatalogue> {
    return this.http.get<ICatalogue>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICatalogue[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCatalogueIdentifier(catalogue: Pick<ICatalogue, 'id'>): number {
    return catalogue.id;
  }

  compareCatalogue(o1: Pick<ICatalogue, 'id'> | null, o2: Pick<ICatalogue, 'id'> | null): boolean {
    return o1 && o2 ? this.getCatalogueIdentifier(o1) === this.getCatalogueIdentifier(o2) : o1 === o2;
  }

  addCatalogueToCollectionIfMissing<Type extends Pick<ICatalogue, 'id'>>(
    catalogueCollection: Type[],
    ...cataloguesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const catalogues: Type[] = cataloguesToCheck.filter(catalogueItem => catalogueItem !== null && catalogueItem !== undefined);
    if (catalogues.length > 0) {
      const catalogueCollectionIdentifiers = catalogueCollection.map(catalogueItem => this.getCatalogueIdentifier(catalogueItem));
      const cataloguesToAdd = catalogues.filter(catalogueItem => {
        const catalogueIdentifier = this.getCatalogueIdentifier(catalogueItem);
        if (catalogueCollectionIdentifiers.includes(catalogueIdentifier)) {
          return false;
        }
        catalogueCollectionIdentifiers.push(catalogueIdentifier);
        return true;
      });
      return [...cataloguesToAdd, ...catalogueCollection];
    }
    return catalogueCollection;
  }
}
