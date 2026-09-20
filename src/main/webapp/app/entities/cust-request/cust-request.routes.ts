import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CustRequestResolve from './route/cust-request-routing-resolve.service';

const custRequestRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cust-request').then(m => m.CustRequest),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cust-request-detail').then(m => m.CustRequestDetail),
    resolve: {
      custRequest: CustRequestResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cust-request-update').then(m => m.CustRequestUpdate),
    resolve: {
      custRequest: CustRequestResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cust-request-update').then(m => m.CustRequestUpdate),
    resolve: {
      custRequest: CustRequestResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default custRequestRoute;
