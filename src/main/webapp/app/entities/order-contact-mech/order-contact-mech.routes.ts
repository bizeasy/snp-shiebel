import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrderContactMechResolve from './route/order-contact-mech-routing-resolve.service';

const orderContactMechRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/order-contact-mech').then(m => m.OrderContactMech),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/order-contact-mech-detail').then(m => m.OrderContactMechDetail),
    resolve: {
      orderContactMech: OrderContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/order-contact-mech-update').then(m => m.OrderContactMechUpdate),
    resolve: {
      orderContactMech: OrderContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/order-contact-mech-update').then(m => m.OrderContactMechUpdate),
    resolve: {
      orderContactMech: OrderContactMechResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default orderContactMechRoute;
