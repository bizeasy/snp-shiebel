import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InvoiceResolve from './route/invoice-routing-resolve.service';

const invoiceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/invoice').then(m => m.Invoice),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/invoice-detail').then(m => m.InvoiceDetail),
    resolve: {
      invoice: InvoiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/invoice-update').then(m => m.InvoiceUpdate),
    resolve: {
      invoice: InvoiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/invoice-update').then(m => m.InvoiceUpdate),
    resolve: {
      invoice: InvoiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default invoiceRoute;
