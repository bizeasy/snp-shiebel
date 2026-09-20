import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IFacility } from '../facility.model';
import { FacilityService } from '../service/facility.service';

const facilityResolve = (route: ActivatedRouteSnapshot): Observable<null | IFacility> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(FacilityService);
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

export default facilityResolve;
