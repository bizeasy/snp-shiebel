import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IContactMechType, NewContactMechType } from '../contact-mech-type.model';

export type PartialUpdateContactMechType = Partial<IContactMechType> & Pick<IContactMechType, 'id'>;

@Service()
export class ContactMechTypesService {
  readonly contactMechTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly contactMechTypesResource = httpResource<IContactMechType[]>(() => {
    const params = this.contactMechTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of contactMechType that have been fetched. It is updated when the contactMechTypesResource emits a new value.
   * In case of error while fetching the contactMechTypes, the signal is set to an empty array.
   */
  readonly contactMechTypes = computed(() => (this.contactMechTypesResource.hasValue() ? this.contactMechTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/contact-mech-types`;
}

@Service()
export class ContactMechTypeService extends ContactMechTypesService {
  protected readonly http = inject(HttpClient);

  create(contactMechType: NewContactMechType): Observable<IContactMechType> {
    return this.http.post<IContactMechType>(this.resourceUrl, contactMechType);
  }

  update(contactMechType: IContactMechType): Observable<IContactMechType> {
    return this.http.put<IContactMechType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getContactMechTypeIdentifier(contactMechType))}`,
      contactMechType,
    );
  }

  partialUpdate(contactMechType: PartialUpdateContactMechType): Observable<IContactMechType> {
    return this.http.patch<IContactMechType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getContactMechTypeIdentifier(contactMechType))}`,
      contactMechType,
    );
  }

  find(id: number): Observable<IContactMechType> {
    return this.http.get<IContactMechType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IContactMechType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IContactMechType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getContactMechTypeIdentifier(contactMechType: Pick<IContactMechType, 'id'>): number {
    return contactMechType.id;
  }

  compareContactMechType(o1: Pick<IContactMechType, 'id'> | null, o2: Pick<IContactMechType, 'id'> | null): boolean {
    return o1 && o2 ? this.getContactMechTypeIdentifier(o1) === this.getContactMechTypeIdentifier(o2) : o1 === o2;
  }

  addContactMechTypeToCollectionIfMissing<Type extends Pick<IContactMechType, 'id'>>(
    contactMechTypeCollection: Type[],
    ...contactMechTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contactMechTypes: Type[] = contactMechTypesToCheck.filter(
      contactMechTypeItem => contactMechTypeItem !== null && contactMechTypeItem !== undefined,
    );
    if (contactMechTypes.length > 0) {
      const contactMechTypeCollectionIdentifiers = contactMechTypeCollection.map(contactMechTypeItem =>
        this.getContactMechTypeIdentifier(contactMechTypeItem),
      );
      const contactMechTypesToAdd = contactMechTypes.filter(contactMechTypeItem => {
        const contactMechTypeIdentifier = this.getContactMechTypeIdentifier(contactMechTypeItem);
        if (contactMechTypeCollectionIdentifiers.includes(contactMechTypeIdentifier)) {
          return false;
        }
        contactMechTypeCollectionIdentifiers.push(contactMechTypeIdentifier);
        return true;
      });
      return [...contactMechTypesToAdd, ...contactMechTypeCollection];
    }
    return contactMechTypeCollection;
  }
}
