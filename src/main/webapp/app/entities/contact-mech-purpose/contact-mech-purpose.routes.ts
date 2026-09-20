import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ContactMechPurposeResolve from './route/contact-mech-purpose-routing-resolve.service';

const contactMechPurposeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/contact-mech-purpose').then(m => m.ContactMechPurpose),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/contact-mech-purpose-detail').then(m => m.ContactMechPurposeDetail),
    resolve: {
      contactMechPurpose: ContactMechPurposeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/contact-mech-purpose-update').then(m => m.ContactMechPurposeUpdate),
    resolve: {
      contactMechPurpose: ContactMechPurposeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/contact-mech-purpose-update').then(m => m.ContactMechPurposeUpdate),
    resolve: {
      contactMechPurpose: ContactMechPurposeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default contactMechPurposeRoute;
