import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ILocationType } from '../location-type.model';
import { LocationTypeService } from '../service/location-type.service';

const locationTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ILocationType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(LocationTypeService);
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

export default locationTypeResolve;
