import { Routes } from '@angular/router';

/**
 * File manager routes, ported from snp21 dropbox.routes.ts. Paths kept identical so the
 * existing side-menu links (/dropbox and /dropbox/shared-list) keep working.
 */
const fileManagerRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./file-browser/file-browser'),
    title: 'snp.fileManager.files.title',
  },
  {
    path: 'shared-list',
    loadComponent: () => import('./shared-files/shared-files'),
    title: 'snp.fileManager.shared.title',
  },
  {
    path: 'trash',
    loadComponent: () => import('./trash/trash'),
    title: 'snp.fileManager.trash.title',
  },
];

export default fileManagerRoutes;
