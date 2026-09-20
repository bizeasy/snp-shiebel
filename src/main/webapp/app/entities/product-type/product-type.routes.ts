import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductTypeResolve from './route/product-type-routing-resolve.service';

const productTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-type').then(m => m.ProductType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-type-detail').then(m => m.ProductTypeDetail),
    resolve: {
      productType: ProductTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-type-update').then(m => m.ProductTypeUpdate),
    resolve: {
      productType: ProductTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-type-update').then(m => m.ProductTypeUpdate),
    resolve: {
      productType: ProductTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productTypeRoute;
