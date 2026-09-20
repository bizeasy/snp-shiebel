import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CatalogueCategoryResolve from './route/catalogue-category-routing-resolve.service';

const catalogueCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/catalogue-category').then(m => m.CatalogueCategory),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/catalogue-category-detail').then(m => m.CatalogueCategoryDetail),
    resolve: {
      catalogueCategory: CatalogueCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/catalogue-category-update').then(m => m.CatalogueCategoryUpdate),
    resolve: {
      catalogueCategory: CatalogueCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/catalogue-category-update').then(m => m.CatalogueCategoryUpdate),
    resolve: {
      catalogueCategory: CatalogueCategoryResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default catalogueCategoryRoute;
