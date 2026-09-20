import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ShipmentTypeResolve from './route/shipment-type-routing-resolve.service';

const shipmentTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/shipment-type').then(m => m.ShipmentType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/shipment-type-detail').then(m => m.ShipmentTypeDetail),
    resolve: {
      shipmentType: ShipmentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/shipment-type-update').then(m => m.ShipmentTypeUpdate),
    resolve: {
      shipmentType: ShipmentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/shipment-type-update').then(m => m.ShipmentTypeUpdate),
    resolve: {
      shipmentType: ShipmentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default shipmentTypeRoute;
