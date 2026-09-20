import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FileFolderResolve from './route/file-folder-routing-resolve.service';

const fileFolderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/file-folder').then(m => m.FileFolder),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/file-folder-detail').then(m => m.FileFolderDetail),
    resolve: {
      fileFolder: FileFolderResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/file-folder-update').then(m => m.FileFolderUpdate),
    resolve: {
      fileFolder: FileFolderResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/file-folder-update').then(m => m.FileFolderUpdate),
    resolve: {
      fileFolder: FileFolderResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default fileFolderRoute;
