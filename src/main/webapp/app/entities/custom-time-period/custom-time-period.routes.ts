import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CustomTimePeriodResolve from './route/custom-time-period-routing-resolve.service';

const customTimePeriodRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/custom-time-period').then(m => m.CustomTimePeriod),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/custom-time-period-detail').then(m => m.CustomTimePeriodDetail),
    resolve: {
      customTimePeriod: CustomTimePeriodResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/custom-time-period-update').then(m => m.CustomTimePeriodUpdate),
    resolve: {
      customTimePeriod: CustomTimePeriodResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/custom-time-period-update').then(m => m.CustomTimePeriodUpdate),
    resolve: {
      customTimePeriod: CustomTimePeriodResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default customTimePeriodRoute;
