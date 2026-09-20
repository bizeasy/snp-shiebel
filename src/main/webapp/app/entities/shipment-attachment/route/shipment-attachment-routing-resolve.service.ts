import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ShipmentAttachmentService } from '../service/shipment-attachment.service';
import { IShipmentAttachment } from '../shipment-attachment.model';

const shipmentAttachmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IShipmentAttachment> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ShipmentAttachmentService);
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

export default shipmentAttachmentResolve;
