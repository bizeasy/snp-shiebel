import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { UomTypeService } from '../service/uom-type.service';
import { IUomType } from '../uom-type.model';

const uomTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IUomType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(UomTypeService);
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

export default uomTypeResolve;
