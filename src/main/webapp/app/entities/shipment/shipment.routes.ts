import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ShipmentResolve from './route/shipment-routing-resolve.service';

const shipmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/shipment').then(m => m.Shipment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/shipment-detail').then(m => m.ShipmentDetail),
    resolve: {
      shipment: ShipmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/shipment-update').then(m => m.ShipmentUpdate),
    resolve: {
      shipment: ShipmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/shipment-update').then(m => m.ShipmentUpdate),
    resolve: {
      shipment: ShipmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default shipmentRoute;
