import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IFacilityType } from '../facility-type.model';
import { FacilityTypeService } from '../service/facility-type.service';

const facilityTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IFacilityType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(FacilityTypeService);
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

export default facilityTypeResolve;
