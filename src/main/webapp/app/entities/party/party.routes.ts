import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PartyResolve from './route/party-routing-resolve.service';

const partyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/party').then(m => m.Party),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/party-detail').then(m => m.PartyDetail),
    resolve: {
      party: PartyResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/party-update').then(m => m.PartyUpdate),
    resolve: {
      party: PartyResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/party-update').then(m => m.PartyUpdate),
    resolve: {
      party: PartyResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partyRoute;
