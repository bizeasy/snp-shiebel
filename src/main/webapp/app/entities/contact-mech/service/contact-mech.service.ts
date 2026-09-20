import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IContactMech, NewContactMech } from '../contact-mech.model';

export type PartialUpdateContactMech = Partial<IContactMech> & Pick<IContactMech, 'id'>;

@Service()
export class ContactMechesService {
  readonly contactMechesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly contactMechesResource = httpResource<IContactMech[]>(() => {
    const params = this.contactMechesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of contactMech that have been fetched. It is updated when the contactMechesResource emits a new value.
   * In case of error while fetching the contactMeches, the signal is set to an empty array.
   */
  readonly contactMeches = computed(() => (this.contactMechesResource.hasValue() ? this.contactMechesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/contact-meches`;
}

@Service()
export class ContactMechService extends ContactMechesService {
  protected readonly http = inject(HttpClient);

  create(contactMech: NewContactMech): Observable<IContactMech> {
    return this.http.post<IContactMech>(this.resourceUrl, contactMech);
  }

  update(contactMech: IContactMech): Observable<IContactMech> {
    return this.http.put<IContactMech>(
      `${this.resourceUrl}/${encodeURIComponent(this.getContactMechIdentifier(contactMech))}`,
      contactMech,
    );
  }

  partialUpdate(contactMech: PartialUpdateContactMech): Observable<IContactMech> {
    return this.http.patch<IContactMech>(
      `${this.resourceUrl}/${encodeURIComponent(this.getContactMechIdentifier(contactMech))}`,
      contactMech,
    );
  }

  find(id: number): Observable<IContactMech> {
    return this.http.get<IContactMech>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IContactMech[]>> {
    const options = createRequestOption(req);
    return this.http.get<IContactMech[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getContactMechIdentifier(contactMech: Pick<IContactMech, 'id'>): number {
    return contactMech.id;
  }

  compareContactMech(o1: Pick<IContactMech, 'id'> | null, o2: Pick<IContactMech, 'id'> | null): boolean {
    return o1 && o2 ? this.getContactMechIdentifier(o1) === this.getContactMechIdentifier(o2) : o1 === o2;
  }

  addContactMechToCollectionIfMissing<Type extends Pick<IContactMech, 'id'>>(
    contactMechCollection: Type[],
    ...contactMechesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contactMeches: Type[] = contactMechesToCheck.filter(contactMechItem => contactMechItem !== null && contactMechItem !== undefined);
    if (contactMeches.length > 0) {
      const contactMechCollectionIdentifiers = contactMechCollection.map(contactMechItem => this.getContactMechIdentifier(contactMechItem));
      const contactMechesToAdd = contactMeches.filter(contactMechItem => {
        const contactMechIdentifier = this.getContactMechIdentifier(contactMechItem);
        if (contactMechCollectionIdentifiers.includes(contactMechIdentifier)) {
          return false;
        }
        contactMechCollectionIdentifiers.push(contactMechIdentifier);
        return true;
      });
      return [...contactMechesToAdd, ...contactMechCollection];
    }
    return contactMechCollection;
  }
}
