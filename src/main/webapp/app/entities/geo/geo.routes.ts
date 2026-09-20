import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import GeoResolve from './route/geo-routing-resolve.service';

const geoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/geo').then(m => m.Geo),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/geo-detail').then(m => m.GeoDetail),
    resolve: {
      geo: GeoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/geo-update').then(m => m.GeoUpdate),
    resolve: {
      geo: GeoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/geo-update').then(m => m.GeoUpdate),
    resolve: {
      geo: GeoResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default geoRoute;
