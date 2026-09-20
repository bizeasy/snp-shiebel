import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CustRequestRoleResolve from './route/cust-request-role-routing-resolve.service';

const custRequestRoleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cust-request-role').then(m => m.CustRequestRole),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cust-request-role-detail').then(m => m.CustRequestRoleDetail),
    resolve: {
      custRequestRole: CustRequestRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cust-request-role-update').then(m => m.CustRequestRoleUpdate),
    resolve: {
      custRequestRole: CustRequestRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cust-request-role-update').then(m => m.CustRequestRoleUpdate),
    resolve: {
      custRequestRole: CustRequestRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default custRequestRoleRoute;
