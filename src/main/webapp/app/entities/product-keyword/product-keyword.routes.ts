import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductKeywordResolve from './route/product-keyword-routing-resolve.service';

const productKeywordRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-keyword').then(m => m.ProductKeyword),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-keyword-detail').then(m => m.ProductKeywordDetail),
    resolve: {
      productKeyword: ProductKeywordResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-keyword-update').then(m => m.ProductKeywordUpdate),
    resolve: {
      productKeyword: ProductKeywordResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-keyword-update').then(m => m.ProductKeywordUpdate),
    resolve: {
      productKeyword: ProductKeywordResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productKeywordRoute;
