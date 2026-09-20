import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PickListItemResolve from './route/pick-list-item-routing-resolve.service';

const pickListItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pick-list-item').then(m => m.PickListItem),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pick-list-item-detail').then(m => m.PickListItemDetail),
    resolve: {
      pickListItem: PickListItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pick-list-item-update').then(m => m.PickListItemUpdate),
    resolve: {
      pickListItem: PickListItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pick-list-item-update').then(m => m.PickListItemUpdate),
    resolve: {
      pickListItem: PickListItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default pickListItemRoute;
