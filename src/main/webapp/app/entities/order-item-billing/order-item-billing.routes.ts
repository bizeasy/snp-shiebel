import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrderItemBillingResolve from './route/order-item-billing-routing-resolve.service';

const orderItemBillingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-item-billing').then(m => m.OrderItemBilling),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-item-billing-detail').then(m => m.OrderItemBillingDetail),
    resolve: {
      orderItemBilling: OrderItemBillingResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-item-billing-update').then(m => m.OrderItemBillingUpdate),
    resolve: {
      orderItemBilling: OrderItemBillingResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-item-billing-update').then(m => m.OrderItemBillingUpdate),
    resolve: {
      orderItemBilling: OrderItemBillingResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default orderItemBillingRoute;
