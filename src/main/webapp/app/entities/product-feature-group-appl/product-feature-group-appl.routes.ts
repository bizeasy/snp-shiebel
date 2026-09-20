import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductFeatureGroupApplResolve from './route/product-feature-group-appl-routing-resolve.service';

const productFeatureGroupApplRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-feature-group-appl').then(m => m.ProductFeatureGroupAppl),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-feature-group-appl-detail').then(m => m.ProductFeatureGroupApplDetail),
    resolve: {
      productFeatureGroupAppl: ProductFeatureGroupApplResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-feature-group-appl-update').then(m => m.ProductFeatureGroupApplUpdate),
    resolve: {
      productFeatureGroupAppl: ProductFeatureGroupApplResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-feature-group-appl-update').then(m => m.ProductFeatureGroupApplUpdate),
    resolve: {
      productFeatureGroupAppl: ProductFeatureGroupApplResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productFeatureGroupApplRoute;
