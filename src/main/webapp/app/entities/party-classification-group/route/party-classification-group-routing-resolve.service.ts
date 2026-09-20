import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPartyClassificationGroup } from '../party-classification-group.model';
import { PartyClassificationGroupService } from '../service/party-classification-group.service';

const partyClassificationGroupResolve = (route: ActivatedRouteSnapshot): Observable<null | IPartyClassificationGroup> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PartyClassificationGroupService);
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

export default partyClassificationGroupResolve;
