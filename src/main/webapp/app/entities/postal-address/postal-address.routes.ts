import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PostalAddressResolve from './route/postal-address-routing-resolve.service';

const postalAddressRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/postal-address').then(m => m.PostalAddress),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/postal-address-detail').then(m => m.PostalAddressDetail),
    resolve: {
      postalAddress: PostalAddressResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/postal-address-update').then(m => m.PostalAddressUpdate),
    resolve: {
      postalAddress: PostalAddressResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/postal-address-update').then(m => m.PostalAddressUpdate),
    resolve: {
      postalAddress: PostalAddressResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default postalAddressRoute;
