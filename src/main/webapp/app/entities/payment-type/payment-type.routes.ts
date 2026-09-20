import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PaymentTypeResolve from './route/payment-type-routing-resolve.service';

const paymentTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/payment-type').then(m => m.PaymentType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/payment-type-detail').then(m => m.PaymentTypeDetail),
    resolve: {
      paymentType: PaymentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/payment-type-update').then(m => m.PaymentTypeUpdate),
    resolve: {
      paymentType: PaymentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/payment-type-update').then(m => m.PaymentTypeUpdate),
    resolve: {
      paymentType: PaymentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default paymentTypeRoute;
