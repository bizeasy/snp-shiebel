import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrderAdjustmentTypeResolve from './route/order-adjustment-type-routing-resolve.service';

const orderAdjustmentTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-adjustment-type').then(m => m.OrderAdjustmentType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-adjustment-type-detail').then(m => m.OrderAdjustmentTypeDetail),
    resolve: {
      orderAdjustmentType: OrderAdjustmentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-adjustment-type-update').then(m => m.OrderAdjustmentTypeUpdate),
    resolve: {
      orderAdjustmentType: OrderAdjustmentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-adjustment-type-update').then(m => m.OrderAdjustmentTypeUpdate),
    resolve: {
      orderAdjustmentType: OrderAdjustmentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default orderAdjustmentTypeRoute;
