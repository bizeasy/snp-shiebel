import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import TaxSlabResolve from './route/tax-slab-routing-resolve.service';

const taxSlabRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tax-slab').then(m => m.TaxSlab),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tax-slab-detail').then(m => m.TaxSlabDetail),
    resolve: {
      taxSlab: TaxSlabResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tax-slab-update').then(m => m.TaxSlabUpdate),
    resolve: {
      taxSlab: TaxSlabResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tax-slab-update').then(m => m.TaxSlabUpdate),
    resolve: {
      taxSlab: TaxSlabResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default taxSlabRoute;
