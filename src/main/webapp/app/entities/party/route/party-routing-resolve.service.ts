import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IParty } from '../party.model';
import { PartyService } from '../service/party.service';

const partyResolve = (route: ActivatedRouteSnapshot): Observable<null | IParty> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PartyService);
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

export default partyResolve;
