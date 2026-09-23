import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { serverApiUrl } from 'app/config';

export interface EnquiryRow {
  id: number;
  enquiryNo: string | null;
  customer: string | null;
  customerId: number | null;
  enquiryDate: string | null;
  responseRequiredDate: string | null;
  currency: string | null;
  currencyUomId: number | null;
  statusName: string | null;
  status: string | null;
  lineCount: number;
}

export interface EnquiryLine {
  id: number;
  sequenceNo: number | null;
  productId: number | null;
  product: string | null;
  description: string | null;
  quantity: number | null;
  maxAmount: number | null;
  requiredByDate: string | null;
  uom: string | null;
  status: string | null;
  statusName: string | null;
  quoteCount: number;
}

export interface EnquiryDetail extends EnquiryRow {
  description: string | null;
  remarks: string | null;
  lines: EnquiryLine[];
}

export interface EnquiryPage {
  rows: EnquiryRow[];
  totalRecords: number;
  totalPages: number;
}

export interface CreateEnquiryPayload {
  partyId: number;
  name?: string | null;
  description?: string | null;
  remarks?: string | null;
  receivedDate?: string | null;
  responseRequiredDate?: string | null;
  currencyUomId?: number | null;
  lines: {
    productId: number | null;
    quantity: number | null;
    description?: string | null;
    requiredByDate?: string | null;
    maxAmount?: number | null;
    uomId?: number | null;
  }[];
}

/**
 * Enquiries (RFQ).
 *
 * snp21's enquiry service called /api/cust-requests/enquiry/create, but the controller -
 * already mapped at /api/cust-requests - declared the method as "/cust-requests/enquiry/create",
 * so the served path was /api/cust-requests/cust-requests/enquiry/create. Every call 404'd.
 * These paths resolve.
 */
@Injectable({ providedIn: 'root' })
export class EnquiryService {
  private readonly http = inject(HttpClient);
  private readonly resourceUrl = `${serverApiUrl}api/cust-requests`;

  list(page: number, size: number, search?: string): Observable<EnquiryPage> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<EnquiryPage>(`${this.resourceUrl}/enquiry/list`, { params });
  }

  details(id: number): Observable<EnquiryDetail> {
    return this.http.get<{ success?: EnquiryDetail }>(`${this.resourceUrl}/enquiry/${id}`).pipe(map(body => body.success as EnquiryDetail));
  }

  create(payload: CreateEnquiryPayload): Observable<{ success?: EnquiryRow; error?: unknown }> {
    return this.http.post<{ success?: EnquiryRow; error?: unknown }>(`${this.resourceUrl}/enquiry`, payload);
  }
}
