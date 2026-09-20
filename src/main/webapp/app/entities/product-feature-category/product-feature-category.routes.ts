import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductFeatureCategoryResolve from './route/product-feature-category-routing-resolve.service';

const productFeatureCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-feature-category').then(m => m.ProductFeatureCategory),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-feature-category-detail').then(m => m.ProductFeatureCategoryDetail),
    resolve: {
      productFeatureCategory: ProductFeatureCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-feature-category-update').then(m => m.ProductFeatureCategoryUpdate),
    resolve: {
      productFeatureCategory: ProductFeatureCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-feature-category-update').then(m => m.ProductFeatureCategoryUpdate),
    resolve: {
      productFeatureCategory: ProductFeatureCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productFeatureCategoryRoute;
