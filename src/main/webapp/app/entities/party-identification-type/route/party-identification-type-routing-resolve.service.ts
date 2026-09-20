import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPartyIdentificationType } from '../party-identification-type.model';
import { PartyIdentificationTypeService } from '../service/party-identification-type.service';

const partyIdentificationTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IPartyIdentificationType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PartyIdentificationTypeService);
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

export default partyIdentificationTypeResolve;
