import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PartyIdentificationResolve from './route/party-identification-routing-resolve.service';

const partyIdentificationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/party-identification').then(m => m.PartyIdentification),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/party-identification-detail').then(m => m.PartyIdentificationDetail),
    resolve: {
      partyIdentification: PartyIdentificationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/party-identification-update').then(m => m.PartyIdentificationUpdate),
    resolve: {
      partyIdentification: PartyIdentificationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/party-identification-update').then(m => m.PartyIdentificationUpdate),
    resolve: {
      partyIdentification: PartyIdentificationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partyIdentificationRoute;
