import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPartyClassification } from '../party-classification.model';
import { PartyClassificationService } from '../service/party-classification.service';

const partyClassificationResolve = (route: ActivatedRouteSnapshot): Observable<null | IPartyClassification> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PartyClassificationService);
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

export default partyClassificationResolve;
