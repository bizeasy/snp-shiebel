import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductPricePurpose } from '../product-price-purpose.model';
import { ProductPricePurposeService } from '../service/product-price-purpose.service';

const productPricePurposeResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductPricePurpose> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductPricePurposeService);
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

export default productPricePurposeResolve;
