import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FacilityGroupTypeResolve from './route/facility-group-type-routing-resolve.service';

const facilityGroupTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/facility-group-type').then(m => m.FacilityGroupType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/facility-group-type-detail').then(m => m.FacilityGroupTypeDetail),
    resolve: {
      facilityGroupType: FacilityGroupTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/facility-group-type-update').then(m => m.FacilityGroupTypeUpdate),
    resolve: {
      facilityGroupType: FacilityGroupTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/facility-group-type-update').then(m => m.FacilityGroupTypeUpdate),
    resolve: {
      facilityGroupType: FacilityGroupTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default facilityGroupTypeRoute;
