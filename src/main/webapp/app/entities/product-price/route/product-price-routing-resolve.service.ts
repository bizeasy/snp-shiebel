import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductPrice } from '../product-price.model';
import { ProductPriceService } from '../service/product-price.service';

const productPriceResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductPrice> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductPriceService);
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

export default productPriceResolve;
