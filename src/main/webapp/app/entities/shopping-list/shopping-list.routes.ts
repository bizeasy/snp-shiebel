import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ShoppingListResolve from './route/shopping-list-routing-resolve.service';

const shoppingListRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/shopping-list').then(m => m.ShoppingList),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/shopping-list-detail').then(m => m.ShoppingListDetail),
    resolve: {
      shoppingList: ShoppingListResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/shopping-list-update').then(m => m.ShoppingListUpdate),
    resolve: {
      shoppingList: ShoppingListResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/shopping-list-update').then(m => m.ShoppingListUpdate),
    resolve: {
      shoppingList: ShoppingListResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default shoppingListRoute;
