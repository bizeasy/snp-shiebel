import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TaxAuthorityService } from '../service/tax-authority.service';
import { ITaxAuthority } from '../tax-authority.model';

const taxAuthorityResolve = (route: ActivatedRouteSnapshot): Observable<null | ITaxAuthority> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TaxAuthorityService);
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

export default taxAuthorityResolve;
