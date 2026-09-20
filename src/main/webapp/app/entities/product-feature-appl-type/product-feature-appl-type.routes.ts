import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductFeatureApplTypeResolve from './route/product-feature-appl-type-routing-resolve.service';

const productFeatureApplTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-feature-appl-type').then(m => m.ProductFeatureApplType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-feature-appl-type-detail').then(m => m.ProductFeatureApplTypeDetail),
    resolve: {
      productFeatureApplType: ProductFeatureApplTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-feature-appl-type-update').then(m => m.ProductFeatureApplTypeUpdate),
    resolve: {
      productFeatureApplType: ProductFeatureApplTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-feature-appl-type-update').then(m => m.ProductFeatureApplTypeUpdate),
    resolve: {
      productFeatureApplType: ProductFeatureApplTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productFeatureApplTypeRoute;
