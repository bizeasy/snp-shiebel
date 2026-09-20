import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import QuoteItemResolve from './route/quote-item-routing-resolve.service';

const quoteItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/quote-item').then(m => m.QuoteItem),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/quote-item-detail').then(m => m.QuoteItemDetail),
    resolve: {
      quoteItem: QuoteItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/quote-item-update').then(m => m.QuoteItemUpdate),
    resolve: {
      quoteItem: QuoteItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/quote-item-update').then(m => m.QuoteItemUpdate),
    resolve: {
      quoteItem: QuoteItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default quoteItemRoute;
