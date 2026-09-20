import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import StatusValidChangeResolve from './route/status-valid-change-routing-resolve.service';

const statusValidChangeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/status-valid-change').then(m => m.StatusValidChange),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/status-valid-change-detail').then(m => m.StatusValidChangeDetail),
    resolve: {
      statusValidChange: StatusValidChangeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/status-valid-change-update').then(m => m.StatusValidChangeUpdate),
    resolve: {
      statusValidChange: StatusValidChangeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/status-valid-change-update').then(m => m.StatusValidChangeUpdate),
    resolve: {
      statusValidChange: StatusValidChangeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default statusValidChangeRoute;
