import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductFeatureTypeResolve from './route/product-feature-type-routing-resolve.service';

const productFeatureTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-feature-type').then(m => m.ProductFeatureType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-feature-type-detail').then(m => m.ProductFeatureTypeDetail),
    resolve: {
      productFeatureType: ProductFeatureTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-feature-type-update').then(m => m.ProductFeatureTypeUpdate),
    resolve: {
      productFeatureType: ProductFeatureTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-feature-type-update').then(m => m.ProductFeatureTypeUpdate),
    resolve: {
      productFeatureType: ProductFeatureTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productFeatureTypeRoute;
