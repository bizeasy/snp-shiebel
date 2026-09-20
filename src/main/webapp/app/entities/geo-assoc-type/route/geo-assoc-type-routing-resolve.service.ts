import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IGeoAssocType } from '../geo-assoc-type.model';
import { GeoAssocTypeService } from '../service/geo-assoc-type.service';

const geoAssocTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IGeoAssocType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(GeoAssocTypeService);
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

export default geoAssocTypeResolve;
