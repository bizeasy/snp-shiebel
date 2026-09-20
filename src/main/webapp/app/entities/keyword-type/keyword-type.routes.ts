import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import KeywordTypeResolve from './route/keyword-type-routing-resolve.service';

const keywordTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/keyword-type').then(m => m.KeywordType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/keyword-type-detail').then(m => m.KeywordTypeDetail),
    resolve: {
      keywordType: KeywordTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/keyword-type-update').then(m => m.KeywordTypeUpdate),
    resolve: {
      keywordType: KeywordTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/keyword-type-update').then(m => m.KeywordTypeUpdate),
    resolve: {
      keywordType: KeywordTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default keywordTypeRoute;
