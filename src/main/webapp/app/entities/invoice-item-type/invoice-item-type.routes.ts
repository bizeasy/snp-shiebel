import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InvoiceItemTypeResolve from './route/invoice-item-type-routing-resolve.service';

const invoiceItemTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/invoice-item-type').then(m => m.InvoiceItemType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/invoice-item-type-detail').then(m => m.InvoiceItemTypeDetail),
    resolve: {
      invoiceItemType: InvoiceItemTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/invoice-item-type-update').then(m => m.InvoiceItemTypeUpdate),
    resolve: {
      invoiceItemType: InvoiceItemTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/invoice-item-type-update').then(m => m.InvoiceItemTypeUpdate),
    resolve: {
      invoiceItemType: InvoiceItemTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default invoiceItemTypeRoute;
