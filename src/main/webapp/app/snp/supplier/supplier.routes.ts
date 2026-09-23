import { Routes } from '@angular/router';

/**
 * Supplier routes, ported from snp21 supplier-routes.ts. Paths kept identical.
 *
 * manage-products is rebuilt rather than copied: in snp21 all four of its endpoints
 * 404'd AND its selection logic was commented out, so it never worked.
 */
const supplierRoutes: Routes = [
  { path: '', redirectTo: 'supplier-list', pathMatch: 'full' },
  {
    path: 'supplier-list',
    loadComponent: () => import('./supplier-list/supplier-list'),
    title: 'snp.supplier.list.title',
  },
  {
    path: 'supplier-list/create-supplier',
    loadComponent: () => import('./supplier-form/supplier-form'),
    title: 'snp.supplier.create.title',
  },
  {
    path: 'manage-products',
    loadComponent: () => import('./manage-products/manage-products'),
    title: 'snp.supplier.manageProducts.title',
  },
  {
    path: 'supplier-list/edit',
    loadComponent: () => import('./supplier-form/supplier-form'),
    title: 'snp.supplier.edit.title',
  },
];

export default supplierRoutes;
