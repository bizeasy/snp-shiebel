import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductFeatureCategoryApplResolve from './route/product-feature-category-appl-routing-resolve.service';

const productFeatureCategoryApplRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-feature-category-appl').then(m => m.ProductFeatureCategoryAppl),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-feature-category-appl-detail').then(m => m.ProductFeatureCategoryApplDetail),
    resolve: {
      productFeatureCategoryAppl: ProductFeatureCategoryApplResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-feature-category-appl-update').then(m => m.ProductFeatureCategoryApplUpdate),
    resolve: {
      productFeatureCategoryAppl: ProductFeatureCategoryApplResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-feature-category-appl-update').then(m => m.ProductFeatureCategoryApplUpdate),
    resolve: {
      productFeatureCategoryAppl: ProductFeatureCategoryApplResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productFeatureCategoryApplRoute;
