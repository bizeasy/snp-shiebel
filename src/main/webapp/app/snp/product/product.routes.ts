import { Routes } from '@angular/router';

/** Product routes, ported from snp21 product-routes.ts. Paths kept identical. */
const productRoutes: Routes = [
  { path: '', redirectTo: 'product-list', pathMatch: 'full' },
  {
    path: 'product-list',
    loadComponent: () => import('./product-list/product-list'),
    title: 'snp.product.list.title',
  },
  {
    path: 'product-list/create-product',
    loadComponent: () => import('./product-form/product-form'),
    title: 'snp.product.create.title',
  },
];

export default productRoutes;
