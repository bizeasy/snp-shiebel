import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ISalesChannel } from '../sales-channel.model';
import { SalesChannelService } from '../service/sales-channel.service';

const salesChannelResolve = (route: ActivatedRouteSnapshot): Observable<null | ISalesChannel> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(SalesChannelService);
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

export default salesChannelResolve;
