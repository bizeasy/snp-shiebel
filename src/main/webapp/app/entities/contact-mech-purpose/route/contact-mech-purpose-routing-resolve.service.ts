import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IContactMechPurpose } from '../contact-mech-purpose.model';
import { ContactMechPurposeService } from '../service/contact-mech-purpose.service';

const contactMechPurposeResolve = (route: ActivatedRouteSnapshot): Observable<null | IContactMechPurpose> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ContactMechPurposeService);
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

export default contactMechPurposeResolve;
