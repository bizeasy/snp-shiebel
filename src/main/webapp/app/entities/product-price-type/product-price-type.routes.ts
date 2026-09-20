import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductPriceTypeResolve from './route/product-price-type-routing-resolve.service';

const productPriceTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-price-type').then(m => m.ProductPriceType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-price-type-detail').then(m => m.ProductPriceTypeDetail),
    resolve: {
      productPriceType: ProductPriceTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-price-type-update').then(m => m.ProductPriceTypeUpdate),
    resolve: {
      productPriceType: ProductPriceTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-price-type-update').then(m => m.ProductPriceTypeUpdate),
    resolve: {
      productPriceType: ProductPriceTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productPriceTypeRoute;
