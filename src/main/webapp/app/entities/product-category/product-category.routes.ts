import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductCategoryResolve from './route/product-category-routing-resolve.service';

const productCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-category').then(m => m.ProductCategory),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-category-detail').then(m => m.ProductCategoryDetail),
    resolve: {
      productCategory: ProductCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-category-update').then(m => m.ProductCategoryUpdate),
    resolve: {
      productCategory: ProductCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-category-update').then(m => m.ProductCategoryUpdate),
    resolve: {
      productCategory: ProductCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productCategoryRoute;
