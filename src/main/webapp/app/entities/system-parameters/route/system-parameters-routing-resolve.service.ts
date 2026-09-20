import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { SystemParametersService } from '../service/system-parameters.service';
import { ISystemParameters } from '../system-parameters.model';

const systemParametersResolve = (route: ActivatedRouteSnapshot): Observable<null | ISystemParameters> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(SystemParametersService);
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

export default systemParametersResolve;
