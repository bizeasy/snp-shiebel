import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InvoiceItemResolve from './route/invoice-item-routing-resolve.service';

const invoiceItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/invoice-item').then(m => m.InvoiceItem),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/invoice-item-detail').then(m => m.InvoiceItemDetail),
    resolve: {
      invoiceItem: InvoiceItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/invoice-item-update').then(m => m.InvoiceItemUpdate),
    resolve: {
      invoiceItem: InvoiceItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/invoice-item-update').then(m => m.InvoiceItemUpdate),
    resolve: {
      invoiceItem: InvoiceItemResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default invoiceItemRoute;
