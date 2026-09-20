import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import RoleTypeResolve from './route/role-type-routing-resolve.service';

const roleTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/role-type').then(m => m.RoleType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/role-type-detail').then(m => m.RoleTypeDetail),
    resolve: {
      roleType: RoleTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/role-type-update').then(m => m.RoleTypeUpdate),
    resolve: {
      roleType: RoleTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/role-type-update').then(m => m.RoleTypeUpdate),
    resolve: {
      roleType: RoleTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default roleTypeRoute;
