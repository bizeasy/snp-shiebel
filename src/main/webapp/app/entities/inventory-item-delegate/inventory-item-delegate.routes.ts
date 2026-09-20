import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InventoryItemDelegateResolve from './route/inventory-item-delegate-routing-resolve.service';

const inventoryItemDelegateRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/inventory-item-delegate').then(m => m.InventoryItemDelegate),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/inventory-item-delegate-detail').then(m => m.InventoryItemDelegateDetail),
    resolve: {
      inventoryItemDelegate: InventoryItemDelegateResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/inventory-item-delegate-update').then(m => m.InventoryItemDelegateUpdate),
    resolve: {
      inventoryItemDelegate: InventoryItemDelegateResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/inventory-item-delegate-update').then(m => m.InventoryItemDelegateUpdate),
    resolve: {
      inventoryItemDelegate: InventoryItemDelegateResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default inventoryItemDelegateRoute;
