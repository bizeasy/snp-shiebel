import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPickListItem } from '../pick-list-item.model';
import { PickListItemService } from '../service/pick-list-item.service';

const pickListItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IPickListItem> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PickListItemService);
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

export default pickListItemResolve;
