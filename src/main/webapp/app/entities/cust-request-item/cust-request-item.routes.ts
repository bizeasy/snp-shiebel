import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CustRequestItemResolve from './route/cust-request-item-routing-resolve.service';

const custRequestItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cust-request-item').then(m => m.CustRequestItem),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cust-request-item-detail').then(m => m.CustRequestItemDetail),
    resolve: {
      custRequestItem: CustRequestItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cust-request-item-update').then(m => m.CustRequestItemUpdate),
    resolve: {
      custRequestItem: CustRequestItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cust-request-item-update').then(m => m.CustRequestItemUpdate),
    resolve: {
      custRequestItem: CustRequestItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default custRequestItemRoute;
