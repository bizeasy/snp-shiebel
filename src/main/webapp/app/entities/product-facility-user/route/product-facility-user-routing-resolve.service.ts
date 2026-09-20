import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductFacilityUser } from '../product-facility-user.model';
import { ProductFacilityUserService } from '../service/product-facility-user.service';

const productFacilityUserResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductFacilityUser> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductFacilityUserService);
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

export default productFacilityUserResolve;
