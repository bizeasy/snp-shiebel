import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { serverApiUrl } from 'app/config';

export interface QuoteRow {
  id: number;
  quoteNo: string | null;
  name: string | null;
  supplier: string | null;
  supplierId: number | null;
  issueDate: string | null;
  offerValue: number | null;
  currency: string | null;
  enquiryId: number | null;
  enquiryNo: string | null;
  quoteType: string | null;
  statusName: string | null;
  status: string | null;
}

export interface QuoteLine {
  id: number;
  sequenceNo: number | null;
  product: string | null;
  quantity: number | null;
  quoteUnitPrice: number | null;
  totalAmount: number | null;
  leadTimeDays: number | null;
  estimatedDeliveryDate: string | null;
  comments: string | null;
  custRequestItemId: number | null;
}

export interface QuoteDetail extends QuoteRow {
  description: string | null;
  remarks: string | null;
  validFromDate: string | null;
  validThruDate: string | null;
  lines: QuoteLine[];
}

export interface QuotePage {
  rows: QuoteRow[];
  totalRecords: number;
  totalPages: number;
}

/** An enquiry line a quote can be raised against. */
export interface OpenEnquiryLine {
  custRequestItemId: number;
  enquiryId: number;
  enquiryNo: string | null;
  customer: string | null;
  product: string | null;
  quantity: number | null;
  requiredByDate: string | null;
  quoteCount: number;
  status: string | null;
}

/** One supplier offer in the finalization comparison. */
export interface Offer {
  quoteId: number;
  quoteNo: string | null;
  supplier: string | null;
  unitPrice: number | null;
  totalAmount: number | null;
  leadTimeDays: number | null;
  statusName: string | null;
  status: string | null;
}

export interface ComparableLine {
  custRequestItemId: number;
  enquiryId: number | null;
  enquiryNo: string | null;
  customer: string | null;
  product: string | null;
  quantity: number | null;
  status: string | null;
  offers: Offer[];
  bestTotal: number | null;
}

export interface CreateQuotePayload {
  custRequestItemId: number;
  partyId: number;
  quoteNumber?: string | null;
  name?: string | null;
  issueDate?: string | null;
  validFromDate?: string | null;
  validThruDate?: string | null;
  quantity: number | null;
  unitPrice: number | null;
  leadTimeDays?: number | null;
  estimatedDeliveryDate?: string | null;
  quoteTypeId?: number | null;
  description?: string | null;
  remarks?: string | null;
  comments?: string | null;
}

/**
 * Quotes and quote finalization.
 *
 * snp21 called /api/quote/... while the controller served /api/quotes/quote/... - a doubled
 * segment and a singular/plural mismatch, so none of it resolved. These paths do.
 */
@Injectable({ providedIn: 'root' })
export class QuoteService {
  private readonly http = inject(HttpClient);
  private readonly resourceUrl = `${serverApiUrl}api/quotes`;

  list(page: number, size: number, search?: string): Observable<QuotePage> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<QuotePage>(`${this.resourceUrl}/list`, { params });
  }

  details(id: number): Observable<QuoteDetail> {
    return this.http.get<{ success?: QuoteDetail }>(`${this.resourceUrl}/id/${id}/details`).pipe(map(b => b.success as QuoteDetail));
  }

  openEnquiryLines(): Observable<OpenEnquiryLine[]> {
    return this.http.get<OpenEnquiryLine[]>(`${this.resourceUrl}/open-enquiry-lines`);
  }

  create(payload: CreateQuotePayload): Observable<{ success?: QuoteRow; error?: unknown }> {
    return this.http.post<{ success?: QuoteRow; error?: unknown }>(`${this.resourceUrl}/from-enquiry`, payload);
  }

  comparableLines(): Observable<ComparableLine[]> {
    return this.http.get<ComparableLine[]>(`${this.resourceUrl}/comparable-lines`);
  }

  finalize(quoteId: number, remarks?: string | null): Observable<{ success?: QuoteRow & { rejectedCount?: number }; error?: unknown }> {
    return this.http.post<{ success?: QuoteRow & { rejectedCount?: number }; error?: unknown }>(`${this.resourceUrl}/finalize`, {
      quoteId,
      remarks,
    });
  }
}
