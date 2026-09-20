import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductRoleResolve from './route/product-role-routing-resolve.service';

const productRoleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-role').then(m => m.ProductRole),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-role-detail').then(m => m.ProductRoleDetail),
    resolve: {
      productRole: ProductRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-role-update').then(m => m.ProductRoleUpdate),
    resolve: {
      productRole: ProductRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-role-update').then(m => m.ProductRoleUpdate),
    resolve: {
      productRole: ProductRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productRoleRoute;
