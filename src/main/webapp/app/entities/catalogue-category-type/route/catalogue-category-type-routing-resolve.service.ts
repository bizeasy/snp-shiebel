import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICatalogueCategoryType } from '../catalogue-category-type.model';
import { CatalogueCategoryTypeService } from '../service/catalogue-category-type.service';

const catalogueCategoryTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ICatalogueCategoryType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CatalogueCategoryTypeService);
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

export default catalogueCategoryTypeResolve;
