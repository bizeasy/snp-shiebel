import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import StatusCategoryResolve from './route/status-category-routing-resolve.service';

const statusCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/status-category').then(m => m.StatusCategory),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/status-category-detail').then(m => m.StatusCategoryDetail),
    resolve: {
      statusCategory: StatusCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/status-category-update').then(m => m.StatusCategoryUpdate),
    resolve: {
      statusCategory: StatusCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/status-category-update').then(m => m.StatusCategoryUpdate),
    resolve: {
      statusCategory: StatusCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default statusCategoryRoute;
