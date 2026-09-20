import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CustRequestItemAttachmentResolve from './route/cust-request-item-attachment-routing-resolve.service';

const custRequestItemAttachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cust-request-item-attachment').then(m => m.CustRequestItemAttachment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cust-request-item-attachment-detail').then(m => m.CustRequestItemAttachmentDetail),
    resolve: {
      custRequestItemAttachment: CustRequestItemAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cust-request-item-attachment-update').then(m => m.CustRequestItemAttachmentUpdate),
    resolve: {
      custRequestItemAttachment: CustRequestItemAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cust-request-item-attachment-update').then(m => m.CustRequestItemAttachmentUpdate),
    resolve: {
      custRequestItemAttachment: CustRequestItemAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default custRequestItemAttachmentRoute;
