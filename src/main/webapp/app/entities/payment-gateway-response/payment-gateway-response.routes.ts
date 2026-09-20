import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PaymentGatewayResponseResolve from './route/payment-gateway-response-routing-resolve.service';

const paymentGatewayResponseRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/payment-gateway-response').then(m => m.PaymentGatewayResponse),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/payment-gateway-response-detail').then(m => m.PaymentGatewayResponseDetail),
    resolve: {
      paymentGatewayResponse: PaymentGatewayResponseResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/payment-gateway-response-update').then(m => m.PaymentGatewayResponseUpdate),
    resolve: {
      paymentGatewayResponse: PaymentGatewayResponseResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/payment-gateway-response-update').then(m => m.PaymentGatewayResponseUpdate),
    resolve: {
      paymentGatewayResponse: PaymentGatewayResponseResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default paymentGatewayResponseRoute;
