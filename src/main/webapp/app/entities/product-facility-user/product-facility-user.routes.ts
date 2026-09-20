import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductFacilityUserResolve from './route/product-facility-user-routing-resolve.service';

const productFacilityUserRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-facility-user').then(m => m.ProductFacilityUser),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-facility-user-detail').then(m => m.ProductFacilityUserDetail),
    resolve: {
      productFacilityUser: ProductFacilityUserResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-facility-user-update').then(m => m.ProductFacilityUserUpdate),
    resolve: {
      productFacilityUser: ProductFacilityUserResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-facility-user-update').then(m => m.ProductFacilityUserUpdate),
    resolve: {
      productFacilityUser: ProductFacilityUserResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productFacilityUserRoute;
