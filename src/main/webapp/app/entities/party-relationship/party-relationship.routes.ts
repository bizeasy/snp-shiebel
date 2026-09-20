import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PartyRelationshipResolve from './route/party-relationship-routing-resolve.service';

const partyRelationshipRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/party-relationship').then(m => m.PartyRelationship),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/party-relationship-detail').then(m => m.PartyRelationshipDetail),
    resolve: {
      partyRelationship: PartyRelationshipResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/party-relationship-update').then(m => m.PartyRelationshipUpdate),
    resolve: {
      partyRelationship: PartyRelationshipResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/party-relationship-update').then(m => m.PartyRelationshipUpdate),
    resolve: {
      partyRelationship: PartyRelationshipResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partyRelationshipRoute;
