import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import UomConversionResolve from './route/uom-conversion-routing-resolve.service';

const uomConversionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/uom-conversion').then(m => m.UomConversion),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/uom-conversion-detail').then(m => m.UomConversionDetail),
    resolve: {
      uomConversion: UomConversionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/uom-conversion-update').then(m => m.UomConversionUpdate),
    resolve: {
      uomConversion: UomConversionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/uom-conversion-update').then(m => m.UomConversionUpdate),
    resolve: {
      uomConversion: UomConversionResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default uomConversionRoute;
