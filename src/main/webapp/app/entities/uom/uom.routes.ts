import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import UomResolve from './route/uom-routing-resolve.service';

const uomRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/uom').then(m => m.Uom),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/uom-detail').then(m => m.UomDetail),
    resolve: {
      uom: UomResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/uom-update').then(m => m.UomUpdate),
    resolve: {
      uom: UomResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/uom-update').then(m => m.UomUpdate),
    resolve: {
      uom: UomResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default uomRoute;
