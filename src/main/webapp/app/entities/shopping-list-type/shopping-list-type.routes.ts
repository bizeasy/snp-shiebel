import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ShoppingListTypeResolve from './route/shopping-list-type-routing-resolve.service';

const shoppingListTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/shopping-list-type').then(m => m.ShoppingListType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/shopping-list-type-detail').then(m => m.ShoppingListTypeDetail),
    resolve: {
      shoppingListType: ShoppingListTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/shopping-list-type-update').then(m => m.ShoppingListTypeUpdate),
    resolve: {
      shoppingListType: ShoppingListTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/shopping-list-type-update').then(m => m.ShoppingListTypeUpdate),
    resolve: {
      shoppingListType: ShoppingListTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default shoppingListTypeRoute;
