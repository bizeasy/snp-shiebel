import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FacilityTypeResolve from './route/facility-type-routing-resolve.service';

const facilityTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/facility-type').then(m => m.FacilityType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/facility-type-detail').then(m => m.FacilityTypeDetail),
    resolve: {
      facilityType: FacilityTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/facility-type-update').then(m => m.FacilityTypeUpdate),
    resolve: {
      facilityType: FacilityTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/facility-type-update').then(m => m.FacilityTypeUpdate),
    resolve: {
      facilityType: FacilityTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default facilityTypeRoute;
