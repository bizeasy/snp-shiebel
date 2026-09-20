import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ContactMechTypeResolve from './route/contact-mech-type-routing-resolve.service';

const contactMechTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/contact-mech-type').then(m => m.ContactMechType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/contact-mech-type-detail').then(m => m.ContactMechTypeDetail),
    resolve: {
      contactMechType: ContactMechTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/contact-mech-type-update').then(m => m.ContactMechTypeUpdate),
    resolve: {
      contactMechType: ContactMechTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/contact-mech-type-update').then(m => m.ContactMechTypeUpdate),
    resolve: {
      contactMechType: ContactMechTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default contactMechTypeRoute;
