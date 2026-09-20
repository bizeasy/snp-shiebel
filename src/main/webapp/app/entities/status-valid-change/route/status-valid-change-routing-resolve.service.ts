import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { StatusValidChangeService } from '../service/status-valid-change.service';
import { IStatusValidChange } from '../status-valid-change.model';

const statusValidChangeResolve = (route: ActivatedRouteSnapshot): Observable<null | IStatusValidChange> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(StatusValidChangeService);
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

export default statusValidChangeResolve;
