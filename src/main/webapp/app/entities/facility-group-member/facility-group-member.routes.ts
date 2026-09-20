import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FacilityGroupMemberResolve from './route/facility-group-member-routing-resolve.service';

const facilityGroupMemberRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/facility-group-member').then(m => m.FacilityGroupMember),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/facility-group-member-detail').then(m => m.FacilityGroupMemberDetail),
    resolve: {
      facilityGroupMember: FacilityGroupMemberResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/facility-group-member-update').then(m => m.FacilityGroupMemberUpdate),
    resolve: {
      facilityGroupMember: FacilityGroupMemberResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/facility-group-member-update').then(m => m.FacilityGroupMemberUpdate),
    resolve: {
      facilityGroupMember: FacilityGroupMemberResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default facilityGroupMemberRoute;
