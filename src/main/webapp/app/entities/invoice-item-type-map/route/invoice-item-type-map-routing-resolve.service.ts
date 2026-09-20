import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IInvoiceItemTypeMap } from '../invoice-item-type-map.model';
import { InvoiceItemTypeMapService } from '../service/invoice-item-type-map.service';

const invoiceItemTypeMapResolve = (route: ActivatedRouteSnapshot): Observable<null | IInvoiceItemTypeMap> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(InvoiceItemTypeMapService);
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

export default invoiceItemTypeMapResolve;
