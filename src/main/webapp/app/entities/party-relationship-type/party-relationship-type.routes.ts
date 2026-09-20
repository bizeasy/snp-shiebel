import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PartyRelationshipTypeResolve from './route/party-relationship-type-routing-resolve.service';

const partyRelationshipTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/party-relationship-type').then(m => m.PartyRelationshipType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/party-relationship-type-detail').then(m => m.PartyRelationshipTypeDetail),
    resolve: {
      partyRelationshipType: PartyRelationshipTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/party-relationship-type-update').then(m => m.PartyRelationshipTypeUpdate),
    resolve: {
      partyRelationshipType: PartyRelationshipTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/party-relationship-type-update').then(m => m.PartyRelationshipTypeUpdate),
    resolve: {
      partyRelationshipType: PartyRelationshipTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partyRelationshipTypeRoute;
