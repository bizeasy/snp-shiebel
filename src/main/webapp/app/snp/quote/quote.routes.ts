import { Routes } from '@angular/router';

/** Quote routes. Paths match snp21 so the existing menu links resolve. */
const quoteRoutes: Routes = [
  { path: '', redirectTo: 'quote-list', pathMatch: 'full' },
  { path: 'quote-list', loadComponent: () => import('./quote-list/quote-list'), title: 'snp.quote.list.title' },
  { path: 'quote-list/create-quote', loadComponent: () => import('./quote-form/quote-form'), title: 'snp.quote.create.title' },
];

export default quoteRoutes;
