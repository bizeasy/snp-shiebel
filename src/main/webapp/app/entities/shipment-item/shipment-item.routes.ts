import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ShipmentItemResolve from './route/shipment-item-routing-resolve.service';

const shipmentItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/shipment-item').then(m => m.ShipmentItem),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/shipment-item-detail').then(m => m.ShipmentItemDetail),
    resolve: {
      shipmentItem: ShipmentItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/shipment-item-update').then(m => m.ShipmentItemUpdate),
    resolve: {
      shipmentItem: ShipmentItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/shipment-item-update').then(m => m.ShipmentItemUpdate),
    resolve: {
      shipmentItem: ShipmentItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default shipmentItemRoute;
