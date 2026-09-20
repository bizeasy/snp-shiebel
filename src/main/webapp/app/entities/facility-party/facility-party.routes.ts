import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FacilityPartyResolve from './route/facility-party-routing-resolve.service';

const facilityPartyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/facility-party').then(m => m.FacilityParty),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/facility-party-detail').then(m => m.FacilityPartyDetail),
    resolve: {
      facilityParty: FacilityPartyResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/facility-party-update').then(m => m.FacilityPartyUpdate),
    resolve: {
      facilityParty: FacilityPartyResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/facility-party-update').then(m => m.FacilityPartyUpdate),
    resolve: {
      facilityParty: FacilityPartyResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default facilityPartyRoute;
