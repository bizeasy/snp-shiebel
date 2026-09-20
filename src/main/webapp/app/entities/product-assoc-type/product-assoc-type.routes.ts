import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductAssocTypeResolve from './route/product-assoc-type-routing-resolve.service';

const productAssocTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-assoc-type').then(m => m.ProductAssocType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-assoc-type-detail').then(m => m.ProductAssocTypeDetail),
    resolve: {
      productAssocType: ProductAssocTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-assoc-type-update').then(m => m.ProductAssocTypeUpdate),
    resolve: {
      productAssocType: ProductAssocTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-assoc-type-update').then(m => m.ProductAssocTypeUpdate),
    resolve: {
      productAssocType: ProductAssocTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productAssocTypeRoute;
