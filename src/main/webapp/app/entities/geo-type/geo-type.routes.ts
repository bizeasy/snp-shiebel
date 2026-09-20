import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import GeoTypeResolve from './route/geo-type-routing-resolve.service';

const geoTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/geo-type').then(m => m.GeoType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/geo-type-detail').then(m => m.GeoTypeDetail),
    resolve: {
      geoType: GeoTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/geo-type-update').then(m => m.GeoTypeUpdate),
    resolve: {
      geoType: GeoTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/geo-type-update').then(m => m.GeoTypeUpdate),
    resolve: {
      geoType: GeoTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default geoTypeRoute;
