import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FacilityCarrierShipmentResolve from './route/facility-carrier-shipment-routing-resolve.service';

const facilityCarrierShipmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/facility-carrier-shipment').then(m => m.FacilityCarrierShipment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/facility-carrier-shipment-detail').then(m => m.FacilityCarrierShipmentDetail),
    resolve: {
      facilityCarrierShipment: FacilityCarrierShipmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/facility-carrier-shipment-update').then(m => m.FacilityCarrierShipmentUpdate),
    resolve: {
      facilityCarrierShipment: FacilityCarrierShipmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/facility-carrier-shipment-update').then(m => m.FacilityCarrierShipmentUpdate),
    resolve: {
      facilityCarrierShipment: FacilityCarrierShipmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default facilityCarrierShipmentRoute;
