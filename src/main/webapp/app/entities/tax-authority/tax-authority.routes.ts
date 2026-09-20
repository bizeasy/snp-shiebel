import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import TaxAuthorityResolve from './route/tax-authority-routing-resolve.service';

const taxAuthorityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tax-authority').then(m => m.TaxAuthority),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tax-authority-detail').then(m => m.TaxAuthorityDetail),
    resolve: {
      taxAuthority: TaxAuthorityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tax-authority-update').then(m => m.TaxAuthorityUpdate),
    resolve: {
      taxAuthority: TaxAuthorityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tax-authority-update').then(m => m.TaxAuthorityUpdate),
    resolve: {
      taxAuthority: TaxAuthorityResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default taxAuthorityRoute;
