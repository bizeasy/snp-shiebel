import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import QuoteResolve from './route/quote-routing-resolve.service';

const quoteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/quote').then(m => m.Quote),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/quote-detail').then(m => m.QuoteDetail),
    resolve: {
      quote: QuoteResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/quote-update').then(m => m.QuoteUpdate),
    resolve: {
      quote: QuoteResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/quote-update').then(m => m.QuoteUpdate),
    resolve: {
      quote: QuoteResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default quoteRoute;
