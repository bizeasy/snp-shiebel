import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPartyRole } from '../party-role.model';
import { PartyRoleService } from '../service/party-role.service';

const partyRoleResolve = (route: ActivatedRouteSnapshot): Observable<null | IPartyRole> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PartyRoleService);
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

export default partyRoleResolve;
