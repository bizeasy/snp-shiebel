import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PartyClassificationGroupResolve from './route/party-classification-group-routing-resolve.service';

const partyClassificationGroupRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/party-classification-group').then(m => m.PartyClassificationGroup),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/party-classification-group-detail').then(m => m.PartyClassificationGroupDetail),
    resolve: {
      partyClassificationGroup: PartyClassificationGroupResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/party-classification-group-update').then(m => m.PartyClassificationGroupUpdate),
    resolve: {
      partyClassificationGroup: PartyClassificationGroupResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/party-classification-group-update').then(m => m.PartyClassificationGroupUpdate),
    resolve: {
      partyClassificationGroup: PartyClassificationGroupResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partyClassificationGroupRoute;
