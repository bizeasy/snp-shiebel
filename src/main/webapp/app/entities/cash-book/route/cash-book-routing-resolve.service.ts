import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICashBook } from '../cash-book.model';
import { CashBookService } from '../service/cash-book.service';

const cashBookResolve = (route: ActivatedRouteSnapshot): Observable<null | ICashBook> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CashBookService);
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

export default cashBookResolve;
