import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductFacilityUser, NewProductFacilityUser } from '../product-facility-user.model';

export type PartialUpdateProductFacilityUser = Partial<IProductFacilityUser> & Pick<IProductFacilityUser, 'id'>;

type RestOf<T extends IProductFacilityUser | NewProductFacilityUser> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestProductFacilityUser = RestOf<IProductFacilityUser>;

export type NewRestProductFacilityUser = RestOf<NewProductFacilityUser>;

export type PartialUpdateRestProductFacilityUser = RestOf<PartialUpdateProductFacilityUser>;

@Service()
export class ProductFacilityUsersService {
  readonly productFacilityUsersParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productFacilityUsersResource = httpResource<RestProductFacilityUser[]>(() => {
    const params = this.productFacilityUsersParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productFacilityUser that have been fetched. It is updated when the productFacilityUsersResource emits a new value.
   * In case of error while fetching the productFacilityUsers, the signal is set to an empty array.
   */
  readonly productFacilityUsers = computed(() =>
    (this.productFacilityUsersResource.hasValue() ? this.productFacilityUsersResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-facility-users`;

  protected convertValueFromServer(restProductFacilityUser: RestProductFacilityUser): IProductFacilityUser {
    return {
      ...restProductFacilityUser,
      fromDate: restProductFacilityUser.fromDate ? dayjs(restProductFacilityUser.fromDate) : undefined,
      thruDate: restProductFacilityUser.thruDate ? dayjs(restProductFacilityUser.thruDate) : undefined,
    };
  }
}

@Service()
export class ProductFacilityUserService extends ProductFacilityUsersService {
  protected readonly http = inject(HttpClient);

  create(productFacilityUser: NewProductFacilityUser): Observable<IProductFacilityUser> {
    const copy = this.convertValueFromClient(productFacilityUser);
    return this.http.post<RestProductFacilityUser>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productFacilityUser: IProductFacilityUser): Observable<IProductFacilityUser> {
    const copy = this.convertValueFromClient(productFacilityUser);
    return this.http
      .put<RestProductFacilityUser>(
        `${this.resourceUrl}/${encodeURIComponent(this.getProductFacilityUserIdentifier(productFacilityUser))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productFacilityUser: PartialUpdateProductFacilityUser): Observable<IProductFacilityUser> {
    const copy = this.convertValueFromClient(productFacilityUser);
    return this.http
      .patch<RestProductFacilityUser>(
        `${this.resourceUrl}/${encodeURIComponent(this.getProductFacilityUserIdentifier(productFacilityUser))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IProductFacilityUser> {
    return this.http
      .get<RestProductFacilityUser>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IProductFacilityUser[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductFacilityUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductFacilityUserIdentifier(productFacilityUser: Pick<IProductFacilityUser, 'id'>): number {
    return productFacilityUser.id;
  }

  compareProductFacilityUser(o1: Pick<IProductFacilityUser, 'id'> | null, o2: Pick<IProductFacilityUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductFacilityUserIdentifier(o1) === this.getProductFacilityUserIdentifier(o2) : o1 === o2;
  }

  addProductFacilityUserToCollectionIfMissing<Type extends Pick<IProductFacilityUser, 'id'>>(
    productFacilityUserCollection: Type[],
    ...productFacilityUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productFacilityUsers: Type[] = productFacilityUsersToCheck.filter(
      productFacilityUserItem => productFacilityUserItem !== null && productFacilityUserItem !== undefined,
    );
    if (productFacilityUsers.length > 0) {
      const productFacilityUserCollectionIdentifiers = productFacilityUserCollection.map(productFacilityUserItem =>
        this.getProductFacilityUserIdentifier(productFacilityUserItem),
      );
      const productFacilityUsersToAdd = productFacilityUsers.filter(productFacilityUserItem => {
        const productFacilityUserIdentifier = this.getProductFacilityUserIdentifier(productFacilityUserItem);
        if (productFacilityUserCollectionIdentifiers.includes(productFacilityUserIdentifier)) {
          return false;
        }
        productFacilityUserCollectionIdentifiers.push(productFacilityUserIdentifier);
        return true;
      });
      return [...productFacilityUsersToAdd, ...productFacilityUserCollection];
    }
    return productFacilityUserCollection;
  }

  protected convertValueFromClient<T extends IProductFacilityUser | NewProductFacilityUser | PartialUpdateProductFacilityUser>(
    productFacilityUser: T,
  ): RestOf<T> {
    return {
      ...productFacilityUser,
      fromDate: productFacilityUser.fromDate?.toJSON() ?? null,
      thruDate: productFacilityUser.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestProductFacilityUser): IProductFacilityUser {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestProductFacilityUser[]): IProductFacilityUser[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
