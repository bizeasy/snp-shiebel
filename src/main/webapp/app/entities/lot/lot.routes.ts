import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import LotResolve from './route/lot-routing-resolve.service';

const lotRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/lot').then(m => m.Lot),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/lot-detail').then(m => m.LotDetail),
    resolve: {
      lot: LotResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/lot-update').then(m => m.LotUpdate),
    resolve: {
      lot: LotResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/lot-update').then(m => m.LotUpdate),
    resolve: {
      lot: LotResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default lotRoute;
