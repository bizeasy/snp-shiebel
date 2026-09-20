import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import GeoPointResolve from './route/geo-point-routing-resolve.service';

const geoPointRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/geo-point').then(m => m.GeoPoint),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/geo-point-detail').then(m => m.GeoPointDetail),
    resolve: {
      geoPoint: GeoPointResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/geo-point-update').then(m => m.GeoPointUpdate),
    resolve: {
      geoPoint: GeoPointResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/geo-point-update').then(m => m.GeoPointUpdate),
    resolve: {
      geoPoint: GeoPointResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default geoPointRoute;
