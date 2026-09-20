import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PartyIdentificationTypeResolve from './route/party-identification-type-routing-resolve.service';

const partyIdentificationTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/party-identification-type').then(m => m.PartyIdentificationType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/party-identification-type-detail').then(m => m.PartyIdentificationTypeDetail),
    resolve: {
      partyIdentificationType: PartyIdentificationTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/party-identification-type-update').then(m => m.PartyIdentificationTypeUpdate),
    resolve: {
      partyIdentificationType: PartyIdentificationTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/party-identification-type-update').then(m => m.PartyIdentificationTypeUpdate),
    resolve: {
      partyIdentificationType: PartyIdentificationTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partyIdentificationTypeRoute;
