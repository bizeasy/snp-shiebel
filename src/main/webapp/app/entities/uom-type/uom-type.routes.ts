import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import UomTypeResolve from './route/uom-type-routing-resolve.service';

const uomTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/uom-type').then(m => m.UomType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/uom-type-detail').then(m => m.UomTypeDetail),
    resolve: {
      uomType: UomTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/uom-type-update').then(m => m.UomTypeUpdate),
    resolve: {
      uomType: UomTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/uom-type-update').then(m => m.UomTypeUpdate),
    resolve: {
      uomType: UomTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default uomTypeRoute;
