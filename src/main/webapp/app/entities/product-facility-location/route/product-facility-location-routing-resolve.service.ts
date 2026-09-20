import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductFacilityLocation } from '../product-facility-location.model';
import { ProductFacilityLocationService } from '../service/product-facility-location.service';

const productFacilityLocationResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductFacilityLocation> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductFacilityLocationService);
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

export default productFacilityLocationResolve;
