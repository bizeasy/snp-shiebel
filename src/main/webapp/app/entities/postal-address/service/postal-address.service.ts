import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPostalAddress, NewPostalAddress } from '../postal-address.model';

export type PartialUpdatePostalAddress = Partial<IPostalAddress> & Pick<IPostalAddress, 'id'>;

@Service()
export class PostalAddressesService {
  readonly postalAddressesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly postalAddressesResource = httpResource<IPostalAddress[]>(() => {
    const params = this.postalAddressesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of postalAddress that have been fetched. It is updated when the postalAddressesResource emits a new value.
   * In case of error while fetching the postalAddresses, the signal is set to an empty array.
   */
  readonly postalAddresses = computed(() => (this.postalAddressesResource.hasValue() ? this.postalAddressesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/postal-addresses`;
}

@Service()
export class PostalAddressService extends PostalAddressesService {
  protected readonly http = inject(HttpClient);

  create(postalAddress: NewPostalAddress): Observable<IPostalAddress> {
    return this.http.post<IPostalAddress>(this.resourceUrl, postalAddress);
  }

  update(postalAddress: IPostalAddress): Observable<IPostalAddress> {
    return this.http.put<IPostalAddress>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPostalAddressIdentifier(postalAddress))}`,
      postalAddress,
    );
  }

  partialUpdate(postalAddress: PartialUpdatePostalAddress): Observable<IPostalAddress> {
    return this.http.patch<IPostalAddress>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPostalAddressIdentifier(postalAddress))}`,
      postalAddress,
    );
  }

  find(id: number): Observable<IPostalAddress> {
    return this.http.get<IPostalAddress>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPostalAddress[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPostalAddress[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPostalAddressIdentifier(postalAddress: Pick<IPostalAddress, 'id'>): number {
    return postalAddress.id;
  }

  comparePostalAddress(o1: Pick<IPostalAddress, 'id'> | null, o2: Pick<IPostalAddress, 'id'> | null): boolean {
    return o1 && o2 ? this.getPostalAddressIdentifier(o1) === this.getPostalAddressIdentifier(o2) : o1 === o2;
  }

  addPostalAddressToCollectionIfMissing<Type extends Pick<IPostalAddress, 'id'>>(
    postalAddressCollection: Type[],
    ...postalAddressesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const postalAddresses: Type[] = postalAddressesToCheck.filter(
      postalAddressItem => postalAddressItem !== null && postalAddressItem !== undefined,
    );
    if (postalAddresses.length > 0) {
      const postalAddressCollectionIdentifiers = postalAddressCollection.map(postalAddressItem =>
        this.getPostalAddressIdentifier(postalAddressItem),
      );
      const postalAddressesToAdd = postalAddresses.filter(postalAddressItem => {
        const postalAddressIdentifier = this.getPostalAddressIdentifier(postalAddressItem);
        if (postalAddressCollectionIdentifiers.includes(postalAddressIdentifier)) {
          return false;
        }
        postalAddressCollectionIdentifiers.push(postalAddressIdentifier);
        return true;
      });
      return [...postalAddressesToAdd, ...postalAddressCollection];
    }
    return postalAddressCollection;
  }
}
