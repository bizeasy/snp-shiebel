import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ReturnHeaderResolve from './route/return-header-routing-resolve.service';

const returnHeaderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/return-header').then(m => m.ReturnHeader),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/return-header-detail').then(m => m.ReturnHeaderDetail),
    resolve: {
      returnHeader: ReturnHeaderResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/return-header-update').then(m => m.ReturnHeaderUpdate),
    resolve: {
      returnHeader: ReturnHeaderResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/return-header-update').then(m => m.ReturnHeaderUpdate),
    resolve: {
      returnHeader: ReturnHeaderResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default returnHeaderRoute;
