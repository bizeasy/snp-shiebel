import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import TransactionCategoryResolve from './route/transaction-category-routing-resolve.service';

const transactionCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transaction-category').then(m => m.TransactionCategory),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transaction-category-detail').then(m => m.TransactionCategoryDetail),
    resolve: {
      transactionCategory: TransactionCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transaction-category-update').then(m => m.TransactionCategoryUpdate),
    resolve: {
      transactionCategory: TransactionCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transaction-category-update').then(m => m.TransactionCategoryUpdate),
    resolve: {
      transactionCategory: TransactionCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default transactionCategoryRoute;
