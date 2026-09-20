import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICustRequestAttachment } from '../cust-request-attachment.model';
import { CustRequestAttachmentService } from '../service/cust-request-attachment.service';

const custRequestAttachmentResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustRequestAttachment> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CustRequestAttachmentService);
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

export default custRequestAttachmentResolve;
