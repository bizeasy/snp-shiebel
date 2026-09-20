import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FacilityResolve from './route/facility-routing-resolve.service';

const facilityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/facility').then(m => m.Facility),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/facility-detail').then(m => m.FacilityDetail),
    resolve: {
      facility: FacilityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/facility-update').then(m => m.FacilityUpdate),
    resolve: {
      facility: FacilityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/facility-update').then(m => m.FacilityUpdate),
    resolve: {
      facility: FacilityResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default facilityRoute;
