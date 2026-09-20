import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PaymentMethodTypeResolve from './route/payment-method-type-routing-resolve.service';

const paymentMethodTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/payment-method-type').then(m => m.PaymentMethodType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/payment-method-type-detail').then(m => m.PaymentMethodTypeDetail),
    resolve: {
      paymentMethodType: PaymentMethodTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/payment-method-type-update').then(m => m.PaymentMethodTypeUpdate),
    resolve: {
      paymentMethodType: PaymentMethodTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/payment-method-type-update').then(m => m.PaymentMethodTypeUpdate),
    resolve: {
      paymentMethodType: PaymentMethodTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default paymentMethodTypeRoute;
