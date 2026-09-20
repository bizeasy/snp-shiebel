import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrderRoleResolve from './route/order-role-routing-resolve.service';

const orderRoleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-role').then(m => m.OrderRole),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-role-detail').then(m => m.OrderRoleDetail),
    resolve: {
      orderRole: OrderRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-role-update').then(m => m.OrderRoleUpdate),
    resolve: {
      orderRole: OrderRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-role-update').then(m => m.OrderRoleUpdate),
    resolve: {
      orderRole: OrderRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default orderRoleRoute;
