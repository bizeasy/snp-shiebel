import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IGeoPoint } from '../geo-point.model';
import { GeoPointService } from '../service/geo-point.service';

const geoPointResolve = (route: ActivatedRouteSnapshot): Observable<null | IGeoPoint> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(GeoPointService);
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

export default geoPointResolve;
