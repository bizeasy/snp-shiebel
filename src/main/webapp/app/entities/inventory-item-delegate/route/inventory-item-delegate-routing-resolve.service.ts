import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IInventoryItemDelegate } from '../inventory-item-delegate.model';
import { InventoryItemDelegateService } from '../service/inventory-item-delegate.service';

const inventoryItemDelegateResolve = (route: ActivatedRouteSnapshot): Observable<null | IInventoryItemDelegate> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(InventoryItemDelegateService);
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

export default inventoryItemDelegateResolve;
