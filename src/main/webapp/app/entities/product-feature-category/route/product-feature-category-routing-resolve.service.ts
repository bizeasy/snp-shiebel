import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductFeatureCategory } from '../product-feature-category.model';
import { ProductFeatureCategoryService } from '../service/product-feature-category.service';

const productFeatureCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductFeatureCategory> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductFeatureCategoryService);
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

export default productFeatureCategoryResolve;
