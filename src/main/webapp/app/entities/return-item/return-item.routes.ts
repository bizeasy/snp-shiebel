import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ReturnItemResolve from './route/return-item-routing-resolve.service';

const returnItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/return-item').then(m => m.ReturnItem),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/return-item-detail').then(m => m.ReturnItemDetail),
    resolve: {
      returnItem: ReturnItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/return-item-update').then(m => m.ReturnItemUpdate),
    resolve: {
      returnItem: ReturnItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/return-item-update').then(m => m.ReturnItemUpdate),
    resolve: {
      returnItem: ReturnItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default returnItemRoute;
