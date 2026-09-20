import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CashBookResolve from './route/cash-book-routing-resolve.service';

const cashBookRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cash-book').then(m => m.CashBook),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cash-book-detail').then(m => m.CashBookDetail),
    resolve: {
      cashBook: CashBookResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cash-book-update').then(m => m.CashBookUpdate),
    resolve: {
      cashBook: CashBookResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cash-book-update').then(m => m.CashBookUpdate),
    resolve: {
      cashBook: CashBookResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default cashBookRoute;
