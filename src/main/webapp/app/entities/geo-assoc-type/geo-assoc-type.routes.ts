import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import GeoAssocTypeResolve from './route/geo-assoc-type-routing-resolve.service';

const geoAssocTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/geo-assoc-type').then(m => m.GeoAssocType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/geo-assoc-type-detail').then(m => m.GeoAssocTypeDetail),
    resolve: {
      geoAssocType: GeoAssocTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/geo-assoc-type-update').then(m => m.GeoAssocTypeUpdate),
    resolve: {
      geoAssocType: GeoAssocTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/geo-assoc-type-update').then(m => m.GeoAssocTypeUpdate),
    resolve: {
      geoAssocType: GeoAssocTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default geoAssocTypeRoute;
