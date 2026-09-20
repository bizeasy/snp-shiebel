import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductStoreResolve from './route/product-store-routing-resolve.service';

const productStoreRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-store').then(m => m.ProductStore),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-store-detail').then(m => m.ProductStoreDetail),
    resolve: {
      productStore: ProductStoreResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-store-update').then(m => m.ProductStoreUpdate),
    resolve: {
      productStore: ProductStoreResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-store-update').then(m => m.ProductStoreUpdate),
    resolve: {
      productStore: ProductStoreResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productStoreRoute;
