import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrderAdjustmentResolve from './route/order-adjustment-routing-resolve.service';

const orderAdjustmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-adjustment').then(m => m.OrderAdjustment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-adjustment-detail').then(m => m.OrderAdjustmentDetail),
    resolve: {
      orderAdjustment: OrderAdjustmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-adjustment-update').then(m => m.OrderAdjustmentUpdate),
    resolve: {
      orderAdjustment: OrderAdjustmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-adjustment-update').then(m => m.OrderAdjustmentUpdate),
    resolve: {
      orderAdjustment: OrderAdjustmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default orderAdjustmentRoute;
