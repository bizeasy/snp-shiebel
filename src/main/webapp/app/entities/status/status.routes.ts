import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import StatusResolve from './route/status-routing-resolve.service';

const statusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/status').then(m => m.Status),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/status-detail').then(m => m.StatusDetail),
    resolve: {
      status: StatusResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/status-update').then(m => m.StatusUpdate),
    resolve: {
      status: StatusResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/status-update').then(m => m.StatusUpdate),
    resolve: {
      status: StatusResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default statusRoute;
