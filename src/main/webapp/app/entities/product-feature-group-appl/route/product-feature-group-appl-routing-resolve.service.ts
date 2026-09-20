import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IProductFeatureGroupAppl } from '../product-feature-group-appl.model';
import { ProductFeatureGroupApplService } from '../service/product-feature-group-appl.service';

const productFeatureGroupApplResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductFeatureGroupAppl> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ProductFeatureGroupApplService);
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

export default productFeatureGroupApplResolve;
