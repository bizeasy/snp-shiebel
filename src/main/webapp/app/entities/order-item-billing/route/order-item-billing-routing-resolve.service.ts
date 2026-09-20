import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IOrderItemBilling } from '../order-item-billing.model';
import { OrderItemBillingService } from '../service/order-item-billing.service';

const orderItemBillingResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrderItemBilling> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(OrderItemBillingService);
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

export default orderItemBillingResolve;
