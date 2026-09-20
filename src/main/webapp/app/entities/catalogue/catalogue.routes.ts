import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CatalogueResolve from './route/catalogue-routing-resolve.service';

const catalogueRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/catalogue').then(m => m.Catalogue),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/catalogue-detail').then(m => m.CatalogueDetail),
    resolve: {
      catalogue: CatalogueResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/catalogue-update').then(m => m.CatalogueUpdate),
    resolve: {
      catalogue: CatalogueResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/catalogue-update').then(m => m.CatalogueUpdate),
    resolve: {
      catalogue: CatalogueResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default catalogueRoute;
