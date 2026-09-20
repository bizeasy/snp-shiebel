import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductType } from '../product-type.model';
import { ProductTypeService } from '../service/product-type.service';

const productTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductTypeService);
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

export default productTypeResolve;
