import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IReturnHeaderType } from '../return-header-type.model';
import { ReturnHeaderTypeService } from '../service/return-header-type.service';

const returnHeaderTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IReturnHeaderType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ReturnHeaderTypeService);
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

export default returnHeaderTypeResolve;
