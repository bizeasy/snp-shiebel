import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import GeoAssocResolve from './route/geo-assoc-routing-resolve.service';

const geoAssocRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/geo-assoc').then(m => m.GeoAssoc),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/geo-assoc-detail').then(m => m.GeoAssocDetail),
    resolve: {
      geoAssoc: GeoAssocResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/geo-assoc-update').then(m => m.GeoAssocUpdate),
    resolve: {
      geoAssoc: GeoAssocResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/geo-assoc-update').then(m => m.GeoAssocUpdate),
    resolve: {
      geoAssoc: GeoAssocResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default geoAssocRoute;
