import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductAssoc } from '../product-assoc.model';
import { ProductAssocService } from '../service/product-assoc.service';

const productAssocResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductAssoc> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductAssocService);
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

export default productAssocResolve;
