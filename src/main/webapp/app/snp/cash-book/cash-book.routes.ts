import { Routes } from '@angular/router';

/**
 * Cash book routes.
 *
 * snp21 exposed only create-cash-book. That path is kept so the existing side-menu link
 * works, and a list is added at cash-book-list, since a ledger you cannot read back is
 * of limited use.
 */
const cashBookRoutes: Routes = [
  { path: '', redirectTo: 'cash-book-list', pathMatch: 'full' },
  {
    path: 'cash-book-list',
    loadComponent: () => import('./cash-book-list/cash-book-list'),
    title: 'snp.cashBook.list.title',
  },
  {
    path: 'create-cash-book',
    loadComponent: () => import('./cash-book-form/cash-book-form'),
    title: 'snp.cashBook.create.title',
  },
];

export default cashBookRoutes;
