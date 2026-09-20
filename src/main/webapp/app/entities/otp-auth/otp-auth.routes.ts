import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OtpAuthResolve from './route/otp-auth-routing-resolve.service';

const otpAuthRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/otp-auth').then(m => m.OtpAuth),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/otp-auth-detail').then(m => m.OtpAuthDetail),
    resolve: {
      otpAuth: OtpAuthResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/otp-auth-update').then(m => m.OtpAuthUpdate),
    resolve: {
      otpAuth: OtpAuthResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/otp-auth-update').then(m => m.OtpAuthUpdate),
    resolve: {
      otpAuth: OtpAuthResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default otpAuthRoute;
