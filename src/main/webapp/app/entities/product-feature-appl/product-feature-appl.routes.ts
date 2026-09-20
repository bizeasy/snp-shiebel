import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductFeatureApplResolve from './route/product-feature-appl-routing-resolve.service';

const productFeatureApplRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-feature-appl').then(m => m.ProductFeatureAppl),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-feature-appl-detail').then(m => m.ProductFeatureApplDetail),
    resolve: {
      productFeatureAppl: ProductFeatureApplResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-feature-appl-update').then(m => m.ProductFeatureApplUpdate),
    resolve: {
      productFeatureAppl: ProductFeatureApplResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-feature-appl-update').then(m => m.ProductFeatureApplUpdate),
    resolve: {
      productFeatureAppl: ProductFeatureApplResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productFeatureApplRoute;
