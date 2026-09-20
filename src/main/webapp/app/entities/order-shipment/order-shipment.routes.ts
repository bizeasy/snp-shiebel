import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrderShipmentResolve from './route/order-shipment-routing-resolve.service';

const orderShipmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-shipment').then(m => m.OrderShipment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-shipment-detail').then(m => m.OrderShipmentDetail),
    resolve: {
      orderShipment: OrderShipmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-shipment-update').then(m => m.OrderShipmentUpdate),
    resolve: {
      orderShipment: OrderShipmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-shipment-update').then(m => m.OrderShipmentUpdate),
    resolve: {
      orderShipment: OrderShipmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default orderShipmentRoute;
