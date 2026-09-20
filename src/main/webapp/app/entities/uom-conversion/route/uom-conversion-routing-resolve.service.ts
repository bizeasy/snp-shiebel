import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { UomConversionService } from '../service/uom-conversion.service';
import { IUomConversion } from '../uom-conversion.model';

const uomConversionResolve = (route: ActivatedRouteSnapshot): Observable<null | IUomConversion> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(UomConversionService);
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

export default uomConversionResolve;
