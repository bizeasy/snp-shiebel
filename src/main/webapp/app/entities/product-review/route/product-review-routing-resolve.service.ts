import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductReview } from '../product-review.model';
import { ProductReviewService } from '../service/product-review.service';

const productReviewResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductReview> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductReviewService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default productReviewResolve;
