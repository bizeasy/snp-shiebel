import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { serverApiUrl } from 'app/config';

/** A sales / purchase order list row, as shaped by SnpHelperService. */
export interface OrderRow {
  id: number;
  name: string | null;
  refNo: string | null;
  orderRefNo: string | null;
  party: string | null;
  supplier: string | null;
  supplierReferenceNo: string | null;
  orderDate: string | null;
  deliveryDate: string | null;
  customerDeliveryDate: string | null;
  status: string | null;
  grandTotal: number | null;
  version: number | null;
  poType: string | null;
  projectName: string | null;
}

export interface OrderPartyBlock {
  id?: number;
  name?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  address?: string | null;
}

export interface OrderItemRow {
  orderItemId: number;
  id: number | null;
  serialNo: number | null;
  tagNo: string | null;
  description: string | null;
  flange: string | null;
  actuatorControlVoltage: string | null;
  actuatorFailCondition: string | null;
  actuatorMotorDuty: string | null;
  qty: number | null;
  unitPrice: number | null;
  totalPrice: number | null;
}

export interface OrderDetail {
  id: number;
  projectName: string | null;
  name: string | null;
  refNo: string | null;
  customerOrderRef: string | null;
  /** "Factory Offer Reference" in snp21; stored in Order.description. */
  factoryRef: string | null;
  orderDate: string | null;
  deliveryDate: string | null;
  status: string | null;
  orderType: string | null;
  version: number | null;
  versionRef: string | null;
  poType: string | null;
  grandTotal: number | null;
  termsReq: {
    currencyId?: number | null;
    currency?: string | null;
    deliveryTerms?: string | null;
    paymentTerms?: string | null;
    remarks?: string | null;
  };
  customerDetails: OrderPartyBlock;
  consigneeDetails: OrderPartyBlock;
  supplierDetails: OrderPartyBlock;
  productDetailesList: OrderItemRow[];
}

export interface OrderLinePayload {
  /** Present when the line already exists, so the server updates it rather than replacing it. */
  orderItemId: number | null;
  serialNo: number;
  tagNo: string;
  description: string;
  flange: string;
  actuatorMotorDuty: string;
  actuatorControlVoltage: string;
  actuatorFailCondition: string;
  remoteOperation: string;
  qty: number | null;
  unitPrice: number | null;
}

export interface OrderPayload {
  id?: number | null;
  existingCustomer: string;
  existingPartyId: number | null;
  supplierId: number | null;
  projectName: string;
  refNo: string;
  customerOrderRef: string;
  factoryRef: string;
  deliveryDate: string;
  poType: string;
  version: number | null;
  termsReq: {
    currencyId: number | null;
    paymentTerms: string;
    deliveryTerms: string;
    remarks: string;
  };
  productDetailsList: OrderLinePayload[];
}

export interface CurrencyUom {
  id: number;
  name: string;
  abbreviation?: string | null;
}

export interface OrderPage {
  rows: OrderRow[];
  totalRecords: number;
  totalPages: number;
}

@Injectable({ providedIn: 'root' })
export class SalesOrderService {
  private readonly http = inject(HttpClient);
  private readonly resourceUrl = `${serverApiUrl}api/orders`;

  private page$(url: string, page: number, size: number): Observable<OrderPage> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<{ response?: { rows?: OrderRow[]; totalRecords?: number; totalPages?: number } }>(url, { params }).pipe(
      map(body => ({
        rows: body.response?.rows ?? [],
        totalRecords: body.response?.totalRecords ?? 0,
        totalPages: body.response?.totalPages ?? 0,
      })),
    );
  }

  getAllSalesList(page: number, size: number): Observable<OrderPage> {
    return this.page$(`${this.resourceUrl}/sales`, page, size);
  }

  getAllPurchaseList(page: number, size: number): Observable<OrderPage> {
    return this.page$(`${this.resourceUrl}/purchase`, page, size);
  }

  /** Full sales order detail. */
  getSalesOrderById(id: number): Observable<OrderDetail> {
    return this.http
      .get<{ success?: OrderDetail }>(`${this.resourceUrl}/id/${id}/sales/details`)
      .pipe(map(body => body.success as OrderDetail));
  }

  /** Full purchase order detail. */
  getPurchaseOrderById(id: number): Observable<OrderDetail> {
    return this.http
      .get<{ success?: OrderDetail }>(`${this.resourceUrl}/id/${id}/purchase/details`)
      .pipe(map(body => body.success as OrderDetail));
  }

  createSalesOrder(payload: OrderPayload): Observable<unknown> {
    return this.http.post(`${this.resourceUrl}/sales/create`, payload);
  }

  createPurchaseOrder(payload: OrderPayload): Observable<unknown> {
    return this.http.post(`${this.resourceUrl}/purchase/create`, payload);
  }

  /** snp21 called this path but nothing was mapped to it, so saving an edit 404'd. */
  editSalesOrder(id: number, payload: OrderPayload): Observable<unknown> {
    return this.http.put(`${this.resourceUrl}/sales/id/${id}/edit`, payload);
  }

  editPurchaseOrder(id: number, payload: OrderPayload): Observable<unknown> {
    return this.http.put(`${this.resourceUrl}/purchase/id/${id}/update`, payload);
  }

  generateOrderNo(kind: 'sales' | 'purchase'): Observable<string> {
    return this.http.get(`${this.resourceUrl}/${kind === 'sales' ? 'no' : 'pono'}/generate`, { responseType: 'text' });
  }

  isRefNoAvailable(refNo: string): Observable<boolean> {
    return this.http
      .get<{ available?: boolean }>(`${this.resourceUrl}/ref-no/available`, { params: new HttpParams().set('refNo', refNo) })
      .pipe(map(body => body.available === true));
  }

  getCurrencyUoms(): Observable<CurrencyUom[]> {
    return this.http
      .get<{ success?: CurrencyUom[] }>(`${serverApiUrl}api/uoms/currency-measure`)
      .pipe(map(body => (Array.isArray(body.success) ? body.success : [])));
  }
}
