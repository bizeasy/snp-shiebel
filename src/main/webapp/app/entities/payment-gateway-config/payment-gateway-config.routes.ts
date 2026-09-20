import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PaymentGatewayConfigResolve from './route/payment-gateway-config-routing-resolve.service';

const paymentGatewayConfigRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/payment-gateway-config').then(m => m.PaymentGatewayConfig),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/payment-gateway-config-detail').then(m => m.PaymentGatewayConfigDetail),
    resolve: {
      paymentGatewayConfig: PaymentGatewayConfigResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/payment-gateway-config-update').then(m => m.PaymentGatewayConfigUpdate),
    resolve: {
      paymentGatewayConfig: PaymentGatewayConfigResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/payment-gateway-config-update').then(m => m.PaymentGatewayConfigUpdate),
    resolve: {
      paymentGatewayConfig: PaymentGatewayConfigResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default paymentGatewayConfigRoute;
