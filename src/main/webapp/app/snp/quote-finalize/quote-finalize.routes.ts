import { Routes } from '@angular/router';

/** Quote finalization routes. */
const quoteFinalizeRoutes: Routes = [
  { path: '', redirectTo: 'quote-finalize-list', pathMatch: 'full' },
  { path: 'quote-finalize-list', loadComponent: () => import('./quote-finalize'), title: 'snp.quoteFinalize.list.title' },
];

export default quoteFinalizeRoutes;
