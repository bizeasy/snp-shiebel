import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductFacility } from '../product-facility.model';
import { ProductFacilityService } from '../service/product-facility.service';

const productFacilityResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductFacility> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductFacilityService);
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

export default productFacilityResolve;
