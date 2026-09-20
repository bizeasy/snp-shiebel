import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InvoiceItemTypeMapResolve from './route/invoice-item-type-map-routing-resolve.service';

const invoiceItemTypeMapRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/invoice-item-type-map').then(m => m.InvoiceItemTypeMap),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/invoice-item-type-map-detail').then(m => m.InvoiceItemTypeMapDetail),
    resolve: {
      invoiceItemTypeMap: InvoiceItemTypeMapResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/invoice-item-type-map-update').then(m => m.InvoiceItemTypeMapUpdate),
    resolve: {
      invoiceItemTypeMap: InvoiceItemTypeMapResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/invoice-item-type-map-update').then(m => m.InvoiceItemTypeMapUpdate),
    resolve: {
      invoiceItemTypeMap: InvoiceItemTypeMapResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default invoiceItemTypeMapRoute;
