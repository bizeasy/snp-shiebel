import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ShipmentStatusResolve from './route/shipment-status-routing-resolve.service';

const shipmentStatusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/shipment-status').then(m => m.ShipmentStatus),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/shipment-status-detail').then(m => m.ShipmentStatusDetail),
    resolve: {
      shipmentStatus: ShipmentStatusResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/shipment-status-update').then(m => m.ShipmentStatusUpdate),
    resolve: {
      shipmentStatus: ShipmentStatusResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/shipment-status-update').then(m => m.ShipmentStatusUpdate),
    resolve: {
      shipmentStatus: ShipmentStatusResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default shipmentStatusRoute;
