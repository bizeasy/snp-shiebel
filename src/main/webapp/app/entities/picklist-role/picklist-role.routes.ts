import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PicklistRoleResolve from './route/picklist-role-routing-resolve.service';

const picklistRoleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/picklist-role').then(m => m.PicklistRole),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/picklist-role-detail').then(m => m.PicklistRoleDetail),
    resolve: {
      picklistRole: PicklistRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/picklist-role-update').then(m => m.PicklistRoleUpdate),
    resolve: {
      picklistRole: PicklistRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/picklist-role-update').then(m => m.PicklistRoleUpdate),
    resolve: {
      picklistRole: PicklistRoleResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default picklistRoleRoute;
