import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ShipmentReceiptService } from '../service/shipment-receipt.service';
import { IShipmentReceipt } from '../shipment-receipt.model';

const shipmentReceiptResolve = (route: ActivatedRouteSnapshot): Observable<null | IShipmentReceipt> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ShipmentReceiptService);
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

export default shipmentReceiptResolve;
