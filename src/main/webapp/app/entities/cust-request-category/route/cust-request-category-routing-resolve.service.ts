import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICustRequestCategory } from '../cust-request-category.model';
import { CustRequestCategoryService } from '../service/cust-request-category.service';

const custRequestCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustRequestCategory> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CustRequestCategoryService);
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

export default custRequestCategoryResolve;
