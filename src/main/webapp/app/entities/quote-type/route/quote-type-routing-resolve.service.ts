import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IQuoteType } from '../quote-type.model';
import { QuoteTypeService } from '../service/quote-type.service';

const quoteTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuoteType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(QuoteTypeService);
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

export default quoteTypeResolve;
