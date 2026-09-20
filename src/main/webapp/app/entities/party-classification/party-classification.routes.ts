import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PartyClassificationResolve from './route/party-classification-routing-resolve.service';

const partyClassificationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/party-classification').then(m => m.PartyClassification),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/party-classification-detail').then(m => m.PartyClassificationDetail),
    resolve: {
      partyClassification: PartyClassificationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/party-classification-update').then(m => m.PartyClassificationUpdate),
    resolve: {
      partyClassification: PartyClassificationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/party-classification-update').then(m => m.PartyClassificationUpdate),
    resolve: {
      partyClassification: PartyClassificationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partyClassificationRoute;
