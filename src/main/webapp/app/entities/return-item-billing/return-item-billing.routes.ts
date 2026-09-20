import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ReturnItemBillingResolve from './route/return-item-billing-routing-resolve.service';

const returnItemBillingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/return-item-billing').then(m => m.ReturnItemBilling),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/return-item-billing-detail').then(m => m.ReturnItemBillingDetail),
    resolve: {
      returnItemBilling: ReturnItemBillingResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/return-item-billing-update').then(m => m.ReturnItemBillingUpdate),
    resolve: {
      returnItemBilling: ReturnItemBillingResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/return-item-billing-update').then(m => m.ReturnItemBillingUpdate),
    resolve: {
      returnItemBilling: ReturnItemBillingResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default returnItemBillingRoute;
