import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ContentTypeResolve from './route/content-type-routing-resolve.service';

const contentTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/content-type').then(m => m.ContentType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/content-type-detail').then(m => m.ContentTypeDetail),
    resolve: {
      contentType: ContentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/content-type-update').then(m => m.ContentTypeUpdate),
    resolve: {
      contentType: ContentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/content-type-update').then(m => m.ContentTypeUpdate),
    resolve: {
      contentType: ContentTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default contentTypeRoute;
