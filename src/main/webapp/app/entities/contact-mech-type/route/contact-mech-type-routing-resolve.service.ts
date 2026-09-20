import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IContactMechType } from '../contact-mech-type.model';
import { ContactMechTypeService } from '../service/contact-mech-type.service';

const contactMechTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IContactMechType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ContactMechTypeService);
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

export default contactMechTypeResolve;
