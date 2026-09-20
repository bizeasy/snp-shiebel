import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IContentType } from '../content-type.model';
import { ContentTypeService } from '../service/content-type.service';

const contentTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IContentType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ContentTypeService);
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

export default contentTypeResolve;
