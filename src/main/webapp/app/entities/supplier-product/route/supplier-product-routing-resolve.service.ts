import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { SupplierProductService } from '../service/supplier-product.service';
import { ISupplierProduct } from '../supplier-product.model';

const supplierProductResolve = (route: ActivatedRouteSnapshot): Observable<null | ISupplierProduct> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(SupplierProductService);
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

export default supplierProductResolve;
