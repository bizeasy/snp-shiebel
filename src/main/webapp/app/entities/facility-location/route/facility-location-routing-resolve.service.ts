import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IFacilityLocation } from '../facility-location.model';
import { FacilityLocationService } from '../service/facility-location.service';

const facilityLocationResolve = (route: ActivatedRouteSnapshot): Observable<null | IFacilityLocation> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(FacilityLocationService);
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

export default facilityLocationResolve;
