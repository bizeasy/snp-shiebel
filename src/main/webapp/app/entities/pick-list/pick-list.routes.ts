import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PickListResolve from './route/pick-list-routing-resolve.service';

const pickListRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pick-list').then(m => m.PickList),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pick-list-detail').then(m => m.PickListDetail),
    resolve: {
      pickList: PickListResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pick-list-update').then(m => m.PickListUpdate),
    resolve: {
      pickList: PickListResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pick-list-update').then(m => m.PickListUpdate),
    resolve: {
      pickList: PickListResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default pickListRoute;
