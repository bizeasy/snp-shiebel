import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductCategory } from '../product-category.model';
import { ProductCategoryService } from '../service/product-category.service';

const productCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductCategory> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductCategoryService);
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

export default productCategoryResolve;
