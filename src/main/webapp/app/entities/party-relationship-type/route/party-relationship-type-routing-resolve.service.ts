import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPartyRelationshipType } from '../party-relationship-type.model';
import { PartyRelationshipTypeService } from '../service/party-relationship-type.service';

const partyRelationshipTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IPartyRelationshipType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PartyRelationshipTypeService);
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

export default partyRelationshipTypeResolve;
