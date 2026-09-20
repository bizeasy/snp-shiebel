import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IContactMechPurpose, NewContactMechPurpose } from '../contact-mech-purpose.model';

export type PartialUpdateContactMechPurpose = Partial<IContactMechPurpose> & Pick<IContactMechPurpose, 'id'>;

@Service()
export class ContactMechPurposesService {
  readonly contactMechPurposesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly contactMechPurposesResource = httpResource<IContactMechPurpose[]>(() => {
    const params = this.contactMechPurposesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of contactMechPurpose that have been fetched. It is updated when the contactMechPurposesResource emits a new value.
   * In case of error while fetching the contactMechPurposes, the signal is set to an empty array.
   */
  readonly contactMechPurposes = computed(() =>
    this.contactMechPurposesResource.hasValue() ? this.contactMechPurposesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/contact-mech-purposes`;
}

@Service()
export class ContactMechPurposeService extends ContactMechPurposesService {
  protected readonly http = inject(HttpClient);

  create(contactMechPurpose: NewContactMechPurpose): Observable<IContactMechPurpose> {
    return this.http.post<IContactMechPurpose>(this.resourceUrl, contactMechPurpose);
  }

  update(contactMechPurpose: IContactMechPurpose): Observable<IContactMechPurpose> {
    return this.http.put<IContactMechPurpose>(
      `${this.resourceUrl}/${encodeURIComponent(this.getContactMechPurposeIdentifier(contactMechPurpose))}`,
      contactMechPurpose,
    );
  }

  partialUpdate(contactMechPurpose: PartialUpdateContactMechPurpose): Observable<IContactMechPurpose> {
    return this.http.patch<IContactMechPurpose>(
      `${this.resourceUrl}/${encodeURIComponent(this.getContactMechPurposeIdentifier(contactMechPurpose))}`,
      contactMechPurpose,
    );
  }

  find(id: number): Observable<IContactMechPurpose> {
    return this.http.get<IContactMechPurpose>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IContactMechPurpose[]>> {
    const options = createRequestOption(req);
    return this.http.get<IContactMechPurpose[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getContactMechPurposeIdentifier(contactMechPurpose: Pick<IContactMechPurpose, 'id'>): number {
    return contactMechPurpose.id;
  }

  compareContactMechPurpose(o1: Pick<IContactMechPurpose, 'id'> | null, o2: Pick<IContactMechPurpose, 'id'> | null): boolean {
    return o1 && o2 ? this.getContactMechPurposeIdentifier(o1) === this.getContactMechPurposeIdentifier(o2) : o1 === o2;
  }

  addContactMechPurposeToCollectionIfMissing<Type extends Pick<IContactMechPurpose, 'id'>>(
    contactMechPurposeCollection: Type[],
    ...contactMechPurposesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contactMechPurposes: Type[] = contactMechPurposesToCheck.filter(
      contactMechPurposeItem => contactMechPurposeItem !== null && contactMechPurposeItem !== undefined,
    );
    if (contactMechPurposes.length > 0) {
      const contactMechPurposeCollectionIdentifiers = contactMechPurposeCollection.map(contactMechPurposeItem =>
        this.getContactMechPurposeIdentifier(contactMechPurposeItem),
      );
      const contactMechPurposesToAdd = contactMechPurposes.filter(contactMechPurposeItem => {
        const contactMechPurposeIdentifier = this.getContactMechPurposeIdentifier(contactMechPurposeItem);
        if (contactMechPurposeCollectionIdentifiers.includes(contactMechPurposeIdentifier)) {
          return false;
        }
        contactMechPurposeCollectionIdentifiers.push(contactMechPurposeIdentifier);
        return true;
      });
      return [...contactMechPurposesToAdd, ...contactMechPurposeCollection];
    }
    return contactMechPurposeCollection;
  }
}
