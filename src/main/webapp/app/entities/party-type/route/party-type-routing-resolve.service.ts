import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPartyType } from '../party-type.model';
import { PartyTypeService } from '../service/party-type.service';

const partyTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IPartyType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PartyTypeService);
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

export default partyTypeResolve;
