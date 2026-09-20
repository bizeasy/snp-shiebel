import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IGeo } from '../geo.model';
import { GeoService } from '../service/geo.service';

const geoResolve = (route: ActivatedRouteSnapshot): Observable<null | IGeo> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(GeoService);
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

export default geoResolve;
