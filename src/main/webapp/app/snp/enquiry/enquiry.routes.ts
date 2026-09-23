import { Routes } from '@angular/router';

/** Enquiry (RFQ) routes. Paths match snp21 so the existing menu links resolve. */
const enquiryRoutes: Routes = [
  { path: '', redirectTo: 'enquiry-list', pathMatch: 'full' },
  { path: 'enquiry-list', loadComponent: () => import('./enquiry-list/enquiry-list'), title: 'snp.enquiry.list.title' },
  { path: 'enquiry-list/create-enquiry', loadComponent: () => import('./enquiry-form/enquiry-form'), title: 'snp.enquiry.create.title' },
];

export default enquiryRoutes;
