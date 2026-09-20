import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ShoppingListTypeService } from '../service/shopping-list-type.service';
import { IShoppingListType } from '../shopping-list-type.model';

const shoppingListTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IShoppingListType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ShoppingListTypeService);
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

export default shoppingListTypeResolve;
