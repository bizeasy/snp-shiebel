import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ItemIssuanceResolve from './route/item-issuance-routing-resolve.service';

const itemIssuanceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/item-issuance').then(m => m.ItemIssuance),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/item-issuance-detail').then(m => m.ItemIssuanceDetail),
    resolve: {
      itemIssuance: ItemIssuanceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/item-issuance-update').then(m => m.ItemIssuanceUpdate),
    resolve: {
      itemIssuance: ItemIssuanceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/item-issuance-update').then(m => m.ItemIssuanceUpdate),
    resolve: {
      itemIssuance: ItemIssuanceResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default itemIssuanceRoute;
