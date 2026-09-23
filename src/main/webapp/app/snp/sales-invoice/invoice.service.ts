import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { serverApiUrl } from 'app/config';

/** One row of the invoice list. */
export interface InvoiceRow {
  id: number;
  invoiceNo: string | null;
  invoiceDate: string | null;
  dueDate: string | null;
  grandTotal: number | null;
  customer: string | null;
  orderId: number | null;
  orderRefNo: string | null;
  orderName: string | null;
  statusName: string | null;
  status: string | null;
  type: string | null;
}

/** A line on an invoice. Product lines carry a product; tax is a header adjustment. */
export interface InvoiceLine {
  id: number;
  sequenceNo: number | null;
  product: string | null;
  label: string | null;
  quantity: number | null;
  unitPrice: number | null;
  amount: number | null;
  uom: string | null;
  type: string | null;
}

export interface InvoiceDetail extends InvoiceRow {
  projectName: string | null;
  description: string | null;
  remarks: string | null;
  paymentTerms: string | null;
  deliveryTerms: string | null;
  referenceNumber: string | null;
  invoiceMessage: string | null;
  paidDate: string | null;
  entryDate: string | null;
  currency: string | null;
  items: InvoiceLine[];
}

/** A sales order in the picker, with what is still left to bill. */
export interface InvoiceableOrder {
  id: number;
  name: string | null;
  refNo: string | null;
  orderDate: string | null;
  customer: string | null;
  grandTotal: number | null;
  outstandingValue: number;
  fullyInvoiced: boolean;
}

/** One order line prepared for invoicing. */
export interface BillableLine {
  orderItemId: number;
  sequenceNo: number | null;
  productId: number | null;
  product: string | null;
  description: string | null;
  uom: string | null;
  orderedQuantity: number;
  billedQuantity: number;
  remainingQuantity: number;
  unitPrice: number;
}

export interface BillableOrder {
  orderId: number;
  orderName: string | null;
  orderRefNo: string | null;
  orderDate: string | null;
  grandTotal: number | null;
  projectName: string | null;
  paymentTerms: string | null;
  deliveryTerms: string | null;
  customerId: number | null;
  customer: string | null;
  currency: string | null;
  outstandingValue: number;
  fullyInvoiced: boolean;
  lines: BillableLine[];
}

export interface TaxSlab {
  id: number;
  name: string | null;
  rate: number;
}

export interface CreateInvoicePayload {
  orderId: number;
  invoiceDate?: string | null;
  dueDate?: string | null;
  taxSlabId?: number | null;
  paymentTerms?: string | null;
  deliveryTerms?: string | null;
  remarks?: string | null;
  invoiceMessage?: string | null;
  lines: { orderItemId: number; quantity: number; unitPrice: number }[];
}

export interface InvoicePage {
  rows: InvoiceRow[];
  totalRecords: number;
  totalPages: number;
}

/** The statuses an invoice can be moved to. */
export type InvoiceStatus = 'INVOICE_CREATED' | 'INVOICE_APPROVED' | 'INVOICE_PAID' | 'INVOICE_CANCELLED';

/**
 * Sales invoicing.
 *
 * There was no snp21 service to port: its Create Sales Invoice screen called
 * createPurchaseOrder() and navigated to the purchase order list, so no invoice was ever
 * written. These endpoints are new, and sit alongside the generated /api/invoices CRUD.
 */
@Injectable({ providedIn: 'root' })
export class InvoiceService {
  private readonly http = inject(HttpClient);
  private readonly resourceUrl = `${serverApiUrl}api/invoices`;

  list(page: number, size: number, search?: string): Observable<InvoicePage> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<InvoicePage>(`${this.resourceUrl}/list`, { params });
  }

  details(id: number): Observable<InvoiceDetail> {
    return this.http
      .get<{ success?: InvoiceDetail }>(`${this.resourceUrl}/id/${id}/details`)
      .pipe(map(body => body.success as InvoiceDetail));
  }

  invoiceableOrders(): Observable<InvoiceableOrder[]> {
    return this.http.get<InvoiceableOrder[]>(`${this.resourceUrl}/invoiceable-orders`);
  }

  billableOrder(orderId: number): Observable<BillableOrder> {
    return this.http
      .get<{ success?: BillableOrder }>(`${this.resourceUrl}/order/${orderId}/billable`)
      .pipe(map(body => body.success as BillableOrder));
  }

  create(payload: CreateInvoicePayload): Observable<{ success?: InvoiceRow; error?: unknown }> {
    return this.http.post<{ success?: InvoiceRow; error?: unknown }>(`${this.resourceUrl}/create`, payload);
  }

  changeStatus(id: number, status: InvoiceStatus): Observable<{ success?: InvoiceRow; error?: unknown }> {
    const params = new HttpParams().set('status', status);
    return this.http.put<{ success?: InvoiceRow; error?: unknown }>(`${this.resourceUrl}/id/${id}/status`, null, { params });
  }

  nextInvoiceNo(): Observable<string> {
    return this.http.get(`${this.resourceUrl}/no/generate`, { responseType: 'text' });
  }

  taxSlabs(): Observable<TaxSlab[]> {
    return this.http.get<TaxSlab[]>(`${this.resourceUrl}/tax-slabs`);
  }
}
