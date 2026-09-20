import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import SystemParametersResolve from './route/system-parameters-routing-resolve.service';

const systemParametersRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/system-parameters').then(m => m.SystemParameters),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/system-parameters-detail').then(m => m.SystemParametersDetail),
    resolve: {
      systemParameters: SystemParametersResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/system-parameters-update').then(m => m.SystemParametersUpdate),
    resolve: {
      systemParameters: SystemParametersResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/system-parameters-update').then(m => m.SystemParametersUpdate),
    resolve: {
      systemParameters: SystemParametersResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default systemParametersRoute;
