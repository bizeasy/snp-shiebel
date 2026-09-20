import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductCategoryTypeResolve from './route/product-category-type-routing-resolve.service';

const productCategoryTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-category-type').then(m => m.ProductCategoryType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-category-type-detail').then(m => m.ProductCategoryTypeDetail),
    resolve: {
      productCategoryType: ProductCategoryTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-category-type-update').then(m => m.ProductCategoryTypeUpdate),
    resolve: {
      productCategoryType: ProductCategoryTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-category-type-update').then(m => m.ProductCategoryTypeUpdate),
    resolve: {
      productCategoryType: ProductCategoryTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productCategoryTypeRoute;
