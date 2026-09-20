import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InventoryItemResolve from './route/inventory-item-routing-resolve.service';

const inventoryItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/inventory-item').then(m => m.InventoryItem),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/inventory-item-detail').then(m => m.InventoryItemDetail),
    resolve: {
      inventoryItem: InventoryItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/inventory-item-update').then(m => m.InventoryItemUpdate),
    resolve: {
      inventoryItem: InventoryItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/inventory-item-update').then(m => m.InventoryItemUpdate),
    resolve: {
      inventoryItem: InventoryItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default inventoryItemRoute;
