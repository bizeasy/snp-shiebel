import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICustRequest } from '../cust-request.model';
import { CustRequestService } from '../service/cust-request.service';

const custRequestResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustRequest> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CustRequestService);
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

export default custRequestResolve;
