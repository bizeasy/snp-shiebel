import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PartyContactMechResolve from './route/party-contact-mech-routing-resolve.service';

const partyContactMechRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/party-contact-mech').then(m => m.PartyContactMech),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/party-contact-mech-detail').then(m => m.PartyContactMechDetail),
    resolve: {
      partyContactMech: PartyContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/party-contact-mech-update').then(m => m.PartyContactMechUpdate),
    resolve: {
      partyContactMech: PartyContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/party-contact-mech-update').then(m => m.PartyContactMechUpdate),
    resolve: {
      partyContactMech: PartyContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partyContactMechRoute;
