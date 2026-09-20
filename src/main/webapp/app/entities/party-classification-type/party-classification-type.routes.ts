import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PartyClassificationTypeResolve from './route/party-classification-type-routing-resolve.service';

const partyClassificationTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/party-classification-type').then(m => m.PartyClassificationType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/party-classification-type-detail').then(m => m.PartyClassificationTypeDetail),
    resolve: {
      partyClassificationType: PartyClassificationTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/party-classification-type-update').then(m => m.PartyClassificationTypeUpdate),
    resolve: {
      partyClassificationType: PartyClassificationTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/party-classification-type-update').then(m => m.PartyClassificationTypeUpdate),
    resolve: {
      partyClassificationType: PartyClassificationTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partyClassificationTypeRoute;
