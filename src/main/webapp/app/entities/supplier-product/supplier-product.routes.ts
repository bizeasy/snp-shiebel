import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import SupplierProductResolve from './route/supplier-product-routing-resolve.service';

const supplierProductRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/supplier-product').then(m => m.SupplierProduct),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/supplier-product-detail').then(m => m.SupplierProductDetail),
    resolve: {
      supplierProduct: SupplierProductResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/supplier-product-update').then(m => m.SupplierProductUpdate),
    resolve: {
      supplierProduct: SupplierProductResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/supplier-product-update').then(m => m.SupplierProductUpdate),
    resolve: {
      supplierProduct: SupplierProductResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default supplierProductRoute;
