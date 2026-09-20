import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IQuoteRole } from '../quote-role.model';
import { QuoteRoleService } from '../service/quote-role.service';

const quoteRoleResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuoteRole> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(QuoteRoleService);
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

export default quoteRoleResolve;
