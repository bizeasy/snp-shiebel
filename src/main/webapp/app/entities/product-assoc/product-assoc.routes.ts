import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductAssocResolve from './route/product-assoc-routing-resolve.service';

const productAssocRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-assoc').then(m => m.ProductAssoc),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-assoc-detail').then(m => m.ProductAssocDetail),
    resolve: {
      productAssoc: ProductAssocResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-assoc-update').then(m => m.ProductAssocUpdate),
    resolve: {
      productAssoc: ProductAssocResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-assoc-update').then(m => m.ProductAssocUpdate),
    resolve: {
      productAssoc: ProductAssocResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productAssocRoute;
