import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ShoppingListItemResolve from './route/shopping-list-item-routing-resolve.service';

const shoppingListItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/shopping-list-item').then(m => m.ShoppingListItem),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/shopping-list-item-detail').then(m => m.ShoppingListItemDetail),
    resolve: {
      shoppingListItem: ShoppingListItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/shopping-list-item-update').then(m => m.ShoppingListItemUpdate),
    resolve: {
      shoppingListItem: ShoppingListItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/shopping-list-item-update').then(m => m.ShoppingListItemUpdate),
    resolve: {
      shoppingListItem: ShoppingListItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default shoppingListItemRoute;
