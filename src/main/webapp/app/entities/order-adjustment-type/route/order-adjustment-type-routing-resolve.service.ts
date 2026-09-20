import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IOrderAdjustmentType } from '../order-adjustment-type.model';
import { OrderAdjustmentTypeService } from '../service/order-adjustment-type.service';

const orderAdjustmentTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrderAdjustmentType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(OrderAdjustmentTypeService);
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

export default orderAdjustmentTypeResolve;
