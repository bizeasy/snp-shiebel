import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IReturnHeader } from '../return-header.model';
import { ReturnHeaderService } from '../service/return-header.service';

const returnHeaderResolve = (route: ActivatedRouteSnapshot): Observable<null | IReturnHeader> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ReturnHeaderService);
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

export default returnHeaderResolve;
