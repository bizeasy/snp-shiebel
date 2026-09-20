import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductFeatureResolve from './route/product-feature-routing-resolve.service';

const productFeatureRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-feature').then(m => m.ProductFeature),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-feature-detail').then(m => m.ProductFeatureDetail),
    resolve: {
      productFeature: ProductFeatureResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-feature-update').then(m => m.ProductFeatureUpdate),
    resolve: {
      productFeature: ProductFeatureResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-feature-update').then(m => m.ProductFeatureUpdate),
    resolve: {
      productFeature: ProductFeatureResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productFeatureRoute;
