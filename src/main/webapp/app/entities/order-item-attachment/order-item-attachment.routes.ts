import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrderItemAttachmentResolve from './route/order-item-attachment-routing-resolve.service';

const orderItemAttachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-item-attachment').then(m => m.OrderItemAttachment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-item-attachment-detail').then(m => m.OrderItemAttachmentDetail),
    resolve: {
      orderItemAttachment: OrderItemAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-item-attachment-update').then(m => m.OrderItemAttachmentUpdate),
    resolve: {
      orderItemAttachment: OrderItemAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-item-attachment-update').then(m => m.OrderItemAttachmentUpdate),
    resolve: {
      orderItemAttachment: OrderItemAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default orderItemAttachmentRoute;
