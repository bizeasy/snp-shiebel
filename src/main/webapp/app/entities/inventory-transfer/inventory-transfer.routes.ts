import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InventoryTransferResolve from './route/inventory-transfer-routing-resolve.service';

const inventoryTransferRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/inventory-transfer').then(m => m.InventoryTransfer),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/inventory-transfer-detail').then(m => m.InventoryTransferDetail),
    resolve: {
      inventoryTransfer: InventoryTransferResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/inventory-transfer-update').then(m => m.InventoryTransferUpdate),
    resolve: {
      inventoryTransfer: InventoryTransferResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/inventory-transfer-update').then(m => m.InventoryTransferUpdate),
    resolve: {
      inventoryTransfer: InventoryTransferResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default inventoryTransferRoute;
