import { Routes } from '@angular/router';

/**
 * User management routes, ported from snp21 user-management-routes.ts.
 *
 * snp21 also had create-department and department-list components, but they were never
 * added to its router and their service methods posted to the bare server URL, so they
 * were unreachable dead code. They are not carried over; the Department entity added in
 * the JDL is the place to build that properly if it is wanted.
 */
const userManagementRoutes: Routes = [
  { path: '', redirectTo: 'userList', pathMatch: 'full' },
  { path: 'userList', loadComponent: () => import('./user-list/user-list'), title: 'snp.userManagement.list.title' },
  { path: 'userList/create', loadComponent: () => import('./user-form/user-form'), title: 'snp.userManagement.create.title' },
  { path: 'userList/edit', loadComponent: () => import('./user-form/user-form'), title: 'snp.userManagement.edit.title' },
  { path: 'userList/view', loadComponent: () => import('./user-view/user-view'), title: 'snp.userManagement.view.title' },
];

export default userManagementRoutes;
