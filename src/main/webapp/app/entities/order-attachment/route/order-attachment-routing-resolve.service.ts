import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IOrderAttachment } from '../order-attachment.model';
import { OrderAttachmentService } from '../service/order-attachment.service';

const orderAttachmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrderAttachment> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(OrderAttachmentService);
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

export default orderAttachmentResolve;
