import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductStore } from '../product-store.model';
import { ProductStoreService } from '../service/product-store.service';

const productStoreResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductStore> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductStoreService);
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

export default productStoreResolve;
