import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IGeoType } from '../geo-type.model';
import { GeoTypeService } from '../service/geo-type.service';

const geoTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IGeoType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(GeoTypeService);
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

export default geoTypeResolve;
