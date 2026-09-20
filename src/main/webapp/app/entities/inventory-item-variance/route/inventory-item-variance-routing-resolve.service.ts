import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IInventoryItemVariance } from '../inventory-item-variance.model';
import { InventoryItemVarianceService } from '../service/inventory-item-variance.service';

const inventoryItemVarianceResolve = (route: ActivatedRouteSnapshot): Observable<null | IInventoryItemVariance> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(InventoryItemVarianceService);
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

export default inventoryItemVarianceResolve;
