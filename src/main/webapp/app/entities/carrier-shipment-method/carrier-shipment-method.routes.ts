import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CarrierShipmentMethodResolve from './route/carrier-shipment-method-routing-resolve.service';

const carrierShipmentMethodRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/carrier-shipment-method').then(m => m.CarrierShipmentMethod),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/carrier-shipment-method-detail').then(m => m.CarrierShipmentMethodDetail),
    resolve: {
      carrierShipmentMethod: CarrierShipmentMethodResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/carrier-shipment-method-update').then(m => m.CarrierShipmentMethodUpdate),
    resolve: {
      carrierShipmentMethod: CarrierShipmentMethodResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/carrier-shipment-method-update').then(m => m.CarrierShipmentMethodUpdate),
    resolve: {
      carrierShipmentMethod: CarrierShipmentMethodResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default carrierShipmentMethodRoute;
