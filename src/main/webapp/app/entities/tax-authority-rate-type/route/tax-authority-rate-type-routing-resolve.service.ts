import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TaxAuthorityRateTypeService } from '../service/tax-authority-rate-type.service';
import { ITaxAuthorityRateType } from '../tax-authority-rate-type.model';

const taxAuthorityRateTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ITaxAuthorityRateType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TaxAuthorityRateTypeService);
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

export default taxAuthorityRateTypeResolve;
