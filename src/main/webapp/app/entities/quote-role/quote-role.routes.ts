import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import QuoteRoleResolve from './route/quote-role-routing-resolve.service';

const quoteRoleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/quote-role').then(m => m.QuoteRole),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/quote-role-detail').then(m => m.QuoteRoleDetail),
    resolve: {
      quoteRole: QuoteRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/quote-role-update').then(m => m.QuoteRoleUpdate),
    resolve: {
      quoteRole: QuoteRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/quote-role-update').then(m => m.QuoteRoleUpdate),
    resolve: {
      quoteRole: QuoteRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default quoteRoleRoute;
