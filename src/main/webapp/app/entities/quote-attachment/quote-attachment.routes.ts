import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import QuoteAttachmentResolve from './route/quote-attachment-routing-resolve.service';

const quoteAttachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/quote-attachment').then(m => m.QuoteAttachment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/quote-attachment-detail').then(m => m.QuoteAttachmentDetail),
    resolve: {
      quoteAttachment: QuoteAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/quote-attachment-update').then(m => m.QuoteAttachmentUpdate),
    resolve: {
      quoteAttachment: QuoteAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/quote-attachment-update').then(m => m.QuoteAttachmentUpdate),
    resolve: {
      quoteAttachment: QuoteAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default quoteAttachmentRoute;
