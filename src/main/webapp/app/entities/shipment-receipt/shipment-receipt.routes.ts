import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ShipmentReceiptResolve from './route/shipment-receipt-routing-resolve.service';

const shipmentReceiptRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/shipment-receipt').then(m => m.ShipmentReceipt),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/shipment-receipt-detail').then(m => m.ShipmentReceiptDetail),
    resolve: {
      shipmentReceipt: ShipmentReceiptResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/shipment-receipt-update').then(m => m.ShipmentReceiptUpdate),
    resolve: {
      shipmentReceipt: ShipmentReceiptResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/shipment-receipt-update').then(m => m.ShipmentReceiptUpdate),
    resolve: {
      shipmentReceipt: ShipmentReceiptResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default shipmentReceiptRoute;
