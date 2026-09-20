import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPicklistRole } from '../picklist-role.model';
import { PicklistRoleService } from '../service/picklist-role.service';

const picklistRoleResolve = (route: ActivatedRouteSnapshot): Observable<null | IPicklistRole> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PicklistRoleService);
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

export default picklistRoleResolve;
