import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFacilityGroupMember, NewFacilityGroupMember } from '../facility-group-member.model';

export type PartialUpdateFacilityGroupMember = Partial<IFacilityGroupMember> & Pick<IFacilityGroupMember, 'id'>;

type RestOf<T extends IFacilityGroupMember | NewFacilityGroupMember> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestFacilityGroupMember = RestOf<IFacilityGroupMember>;

export type NewRestFacilityGroupMember = RestOf<NewFacilityGroupMember>;

export type PartialUpdateRestFacilityGroupMember = RestOf<PartialUpdateFacilityGroupMember>;

@Service()
export class FacilityGroupMembersService {
  readonly facilityGroupMembersParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly facilityGroupMembersResource = httpResource<RestFacilityGroupMember[]>(() => {
    const params = this.facilityGroupMembersParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of facilityGroupMember that have been fetched. It is updated when the facilityGroupMembersResource emits a new value.
   * In case of error while fetching the facilityGroupMembers, the signal is set to an empty array.
   */
  readonly facilityGroupMembers = computed(() =>
    (this.facilityGroupMembersResource.hasValue() ? this.facilityGroupMembersResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/facility-group-members`;

  protected convertValueFromServer(restFacilityGroupMember: RestFacilityGroupMember): IFacilityGroupMember {
    return {
      ...restFacilityGroupMember,
      fromDate: restFacilityGroupMember.fromDate ? dayjs(restFacilityGroupMember.fromDate) : undefined,
      thruDate: restFacilityGroupMember.thruDate ? dayjs(restFacilityGroupMember.thruDate) : undefined,
    };
  }
}

@Service()
export class FacilityGroupMemberService extends FacilityGroupMembersService {
  protected readonly http = inject(HttpClient);

  create(facilityGroupMember: NewFacilityGroupMember): Observable<IFacilityGroupMember> {
    const copy = this.convertValueFromClient(facilityGroupMember);
    return this.http.post<RestFacilityGroupMember>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(facilityGroupMember: IFacilityGroupMember): Observable<IFacilityGroupMember> {
    const copy = this.convertValueFromClient(facilityGroupMember);
    return this.http
      .put<RestFacilityGroupMember>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFacilityGroupMemberIdentifier(facilityGroupMember))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(facilityGroupMember: PartialUpdateFacilityGroupMember): Observable<IFacilityGroupMember> {
    const copy = this.convertValueFromClient(facilityGroupMember);
    return this.http
      .patch<RestFacilityGroupMember>(
        `${this.resourceUrl}/${encodeURIComponent(this.getFacilityGroupMemberIdentifier(facilityGroupMember))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFacilityGroupMember> {
    return this.http
      .get<RestFacilityGroupMember>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFacilityGroupMember[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFacilityGroupMember[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFacilityGroupMemberIdentifier(facilityGroupMember: Pick<IFacilityGroupMember, 'id'>): number {
    return facilityGroupMember.id;
  }

  compareFacilityGroupMember(o1: Pick<IFacilityGroupMember, 'id'> | null, o2: Pick<IFacilityGroupMember, 'id'> | null): boolean {
    return o1 && o2 ? this.getFacilityGroupMemberIdentifier(o1) === this.getFacilityGroupMemberIdentifier(o2) : o1 === o2;
  }

  addFacilityGroupMemberToCollectionIfMissing<Type extends Pick<IFacilityGroupMember, 'id'>>(
    facilityGroupMemberCollection: Type[],
    ...facilityGroupMembersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facilityGroupMembers: Type[] = facilityGroupMembersToCheck.filter(
      facilityGroupMemberItem => facilityGroupMemberItem !== null && facilityGroupMemberItem !== undefined,
    );
    if (facilityGroupMembers.length > 0) {
      const facilityGroupMemberCollectionIdentifiers = facilityGroupMemberCollection.map(facilityGroupMemberItem =>
        this.getFacilityGroupMemberIdentifier(facilityGroupMemberItem),
      );
      const facilityGroupMembersToAdd = facilityGroupMembers.filter(facilityGroupMemberItem => {
        const facilityGroupMemberIdentifier = this.getFacilityGroupMemberIdentifier(facilityGroupMemberItem);
        if (facilityGroupMemberCollectionIdentifiers.includes(facilityGroupMemberIdentifier)) {
          return false;
        }
        facilityGroupMemberCollectionIdentifiers.push(facilityGroupMemberIdentifier);
        return true;
      });
      return [...facilityGroupMembersToAdd, ...facilityGroupMemberCollection];
    }
    return facilityGroupMemberCollection;
  }

  protected convertValueFromClient<T extends IFacilityGroupMember | NewFacilityGroupMember | PartialUpdateFacilityGroupMember>(
    facilityGroupMember: T,
  ): RestOf<T> {
    return {
      ...facilityGroupMember,
      fromDate: facilityGroupMember.fromDate?.toJSON() ?? null,
      thruDate: facilityGroupMember.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFacilityGroupMember): IFacilityGroupMember {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFacilityGroupMember[]): IFacilityGroupMember[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
