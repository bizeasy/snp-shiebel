import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IFacilityParty } from '../facility-party.model';
import { FacilityPartyService } from '../service/facility-party.service';

const facilityPartyResolve = (route: ActivatedRouteSnapshot): Observable<null | IFacilityParty> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(FacilityPartyService);
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

export default facilityPartyResolve;
