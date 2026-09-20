import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPickList } from '../pick-list.model';
import { PickListService } from '../service/pick-list.service';

const pickListResolve = (route: ActivatedRouteSnapshot): Observable<null | IPickList> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PickListService);
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

export default pickListResolve;
