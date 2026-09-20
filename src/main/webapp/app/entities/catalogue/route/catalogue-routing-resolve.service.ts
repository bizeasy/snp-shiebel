import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICatalogue } from '../catalogue.model';
import { CatalogueService } from '../service/catalogue.service';

const catalogueResolve = (route: ActivatedRouteSnapshot): Observable<null | ICatalogue> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CatalogueService);
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

export default catalogueResolve;
