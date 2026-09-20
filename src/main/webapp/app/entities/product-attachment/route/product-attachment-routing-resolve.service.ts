import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductAttachment } from '../product-attachment.model';
import { ProductAttachmentService } from '../service/product-attachment.service';

const productAttachmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductAttachment> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductAttachmentService);
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

export default productAttachmentResolve;
