import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductPriceType } from '../product-price-type.model';
import { ProductPriceTypeService } from '../service/product-price-type.service';

const productPriceTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductPriceType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductPriceTypeService);
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

export default productPriceTypeResolve;
