import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import QuoteTypeResolve from './route/quote-type-routing-resolve.service';

const quoteTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/quote-type').then(m => m.QuoteType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/quote-type-detail').then(m => m.QuoteTypeDetail),
    resolve: {
      quoteType: QuoteTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/quote-type-update').then(m => m.QuoteTypeUpdate),
    resolve: {
      quoteType: QuoteTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/quote-type-update').then(m => m.QuoteTypeUpdate),
    resolve: {
      quoteType: QuoteTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default quoteTypeRoute;
