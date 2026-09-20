import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IUom, NewUom } from '../uom.model';

export type PartialUpdateUom = Partial<IUom> & Pick<IUom, 'id'>;

@Service()
export class UomsService {
  readonly uomsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly uomsResource = httpResource<IUom[]>(() => {
    const params = this.uomsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of uom that have been fetched. It is updated when the uomsResource emits a new value.
   * In case of error while fetching the uoms, the signal is set to an empty array.
   */
  readonly uoms = computed(() => (this.uomsResource.hasValue() ? this.uomsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/uoms`;
}

@Service()
export class UomService extends UomsService {
  protected readonly http = inject(HttpClient);

  create(uom: NewUom): Observable<IUom> {
    return this.http.post<IUom>(this.resourceUrl, uom);
  }

  update(uom: IUom): Observable<IUom> {
    return this.http.put<IUom>(`${this.resourceUrl}/${encodeURIComponent(this.getUomIdentifier(uom))}`, uom);
  }

  partialUpdate(uom: PartialUpdateUom): Observable<IUom> {
    return this.http.patch<IUom>(`${this.resourceUrl}/${encodeURIComponent(this.getUomIdentifier(uom))}`, uom);
  }

  find(id: number): Observable<IUom> {
    return this.http.get<IUom>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IUom[]>> {
    const options = createRequestOption(req);
    return this.http.get<IUom[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getUomIdentifier(uom: Pick<IUom, 'id'>): number {
    return uom.id;
  }

  compareUom(o1: Pick<IUom, 'id'> | null, o2: Pick<IUom, 'id'> | null): boolean {
    return o1 && o2 ? this.getUomIdentifier(o1) === this.getUomIdentifier(o2) : o1 === o2;
  }

  addUomToCollectionIfMissing<Type extends Pick<IUom, 'id'>>(uomCollection: Type[], ...uomsToCheck: (Type | null | undefined)[]): Type[] {
    const uoms: Type[] = uomsToCheck.filter(uomItem => uomItem !== null && uomItem !== undefined);
    if (uoms.length > 0) {
      const uomCollectionIdentifiers = uomCollection.map(uomItem => this.getUomIdentifier(uomItem));
      const uomsToAdd = uoms.filter(uomItem => {
        const uomIdentifier = this.getUomIdentifier(uomItem);
        if (uomCollectionIdentifiers.includes(uomIdentifier)) {
          return false;
        }
        uomCollectionIdentifiers.push(uomIdentifier);
        return true;
      });
      return [...uomsToAdd, ...uomCollection];
    }
    return uomCollection;
  }
}
