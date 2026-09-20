import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InvoiceTypeResolve from './route/invoice-type-routing-resolve.service';

const invoiceTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/invoice-type').then(m => m.InvoiceType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/invoice-type-detail').then(m => m.InvoiceTypeDetail),
    resolve: {
      invoiceType: InvoiceTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/invoice-type-update').then(m => m.InvoiceTypeUpdate),
    resolve: {
      invoiceType: InvoiceTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/invoice-type-update').then(m => m.InvoiceTypeUpdate),
    resolve: {
      invoiceType: InvoiceTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default invoiceTypeRoute;
