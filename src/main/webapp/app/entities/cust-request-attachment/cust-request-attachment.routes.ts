import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CustRequestAttachmentResolve from './route/cust-request-attachment-routing-resolve.service';

const custRequestAttachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cust-request-attachment').then(m => m.CustRequestAttachment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cust-request-attachment-detail').then(m => m.CustRequestAttachmentDetail),
    resolve: {
      custRequestAttachment: CustRequestAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cust-request-attachment-update').then(m => m.CustRequestAttachmentUpdate),
    resolve: {
      custRequestAttachment: CustRequestAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cust-request-attachment-update').then(m => m.CustRequestAttachmentUpdate),
    resolve: {
      custRequestAttachment: CustRequestAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default custRequestAttachmentRoute;
