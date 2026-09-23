import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { serverApiUrl } from 'app/config';

/** One row of the customer list, as shaped by SnpHelperService.prepareCustomerInfo. */
export interface CustomerRow {
  id: number;
  name: string | null;
  email: string | null;
  phoneNumber: string | null;
  address: string | null;
}

/** What the backend's CustomerDetailsReq actually accepts. */
export interface CustomerPayload {
  name: string;
  email: string;
  phoneNumber: string;
  address: string;
}

export interface CustomerPage {
  rows: CustomerRow[];
  totalRecords: number;
  totalPages: number;
}

@Injectable({ providedIn: 'root' })
export class CustomerService {
  private readonly http = inject(HttpClient);
  private readonly resourceUrl = `${serverApiUrl}api/parties`;

  /**
   * snp21 built URLs from the raw SERVER_API_URL global. snp22 re-exports that as
   * `serverApiUrl` from app/config, which is what the generated entity services use.
   */
  getAllCustomersList(page: number, size: number): Observable<CustomerPage> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http
      .get<{ response?: { rows?: CustomerRow[]; totalRecords?: number; totalPages?: number } }>(`${this.resourceUrl}/all/customers`, {
        params,
      })
      .pipe(
        map(body => ({
          rows: body.response?.rows ?? [],
          totalRecords: body.response?.totalRecords ?? 0,
          totalPages: body.response?.totalPages ?? 0,
        })),
      );
  }

  /** One customer, shaped like a list row. */
  getCustomerById(id: number): Observable<CustomerRow> {
    return this.http
      .get<{ success?: CustomerRow; error?: unknown }>(`${this.resourceUrl}/customer/id/${id}/details`)
      .pipe(map(body => body.success as CustomerRow));
  }

  createCustomer(payload: CustomerPayload): Observable<unknown> {
    return this.http.post(`${this.resourceUrl}/customer/create`, payload);
  }

  /**
   * snp21 sent this to `/api/customer/update/id/{id}`, but nothing was ever mapped at
   * /api/customer/** — the controller serves /api/parties/customer/... — so Edit and
   * View 404'd in snp21. Corrected here.
   */
  updateCustomer(id: number, payload: CustomerPayload): Observable<unknown> {
    return this.http.put(`${this.resourceUrl}/customer/update/id/${id}`, payload);
  }
}
