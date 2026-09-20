import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrderItemTypeResolve from './route/order-item-type-routing-resolve.service';

const orderItemTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-item-type').then(m => m.OrderItemType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-item-type-detail').then(m => m.OrderItemTypeDetail),
    resolve: {
      orderItemType: OrderItemTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-item-type-update').then(m => m.OrderItemTypeUpdate),
    resolve: {
      orderItemType: OrderItemTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-item-type-update').then(m => m.OrderItemTypeUpdate),
    resolve: {
      orderItemType: OrderItemTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default orderItemTypeRoute;
