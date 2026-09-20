import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductFeatureGroupResolve from './route/product-feature-group-routing-resolve.service';

const productFeatureGroupRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-feature-group').then(m => m.ProductFeatureGroup),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-feature-group-detail').then(m => m.ProductFeatureGroupDetail),
    resolve: {
      productFeatureGroup: ProductFeatureGroupResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-feature-group-update').then(m => m.ProductFeatureGroupUpdate),
    resolve: {
      productFeatureGroup: ProductFeatureGroupResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-feature-group-update').then(m => m.ProductFeatureGroupUpdate),
    resolve: {
      productFeatureGroup: ProductFeatureGroupResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productFeatureGroupRoute;
