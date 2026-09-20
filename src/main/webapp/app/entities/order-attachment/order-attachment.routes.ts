import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrderAttachmentResolve from './route/order-attachment-routing-resolve.service';

const orderAttachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-attachment').then(m => m.OrderAttachment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-attachment-detail').then(m => m.OrderAttachmentDetail),
    resolve: {
      orderAttachment: OrderAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-attachment-update').then(m => m.OrderAttachmentUpdate),
    resolve: {
      orderAttachment: OrderAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-attachment-update').then(m => m.OrderAttachmentUpdate),
    resolve: {
      orderAttachment: OrderAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default orderAttachmentRoute;
