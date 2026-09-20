import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrderTypeResolve from './route/order-type-routing-resolve.service';

const orderTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-type').then(m => m.OrderType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-type-detail').then(m => m.OrderTypeDetail),
    resolve: {
      orderType: OrderTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-type-update').then(m => m.OrderTypeUpdate),
    resolve: {
      orderType: OrderTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-type-update').then(m => m.OrderTypeUpdate),
    resolve: {
      orderType: OrderTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default orderTypeRoute;
