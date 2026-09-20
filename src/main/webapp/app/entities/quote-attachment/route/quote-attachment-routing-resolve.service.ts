import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IQuoteAttachment } from '../quote-attachment.model';
import { QuoteAttachmentService } from '../service/quote-attachment.service';

const quoteAttachmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuoteAttachment> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(QuoteAttachmentService);
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

export default quoteAttachmentResolve;
