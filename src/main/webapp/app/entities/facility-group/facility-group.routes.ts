import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FacilityGroupResolve from './route/facility-group-routing-resolve.service';

const facilityGroupRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/facility-group').then(m => m.FacilityGroup),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/facility-group-detail').then(m => m.FacilityGroupDetail),
    resolve: {
      facilityGroup: FacilityGroupResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/facility-group-update').then(m => m.FacilityGroupUpdate),
    resolve: {
      facilityGroup: FacilityGroupResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/facility-group-update').then(m => m.FacilityGroupUpdate),
    resolve: {
      facilityGroup: FacilityGroupResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default facilityGroupRoute;
