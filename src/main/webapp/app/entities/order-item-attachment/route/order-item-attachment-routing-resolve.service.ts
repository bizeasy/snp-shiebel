import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IOrderItemAttachment } from '../order-item-attachment.model';
import { OrderItemAttachmentService } from '../service/order-item-attachment.service';

const orderItemAttachmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrderItemAttachment> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(OrderItemAttachmentService);
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

export default orderItemAttachmentResolve;
