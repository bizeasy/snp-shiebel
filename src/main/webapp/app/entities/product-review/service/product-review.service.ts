import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductReview, NewProductReview } from '../product-review.model';

export type PartialUpdateProductReview = Partial<IProductReview> & Pick<IProductReview, 'id'>;

type RestOf<T extends IProductReview | NewProductReview> = Omit<T, 'reviewDate'> & {
  reviewDate?: string | null;
};

export type RestProductReview = RestOf<IProductReview>;

export type NewRestProductReview = RestOf<NewProductReview>;

export type PartialUpdateRestProductReview = RestOf<PartialUpdateProductReview>;

@Service()
export class ProductReviewsService {
  readonly productReviewsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productReviewsResource = httpResource<RestProductReview[]>(() => {
    const params = this.productReviewsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productReview that have been fetched. It is updated when the productReviewsResource emits a new value.
   * In case of error while fetching the productReviews, the signal is set to an empty array.
   */
  readonly productReviews = computed(() =>
    (this.productReviewsResource.hasValue() ? this.productReviewsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-reviews`;

  protected convertValueFromServer(restProductReview: RestProductReview): IProductReview {
    return {
      ...restProductReview,
      reviewDate: restProductReview.reviewDate ? dayjs(restProductReview.reviewDate) : undefined,
    };
  }
}

@Service()
export class ProductReviewService extends ProductReviewsService {
  protected readonly http = inject(HttpClient);

  create(productReview: NewProductReview): Observable<IProductReview> {
    const copy = this.convertValueFromClient(productReview);
    return this.http.post<RestProductReview>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productReview: IProductReview): Observable<IProductReview> {
    const copy = this.convertValueFromClient(productReview);
    return this.http
      .put<RestProductReview>(`${this.resourceUrl}/${encodeURIComponent(this.getProductReviewIdentifier(productReview))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productReview: PartialUpdateProductReview): Observable<IProductReview> {
    const copy = this.convertValueFromClient(productReview);
    return this.http
      .patch<RestProductReview>(`${this.resourceUrl}/${encodeURIComponent(this.getProductReviewIdentifier(productReview))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IProductReview> {
    return this.http
      .get<RestProductReview>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IProductReview[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductReview[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductReviewIdentifier(productReview: Pick<IProductReview, 'id'>): number {
    return productReview.id;
  }

  compareProductReview(o1: Pick<IProductReview, 'id'> | null, o2: Pick<IProductReview, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductReviewIdentifier(o1) === this.getProductReviewIdentifier(o2) : o1 === o2;
  }

  addProductReviewToCollectionIfMissing<Type extends Pick<IProductReview, 'id'>>(
    productReviewCollection: Type[],
    ...productReviewsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productReviews: Type[] = productReviewsToCheck.filter(
      productReviewItem => productReviewItem !== null && productReviewItem !== undefined,
    );
    if (productReviews.length > 0) {
      const productReviewCollectionIdentifiers = productReviewCollection.map(productReviewItem =>
        this.getProductReviewIdentifier(productReviewItem),
      );
      const productReviewsToAdd = productReviews.filter(productReviewItem => {
        const productReviewIdentifier = this.getProductReviewIdentifier(productReviewItem);
        if (productReviewCollectionIdentifiers.includes(productReviewIdentifier)) {
          return false;
        }
        productReviewCollectionIdentifiers.push(productReviewIdentifier);
        return true;
      });
      return [...productReviewsToAdd, ...productReviewCollection];
    }
    return productReviewCollection;
  }

  protected convertValueFromClient<T extends IProductReview | NewProductReview | PartialUpdateProductReview>(productReview: T): RestOf<T> {
    return {
      ...productReview,
      reviewDate: productReview.reviewDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestProductReview): IProductReview {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestProductReview[]): IProductReview[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
