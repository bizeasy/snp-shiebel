import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductFacilityLocationResolve from './route/product-facility-location-routing-resolve.service';

const productFacilityLocationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-facility-location').then(m => m.ProductFacilityLocation),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-facility-location-detail').then(m => m.ProductFacilityLocationDetail),
    resolve: {
      productFacilityLocation: ProductFacilityLocationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-facility-location-update').then(m => m.ProductFacilityLocationUpdate),
    resolve: {
      productFacilityLocation: ProductFacilityLocationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-facility-location-update').then(m => m.ProductFacilityLocationUpdate),
    resolve: {
      productFacilityLocation: ProductFacilityLocationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productFacilityLocationRoute;
