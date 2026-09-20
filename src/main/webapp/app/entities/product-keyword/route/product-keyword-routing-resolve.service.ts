import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductKeyword } from '../product-keyword.model';
import { ProductKeywordService } from '../service/product-keyword.service';

const productKeywordResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductKeyword> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductKeywordService);
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

export default productKeywordResolve;
