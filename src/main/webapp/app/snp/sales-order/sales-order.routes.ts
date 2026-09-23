import { Routes } from '@angular/router';

/**
 * Sales order routes, ported from snp21 sales-order-routes.ts. Paths kept identical
 * (including the capitalised View-sales-order) so existing links keep working.
 *
 * create / update / edit all resolve to the shared OrderForm, as snp21 pointed all three
 * at one component.
 *
 * Not carried over:
 *  - view-invoice: snp21's invoice screen creates purchase orders, not invoices.
 *  - file-viewer: its endpoint /api/attachments/{id}/pdfs is unreachable in snp21.
 *  - sales-order-modified-list: existed but was never routed.
 */
const salesOrderRoutes: Routes = [
  { path: '', redirectTo: 'sales-order-list', pathMatch: 'full' },
  {
    path: 'sales-order-list',
    loadComponent: () => import('./sales-order-list/sales-order-list'),
    title: 'snp.salesOrder.list.title',
  },
  {
    path: 'sales-order-list/View-sales-order',
    loadComponent: () => import('./sales-order-view/sales-order-view'),
    title: 'snp.salesOrder.view.title',
  },
  {
    path: 'sales-order-list/create-sales-order',
    loadComponent: () => import('./order-form/order-form'),
    data: { orderKind: 'sales' },
    title: 'snp.salesOrder.create.title',
  },
  {
    path: 'sales-order-list/edit',
    loadComponent: () => import('./order-form/order-form'),
    data: { orderKind: 'sales' },
    title: 'snp.salesOrder.edit.title',
  },
  {
    path: 'sales-order-list/update',
    loadComponent: () => import('./order-form/order-form'),
    data: { orderKind: 'sales' },
    title: 'snp.salesOrder.edit.title',
  },
];

export default salesOrderRoutes;
