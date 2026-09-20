import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductFeature } from '../product-feature.model';
import { ProductFeatureService } from '../service/product-feature.service';

const productFeatureResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductFeature> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductFeatureService);
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

export default productFeatureResolve;
