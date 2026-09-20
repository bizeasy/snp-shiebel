import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductAttachmentResolve from './route/product-attachment-routing-resolve.service';

const productAttachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-attachment').then(m => m.ProductAttachment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-attachment-detail').then(m => m.ProductAttachmentDetail),
    resolve: {
      productAttachment: ProductAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-attachment-update').then(m => m.ProductAttachmentUpdate),
    resolve: {
      productAttachment: ProductAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-attachment-update').then(m => m.ProductAttachmentUpdate),
    resolve: {
      productAttachment: ProductAttachmentResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productAttachmentRoute;
