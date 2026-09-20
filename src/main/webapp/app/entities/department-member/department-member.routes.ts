import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import DepartmentMemberResolve from './route/department-member-routing-resolve.service';

const departmentMemberRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/department-member').then(m => m.DepartmentMember),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/department-member-detail').then(m => m.DepartmentMemberDetail),
    resolve: {
      departmentMember: DepartmentMemberResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/department-member-update').then(m => m.DepartmentMemberUpdate),
    resolve: {
      departmentMember: DepartmentMemberResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/department-member-update').then(m => m.DepartmentMemberUpdate),
    resolve: {
      departmentMember: DepartmentMemberResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default departmentMemberRoute;
