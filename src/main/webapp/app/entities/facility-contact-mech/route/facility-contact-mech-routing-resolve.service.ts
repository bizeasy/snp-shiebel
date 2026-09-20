import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IFacilityContactMech } from '../facility-contact-mech.model';
import { FacilityContactMechService } from '../service/facility-contact-mech.service';

const facilityContactMechResolve = (route: ActivatedRouteSnapshot): Observable<null | IFacilityContactMech> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(FacilityContactMechService);
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

export default facilityContactMechResolve;
