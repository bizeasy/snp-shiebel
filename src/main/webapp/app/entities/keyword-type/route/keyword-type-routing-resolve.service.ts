import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IKeywordType } from '../keyword-type.model';
import { KeywordTypeService } from '../service/keyword-type.service';

const keywordTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IKeywordType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(KeywordTypeService);
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

export default keywordTypeResolve;
