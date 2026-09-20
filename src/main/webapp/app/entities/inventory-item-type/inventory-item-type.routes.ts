import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InventoryItemTypeResolve from './route/inventory-item-type-routing-resolve.service';

const inventoryItemTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/inventory-item-type').then(m => m.InventoryItemType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/inventory-item-type-detail').then(m => m.InventoryItemTypeDetail),
    resolve: {
      inventoryItemType: InventoryItemTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/inventory-item-type-update').then(m => m.InventoryItemTypeUpdate),
    resolve: {
      inventoryItemType: InventoryItemTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/inventory-item-type-update').then(m => m.InventoryItemTypeUpdate),
    resolve: {
      inventoryItemType: InventoryItemTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default inventoryItemTypeRoute;
