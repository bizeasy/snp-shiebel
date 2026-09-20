import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IInventoryItemType } from '../inventory-item-type.model';
import { InventoryItemTypeService } from '../service/inventory-item-type.service';

const inventoryItemTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IInventoryItemType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(InventoryItemTypeService);
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

export default inventoryItemTypeResolve;
