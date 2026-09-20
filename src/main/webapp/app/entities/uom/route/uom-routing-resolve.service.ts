import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { UomService } from '../service/uom.service';
import { IUom } from '../uom.model';

const uomResolve = (route: ActivatedRouteSnapshot): Observable<null | IUom> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(UomService);
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

export default uomResolve;
