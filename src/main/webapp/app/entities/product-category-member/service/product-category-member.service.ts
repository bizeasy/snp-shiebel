import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductCategoryMember, NewProductCategoryMember } from '../product-category-member.model';

export type PartialUpdateProductCategoryMember = Partial<IProductCategoryMember> & Pick<IProductCategoryMember, 'id'>;

type RestOf<T extends IProductCategoryMember | NewProductCategoryMember> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestProductCategoryMember = RestOf<IProductCategoryMember>;

export type NewRestProductCategoryMember = RestOf<NewProductCategoryMember>;

export type PartialUpdateRestProductCategoryMember = RestOf<PartialUpdateProductCategoryMember>;

@Service()
export class ProductCategoryMembersService {
  readonly productCategoryMembersParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productCategoryMembersResource = httpResource<RestProductCategoryMember[]>(() => {
    const params = this.productCategoryMembersParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productCategoryMember that have been fetched. It is updated when the productCategoryMembersResource emits a new value.
   * In case of error while fetching the productCategoryMembers, the signal is set to an empty array.
   */
  readonly productCategoryMembers = computed(() =>
    (this.productCategoryMembersResource.hasValue() ? this.productCategoryMembersResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-category-members`;

  protected convertValueFromServer(restProductCategoryMember: RestProductCategoryMember): IProductCategoryMember {
    return {
      ...restProductCategoryMember,
      fromDate: restProductCategoryMember.fromDate ? dayjs(restProductCategoryMember.fromDate) : undefined,
      thruDate: restProductCategoryMember.thruDate ? dayjs(restProductCategoryMember.thruDate) : undefined,
    };
  }
}

@Service()
export class ProductCategoryMemberService extends ProductCategoryMembersService {
  protected readonly http = inject(HttpClient);

  create(productCategoryMember: NewProductCategoryMember): Observable<IProductCategoryMember> {
    const copy = this.convertValueFromClient(productCategoryMember);
    return this.http.post<RestProductCategoryMember>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productCategoryMember: IProductCategoryMember): Observable<IProductCategoryMember> {
    const copy = this.convertValueFromClient(productCategoryMember);
    return this.http
      .put<RestProductCategoryMember>(
        `${this.resourceUrl}/${encodeURIComponent(this.getProductCategoryMemberIdentifier(productCategoryMember))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productCategoryMember: PartialUpdateProductCategoryMember): Observable<IProductCategoryMember> {
    const copy = this.convertValueFromClient(productCategoryMember);
    return this.http
      .patch<RestProductCategoryMember>(
        `${this.resourceUrl}/${encodeURIComponent(this.getProductCategoryMemberIdentifier(productCategoryMember))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IProductCategoryMember> {
    return this.http
      .get<RestProductCategoryMember>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IProductCategoryMember[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductCategoryMember[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductCategoryMemberIdentifier(productCategoryMember: Pick<IProductCategoryMember, 'id'>): number {
    return productCategoryMember.id;
  }

  compareProductCategoryMember(o1: Pick<IProductCategoryMember, 'id'> | null, o2: Pick<IProductCategoryMember, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductCategoryMemberIdentifier(o1) === this.getProductCategoryMemberIdentifier(o2) : o1 === o2;
  }

  addProductCategoryMemberToCollectionIfMissing<Type extends Pick<IProductCategoryMember, 'id'>>(
    productCategoryMemberCollection: Type[],
    ...productCategoryMembersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productCategoryMembers: Type[] = productCategoryMembersToCheck.filter(
      productCategoryMemberItem => productCategoryMemberItem !== null && productCategoryMemberItem !== undefined,
    );
    if (productCategoryMembers.length > 0) {
      const productCategoryMemberCollectionIdentifiers = productCategoryMemberCollection.map(productCategoryMemberItem =>
        this.getProductCategoryMemberIdentifier(productCategoryMemberItem),
      );
      const productCategoryMembersToAdd = productCategoryMembers.filter(productCategoryMemberItem => {
        const productCategoryMemberIdentifier = this.getProductCategoryMemberIdentifier(productCategoryMemberItem);
        if (productCategoryMemberCollectionIdentifiers.includes(productCategoryMemberIdentifier)) {
          return false;
        }
        productCategoryMemberCollectionIdentifiers.push(productCategoryMemberIdentifier);
        return true;
      });
      return [...productCategoryMembersToAdd, ...productCategoryMemberCollection];
    }
    return productCategoryMemberCollection;
  }

  protected convertValueFromClient<T extends IProductCategoryMember | NewProductCategoryMember | PartialUpdateProductCategoryMember>(
    productCategoryMember: T,
  ): RestOf<T> {
    return {
      ...productCategoryMember,
      fromDate: productCategoryMember.fromDate?.toJSON() ?? null,
      thruDate: productCategoryMember.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestProductCategoryMember): IProductCategoryMember {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestProductCategoryMember[]): IProductCategoryMember[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
