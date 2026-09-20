import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FacilityLocationResolve from './route/facility-location-routing-resolve.service';

const facilityLocationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/facility-location').then(m => m.FacilityLocation),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/facility-location-detail').then(m => m.FacilityLocationDetail),
    resolve: {
      facilityLocation: FacilityLocationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/facility-location-update').then(m => m.FacilityLocationUpdate),
    resolve: {
      facilityLocation: FacilityLocationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/facility-location-update').then(m => m.FacilityLocationUpdate),
    resolve: {
      facilityLocation: FacilityLocationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default facilityLocationRoute;
