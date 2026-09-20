import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IOrderType } from '../order-type.model';
import { OrderTypeService } from '../service/order-type.service';

const orderTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrderType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(OrderTypeService);
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

export default orderTypeResolve;
