import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import TransactionTypeResolve from './route/transaction-type-routing-resolve.service';

const transactionTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transaction-type').then(m => m.TransactionType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transaction-type-detail').then(m => m.TransactionTypeDetail),
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transaction-type-update').then(m => m.TransactionTypeUpdate),
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transaction-type-update').then(m => m.TransactionTypeUpdate),
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default transactionTypeRoute;
