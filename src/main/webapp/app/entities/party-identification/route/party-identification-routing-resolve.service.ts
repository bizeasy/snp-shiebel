import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPartyIdentification } from '../party-identification.model';
import { PartyIdentificationService } from '../service/party-identification.service';

const partyIdentificationResolve = (route: ActivatedRouteSnapshot): Observable<null | IPartyIdentification> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PartyIdentificationService);
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

export default partyIdentificationResolve;
