import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICustRequestItemAttachment } from '../cust-request-item-attachment.model';
import { CustRequestItemAttachmentService } from '../service/cust-request-item-attachment.service';

const custRequestItemAttachmentResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustRequestItemAttachment> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CustRequestItemAttachmentService);
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

export default custRequestItemAttachmentResolve;
