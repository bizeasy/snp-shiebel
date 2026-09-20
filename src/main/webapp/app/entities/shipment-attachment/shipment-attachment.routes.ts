import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ShipmentAttachmentResolve from './route/shipment-attachment-routing-resolve.service';

const shipmentAttachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/shipment-attachment').then(m => m.ShipmentAttachment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/shipment-attachment-detail').then(m => m.ShipmentAttachmentDetail),
    resolve: {
      shipmentAttachment: ShipmentAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/shipment-attachment-update').then(m => m.ShipmentAttachmentUpdate),
    resolve: {
      shipmentAttachment: ShipmentAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/shipment-attachment-update').then(m => m.ShipmentAttachmentUpdate),
    resolve: {
      shipmentAttachment: ShipmentAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default shipmentAttachmentRoute;
