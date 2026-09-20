import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IFileFolder } from '../file-folder.model';
import { FileFolderService } from '../service/file-folder.service';

const fileFolderResolve = (route: ActivatedRouteSnapshot): Observable<null | IFileFolder> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(FileFolderService);
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

export default fileFolderResolve;
