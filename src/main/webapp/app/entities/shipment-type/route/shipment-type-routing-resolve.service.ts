import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ShipmentTypeService } from '../service/shipment-type.service';
import { IShipmentType } from '../shipment-type.model';

const shipmentTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IShipmentType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ShipmentTypeService);
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

export default shipmentTypeResolve;
