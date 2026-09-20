import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FacilityContactMechResolve from './route/facility-contact-mech-routing-resolve.service';

const facilityContactMechRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/facility-contact-mech').then(m => m.FacilityContactMech),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/facility-contact-mech-detail').then(m => m.FacilityContactMechDetail),
    resolve: {
      facilityContactMech: FacilityContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/facility-contact-mech-update').then(m => m.FacilityContactMechUpdate),
    resolve: {
      facilityContactMech: FacilityContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/facility-contact-mech-update').then(m => m.FacilityContactMechUpdate),
    resolve: {
      facilityContactMech: FacilityContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default facilityContactMechRoute;
