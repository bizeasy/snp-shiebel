import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IInventoryTransfer } from '../inventory-transfer.model';
import { InventoryTransferService } from '../service/inventory-transfer.service';

const inventoryTransferResolve = (route: ActivatedRouteSnapshot): Observable<null | IInventoryTransfer> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(InventoryTransferService);
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

export default inventoryTransferResolve;
