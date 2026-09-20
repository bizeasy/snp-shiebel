import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PaymentResolve from './route/payment-routing-resolve.service';

const paymentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/payment').then(m => m.Payment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/payment-detail').then(m => m.PaymentDetail),
    resolve: {
      payment: PaymentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/payment-update').then(m => m.PaymentUpdate),
    resolve: {
      payment: PaymentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/payment-update').then(m => m.PaymentUpdate),
    resolve: {
      payment: PaymentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default paymentRoute;
