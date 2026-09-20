import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import TaxAuthorityRateTypeResolve from './route/tax-authority-rate-type-routing-resolve.service';

const taxAuthorityRateTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tax-authority-rate-type').then(m => m.TaxAuthorityRateType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tax-authority-rate-type-detail').then(m => m.TaxAuthorityRateTypeDetail),
    resolve: {
      taxAuthorityRateType: TaxAuthorityRateTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tax-authority-rate-type-update').then(m => m.TaxAuthorityRateTypeUpdate),
    resolve: {
      taxAuthorityRateType: TaxAuthorityRateTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tax-authority-rate-type-update').then(m => m.TaxAuthorityRateTypeUpdate),
    resolve: {
      taxAuthorityRateType: TaxAuthorityRateTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default taxAuthorityRateTypeRoute;
