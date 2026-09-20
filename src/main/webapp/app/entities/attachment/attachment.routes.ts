import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import AttachmentResolve from './route/attachment-routing-resolve.service';

const attachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/attachment').then(m => m.Attachment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/attachment-detail').then(m => m.AttachmentDetail),
    resolve: {
      attachment: AttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/attachment-update').then(m => m.AttachmentUpdate),
    resolve: {
      attachment: AttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/attachment-update').then(m => m.AttachmentUpdate),
    resolve: {
      attachment: AttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default attachmentRoute;
