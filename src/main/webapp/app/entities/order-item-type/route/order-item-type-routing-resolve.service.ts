import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IOrderItemType } from '../order-item-type.model';
import { OrderItemTypeService } from '../service/order-item-type.service';

const orderItemTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrderItemType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(OrderItemTypeService);
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

export default orderItemTypeResolve;
