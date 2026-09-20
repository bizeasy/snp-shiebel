import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductPriceResolve from './route/product-price-routing-resolve.service';

const productPriceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-price').then(m => m.ProductPrice),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-price-detail').then(m => m.ProductPriceDetail),
    resolve: {
      productPrice: ProductPriceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-price-update').then(m => m.ProductPriceUpdate),
    resolve: {
      productPrice: ProductPriceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-price-update').then(m => m.ProductPriceUpdate),
    resolve: {
      productPrice: ProductPriceResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productPriceRoute;
