import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IDepartmentMember, NewDepartmentMember } from '../department-member.model';

export type PartialUpdateDepartmentMember = Partial<IDepartmentMember> & Pick<IDepartmentMember, 'id'>;

type RestOf<T extends IDepartmentMember | NewDepartmentMember> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestDepartmentMember = RestOf<IDepartmentMember>;

export type NewRestDepartmentMember = RestOf<NewDepartmentMember>;

export type PartialUpdateRestDepartmentMember = RestOf<PartialUpdateDepartmentMember>;

@Service()
export class DepartmentMembersService {
  readonly departmentMembersParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly departmentMembersResource = httpResource<RestDepartmentMember[]>(() => {
    const params = this.departmentMembersParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of departmentMember that have been fetched. It is updated when the departmentMembersResource emits a new value.
   * In case of error while fetching the departmentMembers, the signal is set to an empty array.
   */
  readonly departmentMembers = computed(() =>
    (this.departmentMembersResource.hasValue() ? this.departmentMembersResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/department-members`;

  protected convertValueFromServer(restDepartmentMember: RestDepartmentMember): IDepartmentMember {
    return {
      ...restDepartmentMember,
      fromDate: restDepartmentMember.fromDate ? dayjs(restDepartmentMember.fromDate) : undefined,
      thruDate: restDepartmentMember.thruDate ? dayjs(restDepartmentMember.thruDate) : undefined,
    };
  }
}

@Service()
export class DepartmentMemberService extends DepartmentMembersService {
  protected readonly http = inject(HttpClient);

  create(departmentMember: NewDepartmentMember): Observable<IDepartmentMember> {
    const copy = this.convertValueFromClient(departmentMember);
    return this.http.post<RestDepartmentMember>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(departmentMember: IDepartmentMember): Observable<IDepartmentMember> {
    const copy = this.convertValueFromClient(departmentMember);
    return this.http
      .put<RestDepartmentMember>(`${this.resourceUrl}/${encodeURIComponent(this.getDepartmentMemberIdentifier(departmentMember))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(departmentMember: PartialUpdateDepartmentMember): Observable<IDepartmentMember> {
    const copy = this.convertValueFromClient(departmentMember);
    return this.http
      .patch<RestDepartmentMember>(`${this.resourceUrl}/${encodeURIComponent(this.getDepartmentMemberIdentifier(departmentMember))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IDepartmentMember> {
    return this.http
      .get<RestDepartmentMember>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IDepartmentMember[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDepartmentMember[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getDepartmentMemberIdentifier(departmentMember: Pick<IDepartmentMember, 'id'>): number {
    return departmentMember.id;
  }

  compareDepartmentMember(o1: Pick<IDepartmentMember, 'id'> | null, o2: Pick<IDepartmentMember, 'id'> | null): boolean {
    return o1 && o2 ? this.getDepartmentMemberIdentifier(o1) === this.getDepartmentMemberIdentifier(o2) : o1 === o2;
  }

  addDepartmentMemberToCollectionIfMissing<Type extends Pick<IDepartmentMember, 'id'>>(
    departmentMemberCollection: Type[],
    ...departmentMembersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const departmentMembers: Type[] = departmentMembersToCheck.filter(
      departmentMemberItem => departmentMemberItem !== null && departmentMemberItem !== undefined,
    );
    if (departmentMembers.length > 0) {
      const departmentMemberCollectionIdentifiers = departmentMemberCollection.map(departmentMemberItem =>
        this.getDepartmentMemberIdentifier(departmentMemberItem),
      );
      const departmentMembersToAdd = departmentMembers.filter(departmentMemberItem => {
        const departmentMemberIdentifier = this.getDepartmentMemberIdentifier(departmentMemberItem);
        if (departmentMemberCollectionIdentifiers.includes(departmentMemberIdentifier)) {
          return false;
        }
        departmentMemberCollectionIdentifiers.push(departmentMemberIdentifier);
        return true;
      });
      return [...departmentMembersToAdd, ...departmentMemberCollection];
    }
    return departmentMemberCollection;
  }

  protected convertValueFromClient<T extends IDepartmentMember | NewDepartmentMember | PartialUpdateDepartmentMember>(
    departmentMember: T,
  ): RestOf<T> {
    return {
      ...departmentMember,
      fromDate: departmentMember.fromDate?.toJSON() ?? null,
      thruDate: departmentMember.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestDepartmentMember): IDepartmentMember {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestDepartmentMember[]): IDepartmentMember[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
