import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ReturnHeaderTypeResolve from './route/return-header-type-routing-resolve.service';

const returnHeaderTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/return-header-type').then(m => m.ReturnHeaderType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/return-header-type-detail').then(m => m.ReturnHeaderTypeDetail),
    resolve: {
      returnHeaderType: ReturnHeaderTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/return-header-type-update').then(m => m.ReturnHeaderTypeUpdate),
    resolve: {
      returnHeaderType: ReturnHeaderTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/return-header-type-update').then(m => m.ReturnHeaderTypeUpdate),
    resolve: {
      returnHeaderType: ReturnHeaderTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default returnHeaderTypeRoute;
