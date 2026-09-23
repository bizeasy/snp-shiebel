import { Routes } from '@angular/router';

/**
 * Sales invoice routes.
 *
 * snp21 routed only /sales-invoice/create-sales-invoice, and that screen wrote purchase
 * orders. The path is kept so the existing menu link still resolves, but it now opens a
 * screen that raises a real invoice; a list and a detail view are added beside it.
 */
const salesInvoiceRoutes: Routes = [
  { path: '', redirectTo: 'invoice-list', pathMatch: 'full' },
  {
    path: 'invoice-list',
    loadComponent: () => import('./invoice-list/invoice-list'),
    title: 'snp.salesInvoice.list.title',
  },
  {
    path: 'create-sales-invoice',
    loadComponent: () => import('./invoice-form/invoice-form'),
    title: 'snp.salesInvoice.create.title',
  },
  {
    path: ':id/view',
    loadComponent: () => import('./invoice-view/invoice-view'),
    title: 'snp.salesInvoice.view.title',
  },
];

export default salesInvoiceRoutes;
