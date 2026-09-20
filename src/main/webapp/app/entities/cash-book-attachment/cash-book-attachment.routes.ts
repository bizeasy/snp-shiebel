import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CashBookAttachmentResolve from './route/cash-book-attachment-routing-resolve.service';

const cashBookAttachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cash-book-attachment').then(m => m.CashBookAttachment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cash-book-attachment-detail').then(m => m.CashBookAttachmentDetail),
    resolve: {
      cashBookAttachment: CashBookAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cash-book-attachment-update').then(m => m.CashBookAttachmentUpdate),
    resolve: {
      cashBookAttachment: CashBookAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cash-book-attachment-update').then(m => m.CashBookAttachmentUpdate),
    resolve: {
      cashBookAttachment: CashBookAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default cashBookAttachmentRoute;
