import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductFacilityResolve from './route/product-facility-routing-resolve.service';

const productFacilityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-facility').then(m => m.ProductFacility),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-facility-detail').then(m => m.ProductFacilityDetail),
    resolve: {
      productFacility: ProductFacilityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-facility-update').then(m => m.ProductFacilityUpdate),
    resolve: {
      productFacility: ProductFacilityResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-facility-update').then(m => m.ProductFacilityUpdate),
    resolve: {
      productFacility: ProductFacilityResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productFacilityRoute;
