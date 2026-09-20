import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ShipmentStatusService } from '../service/shipment-status.service';
import { IShipmentStatus } from '../shipment-status.model';

const shipmentStatusResolve = (route: ActivatedRouteSnapshot): Observable<null | IShipmentStatus> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ShipmentStatusService);
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

export default shipmentStatusResolve;
