import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductCategoryMemberResolve from './route/product-category-member-routing-resolve.service';

const productCategoryMemberRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-category-member').then(m => m.ProductCategoryMember),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-category-member-detail').then(m => m.ProductCategoryMemberDetail),
    resolve: {
      productCategoryMember: ProductCategoryMemberResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-category-member-update').then(m => m.ProductCategoryMemberUpdate),
    resolve: {
      productCategoryMember: ProductCategoryMemberResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-category-member-update').then(m => m.ProductCategoryMemberUpdate),
    resolve: {
      productCategoryMember: ProductCategoryMemberResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productCategoryMemberRoute;
