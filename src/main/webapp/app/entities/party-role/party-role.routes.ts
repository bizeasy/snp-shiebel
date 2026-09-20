import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PartyRoleResolve from './route/party-role-routing-resolve.service';

const partyRoleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/party-role').then(m => m.PartyRole),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/party-role-detail').then(m => m.PartyRoleDetail),
    resolve: {
      partyRole: PartyRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/party-role-update').then(m => m.PartyRoleUpdate),
    resolve: {
      partyRole: PartyRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/party-role-update').then(m => m.PartyRoleUpdate),
    resolve: {
      partyRole: PartyRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partyRoleRoute;
