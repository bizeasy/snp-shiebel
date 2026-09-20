import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductFeatureType } from '../product-feature-type.model';
import { ProductFeatureTypeService } from '../service/product-feature-type.service';

const productFeatureTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductFeatureType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductFeatureTypeService);
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

export default productFeatureTypeResolve;
