import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductFeatureApplType } from '../product-feature-appl-type.model';
import { ProductFeatureApplTypeService } from '../service/product-feature-appl-type.service';

const productFeatureApplTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductFeatureApplType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductFeatureApplTypeService);
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

export default productFeatureApplTypeResolve;
