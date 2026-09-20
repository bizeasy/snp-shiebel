import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FileShareResolve from './route/file-share-routing-resolve.service';

const fileShareRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/file-share').then(m => m.FileShare),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/file-share-detail').then(m => m.FileShareDetail),
    resolve: {
      fileShare: FileShareResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/file-share-update').then(m => m.FileShareUpdate),
    resolve: {
      fileShare: FileShareResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/file-share-update').then(m => m.FileShareUpdate),
    resolve: {
      fileShare: FileShareResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default fileShareRoute;
