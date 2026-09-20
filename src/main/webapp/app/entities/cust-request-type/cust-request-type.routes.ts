import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CustRequestTypeResolve from './route/cust-request-type-routing-resolve.service';

const custRequestTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cust-request-type').then(m => m.CustRequestType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cust-request-type-detail').then(m => m.CustRequestTypeDetail),
    resolve: {
      custRequestType: CustRequestTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cust-request-type-update').then(m => m.CustRequestTypeUpdate),
    resolve: {
      custRequestType: CustRequestTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cust-request-type-update').then(m => m.CustRequestTypeUpdate),
    resolve: {
      custRequestType: CustRequestTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default custRequestTypeRoute;
