import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrderStatusResolve from './route/order-status-routing-resolve.service';

const orderStatusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-status').then(m => m.OrderStatus),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-status-detail').then(m => m.OrderStatusDetail),
    resolve: {
      orderStatus: OrderStatusResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-status-update').then(m => m.OrderStatusUpdate),
    resolve: {
      orderStatus: OrderStatusResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-status-update').then(m => m.OrderStatusUpdate),
    resolve: {
      orderStatus: OrderStatusResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default orderStatusRoute;
