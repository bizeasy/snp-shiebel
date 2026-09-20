import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductPricePurposeResolve from './route/product-price-purpose-routing-resolve.service';

const productPricePurposeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-price-purpose').then(m => m.ProductPricePurpose),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-price-purpose-detail').then(m => m.ProductPricePurposeDetail),
    resolve: {
      productPricePurpose: ProductPricePurposeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-price-purpose-update').then(m => m.ProductPricePurposeUpdate),
    resolve: {
      productPricePurpose: ProductPricePurposeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-price-purpose-update').then(m => m.ProductPricePurposeUpdate),
    resolve: {
      productPricePurpose: ProductPricePurposeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productPricePurposeRoute;
