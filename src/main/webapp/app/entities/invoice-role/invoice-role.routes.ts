import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InvoiceRoleResolve from './route/invoice-role-routing-resolve.service';

const invoiceRoleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/invoice-role').then(m => m.InvoiceRole),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/invoice-role-detail').then(m => m.InvoiceRoleDetail),
    resolve: {
      invoiceRole: InvoiceRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/invoice-role-update').then(m => m.InvoiceRoleUpdate),
    resolve: {
      invoiceRole: InvoiceRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/invoice-role-update').then(m => m.InvoiceRoleUpdate),
    resolve: {
      invoiceRole: InvoiceRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default invoiceRoleRoute;
