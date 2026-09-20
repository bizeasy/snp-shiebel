import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import LocationTypeResolve from './route/location-type-routing-resolve.service';

const locationTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/location-type').then(m => m.LocationType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/location-type-detail').then(m => m.LocationTypeDetail),
    resolve: {
      locationType: LocationTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/location-type-update').then(m => m.LocationTypeUpdate),
    resolve: {
      locationType: LocationTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/location-type-update').then(m => m.LocationTypeUpdate),
    resolve: {
      locationType: LocationTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default locationTypeRoute;
