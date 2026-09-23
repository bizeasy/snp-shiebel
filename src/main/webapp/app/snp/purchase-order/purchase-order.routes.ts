import { Routes } from '@angular/router';

/**
 * Purchase order routes, ported from snp21 purchase-order-routes.ts.
 *
 * create-purchase-order reuses the shared OrderForm from the sales-order module:
 * snp21's two create components were near-duplicates of one another.
 */
const purchaseOrderRoutes: Routes = [
  { path: '', redirectTo: 'purchase-order-list', pathMatch: 'full' },
  {
    path: 'purchase-order-list',
    loadComponent: () => import('./purchase-order-list/purchase-order-list'),
    title: 'snp.purchaseOrder.list.title',
  },
  {
    path: 'purchase-order-list/view-purchase-order',
    loadComponent: () => import('./purchase-order-view/purchase-order-view'),
    title: 'snp.purchaseOrder.view.title',
  },
  {
    path: 'purchase-order-list/create-purchase-order',
    loadComponent: () => import('../sales-order/order-form/order-form'),
    data: { orderKind: 'purchase' },
    title: 'snp.purchaseOrder.create.title',
  },
  {
    path: 'purchase-order-list/edit',
    loadComponent: () => import('../sales-order/order-form/order-form'),
    data: { orderKind: 'purchase' },
    title: 'snp.purchaseOrder.edit.title',
  },
];

export default purchaseOrderRoutes;
