import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPaymentGatewayConfig } from '../payment-gateway-config.model';
import { PaymentGatewayConfigService } from '../service/payment-gateway-config.service';

const paymentGatewayConfigResolve = (route: ActivatedRouteSnapshot): Observable<null | IPaymentGatewayConfig> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PaymentGatewayConfigService);
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

export default paymentGatewayConfigResolve;
