import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IContactMech } from '../contact-mech.model';
import { ContactMechService } from '../service/contact-mech.service';

const contactMechResolve = (route: ActivatedRouteSnapshot): Observable<null | IContactMech> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ContactMechService);
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

export default contactMechResolve;
