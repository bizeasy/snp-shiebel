import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PaymentApplicationResolve from './route/payment-application-routing-resolve.service';

const paymentApplicationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/payment-application').then(m => m.PaymentApplication),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/payment-application-detail').then(m => m.PaymentApplicationDetail),
    resolve: {
      paymentApplication: PaymentApplicationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/payment-application-update').then(m => m.PaymentApplicationUpdate),
    resolve: {
      paymentApplication: PaymentApplicationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/payment-application-update').then(m => m.PaymentApplicationUpdate),
    resolve: {
      paymentApplication: PaymentApplicationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default paymentApplicationRoute;
