import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CustRequestCategoryResolve from './route/cust-request-category-routing-resolve.service';

const custRequestCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cust-request-category').then(m => m.CustRequestCategory),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cust-request-category-detail').then(m => m.CustRequestCategoryDetail),
    resolve: {
      custRequestCategory: CustRequestCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cust-request-category-update').then(m => m.CustRequestCategoryUpdate),
    resolve: {
      custRequestCategory: CustRequestCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cust-request-category-update').then(m => m.CustRequestCategoryUpdate),
    resolve: {
      custRequestCategory: CustRequestCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default custRequestCategoryRoute;
