import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import QuoteAdjustmentResolve from './route/quote-adjustment-routing-resolve.service';

const quoteAdjustmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/quote-adjustment').then(m => m.QuoteAdjustment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/quote-adjustment-detail').then(m => m.QuoteAdjustmentDetail),
    resolve: {
      quoteAdjustment: QuoteAdjustmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/quote-adjustment-update').then(m => m.QuoteAdjustmentUpdate),
    resolve: {
      quoteAdjustment: QuoteAdjustmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/quote-adjustment-update').then(m => m.QuoteAdjustmentUpdate),
    resolve: {
      quoteAdjustment: QuoteAdjustmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default quoteAdjustmentRoute;
