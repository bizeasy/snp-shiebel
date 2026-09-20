import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import SalesChannelResolve from './route/sales-channel-routing-resolve.service';

const salesChannelRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/sales-channel').then(m => m.SalesChannel),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/sales-channel-detail').then(m => m.SalesChannelDetail),
    resolve: {
      salesChannel: SalesChannelResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/sales-channel-update').then(m => m.SalesChannelUpdate),
    resolve: {
      salesChannel: SalesChannelResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/sales-channel-update').then(m => m.SalesChannelUpdate),
    resolve: {
      salesChannel: SalesChannelResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default salesChannelRoute;
