import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ContactMechResolve from './route/contact-mech-routing-resolve.service';

const contactMechRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/contact-mech').then(m => m.ContactMech),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/contact-mech-detail').then(m => m.ContactMechDetail),
    resolve: {
      contactMech: ContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/contact-mech-update').then(m => m.ContactMechUpdate),
    resolve: {
      contactMech: ContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/contact-mech-update').then(m => m.ContactMechUpdate),
    resolve: {
      contactMech: ContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default contactMechRoute;
