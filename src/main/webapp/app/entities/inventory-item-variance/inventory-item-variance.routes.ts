import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InventoryItemVarianceResolve from './route/inventory-item-variance-routing-resolve.service';

const inventoryItemVarianceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/inventory-item-variance').then(m => m.InventoryItemVariance),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/inventory-item-variance-detail').then(m => m.InventoryItemVarianceDetail),
    resolve: {
      inventoryItemVariance: InventoryItemVarianceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/inventory-item-variance-update').then(m => m.InventoryItemVarianceUpdate),
    resolve: {
      inventoryItemVariance: InventoryItemVarianceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/inventory-item-variance-update').then(m => m.InventoryItemVarianceUpdate),
    resolve: {
      inventoryItemVariance: InventoryItemVarianceResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default inventoryItemVarianceRoute;
