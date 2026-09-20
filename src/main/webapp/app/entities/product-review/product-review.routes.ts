import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ProductReviewResolve from './route/product-review-routing-resolve.service';

const productReviewRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-review').then(m => m.ProductReview),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-review-detail').then(m => m.ProductReviewDetail),
    resolve: {
      productReview: ProductReviewResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-review-update').then(m => m.ProductReviewUpdate),
    resolve: {
      productReview: ProductReviewResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-review-update').then(m => m.ProductReviewUpdate),
    resolve: {
      productReview: ProductReviewResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default productReviewRoute;
