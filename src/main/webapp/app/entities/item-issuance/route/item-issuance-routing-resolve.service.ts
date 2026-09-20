import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IItemIssuance } from '../item-issuance.model';
import { ItemIssuanceService } from '../service/item-issuance.service';

const itemIssuanceResolve = (route: ActivatedRouteSnapshot): Observable<null | IItemIssuance> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ItemIssuanceService);
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

export default itemIssuanceResolve;
