import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ShipmentMethodTypeResolve from './route/shipment-method-type-routing-resolve.service';

const shipmentMethodTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/shipment-method-type').then(m => m.ShipmentMethodType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/shipment-method-type-detail').then(m => m.ShipmentMethodTypeDetail),
    resolve: {
      shipmentMethodType: ShipmentMethodTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/shipment-method-type-update').then(m => m.ShipmentMethodTypeUpdate),
    resolve: {
      shipmentMethodType: ShipmentMethodTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/shipment-method-type-update').then(m => m.ShipmentMethodTypeUpdate),
    resolve: {
      shipmentMethodType: ShipmentMethodTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default shipmentMethodTypeRoute;
