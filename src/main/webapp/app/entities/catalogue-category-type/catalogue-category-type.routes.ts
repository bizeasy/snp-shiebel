import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CatalogueCategoryTypeResolve from './route/catalogue-category-type-routing-resolve.service';

const catalogueCategoryTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/catalogue-category-type').then(m => m.CatalogueCategoryType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/catalogue-category-type-detail').then(m => m.CatalogueCategoryTypeDetail),
    resolve: {
      catalogueCategoryType: CatalogueCategoryTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/catalogue-category-type-update').then(m => m.CatalogueCategoryTypeUpdate),
    resolve: {
      catalogueCategoryType: CatalogueCategoryTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/catalogue-category-type-update').then(m => m.CatalogueCategoryTypeUpdate),
    resolve: {
      catalogueCategoryType: CatalogueCategoryTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default catalogueCategoryTypeRoute;
