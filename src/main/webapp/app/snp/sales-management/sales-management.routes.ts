import { Routes } from '@angular/router';

/**
 * Sales management routes, ported from snp21 sales-management-routes.ts.
 *
 * Paths are kept identical to snp21 so existing links, bookmarks and the side menu
 * continue to work. create-customer and edit share one component, as they did in snp21.
 */
const salesManagementRoutes: Routes = [
  { path: '', redirectTo: 'customer-list', pathMatch: 'full' },
  {
    path: 'customer-list',
    loadComponent: () => import('./customer-list/customer-list'),
    title: 'snp.salesManagement.customerList.title',
  },
  {
    path: 'customer-list/create-customer',
    loadComponent: () => import('./customer-form/customer-form'),
    title: 'snp.salesManagement.customerCreate.title',
  },
  {
    path: 'customer-list/edit',
    loadComponent: () => import('./customer-form/customer-form'),
    title: 'snp.salesManagement.customerEdit.title',
  },
  {
    path: 'customer-list/view',
    loadComponent: () => import('./customer-view/customer-view'),
    title: 'snp.salesManagement.customerView.title',
  },
];

export default salesManagementRoutes;
