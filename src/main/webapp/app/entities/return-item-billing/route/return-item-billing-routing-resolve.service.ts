import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IReturnItemBilling } from '../return-item-billing.model';
import { ReturnItemBillingService } from '../service/return-item-billing.service';

const returnItemBillingResolve = (route: ActivatedRouteSnapshot): Observable<null | IReturnItemBilling> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ReturnItemBillingService);
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

export default returnItemBillingResolve;
