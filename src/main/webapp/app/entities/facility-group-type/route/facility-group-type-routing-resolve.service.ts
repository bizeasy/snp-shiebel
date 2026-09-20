import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IFacilityGroupType } from '../facility-group-type.model';
import { FacilityGroupTypeService } from '../service/facility-group-type.service';

const facilityGroupTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IFacilityGroupType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(FacilityGroupTypeService);
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

export default facilityGroupTypeResolve;
