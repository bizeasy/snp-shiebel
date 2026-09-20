import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PartyTypeResolve from './route/party-type-routing-resolve.service';

const partyTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/party-type').then(m => m.PartyType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/party-type-detail').then(m => m.PartyTypeDetail),
    resolve: {
      partyType: PartyTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/party-type-update').then(m => m.PartyTypeUpdate),
    resolve: {
      partyType: PartyTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/party-type-update').then(m => m.PartyTypeUpdate),
    resolve: {
      partyType: PartyTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partyTypeRoute;
