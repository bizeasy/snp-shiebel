import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { serverApiUrl } from 'app/config';

/** A product row on the supplier product screens. */
export interface SupplierProduct {
  id: number;
  name: string | null;
  code: string | null;
  price: number | null;
}

/**
 * A supplier list row.
 *
 * Field overloading inherited from snp21: on Party, vatNo lives in
 * organisationShortName, tanNumber in middleName and code in salutation.
 */
export interface SupplierRow {
  id: number;
  name: string | null;
  vatNo: string | null;
  tanNumber: string | null;
  code: string | null;
  phoneNumber: string | null;
  emailId: string | null;
  address: string | null;
}

export interface SupplierPage {
  rows: SupplierRow[];
  totalRecords: number;
  totalPages: number;
}

/** What the backend's CreateSupplierReq accepts. */
export interface SupplierPayload {
  name: string;
  vatNo: string;
  tanNo: string;
  code: string;
  phone: string;
  email: string;
  address: string;
}

@Injectable({ providedIn: 'root' })
export class SupplierService {
  private readonly http = inject(HttpClient);
  private readonly resourceUrl = `${serverApiUrl}api/parties`;

  getAllSuppliersList(page: number, size: number): Observable<SupplierPage> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http
      .get<{ response?: { rows?: SupplierRow[]; totalRecords?: number; totalPages?: number } }>(`${this.resourceUrl}/all/suppliers`, {
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

  getSupplierById(id: number): Observable<SupplierRow> {
    return this.http
      .get<{ success?: SupplierRow }>(`${this.resourceUrl}/suppliers/id/${id}/details`)
      .pipe(map(body => body.success as SupplierRow));
  }

  createSupplier(payload: SupplierPayload): Observable<unknown> {
    return this.http.post(`${this.resourceUrl}/supplier/create`, payload);
  }

  updateSupplier(id: number, payload: SupplierPayload): Observable<unknown> {
    return this.http.put(`${this.resourceUrl}/supplier/id/${id}/update`, payload);
  }

  // --- supplier <-> product links ------------------------------------------
  //
  // snp21 called /api/product-roles/... and /api/products/suppliers/... while its
  // controllers served /api/product-roles/product-roles/... and
  // /api/products/products/suppliers/..., so all four of these 404'd and the Manage
  // Products screen never worked. snp22 serves the single-segment paths below.

  /** Products already linked to this supplier. */
  getSupplierProductsById(supplierId: number): Observable<SupplierProduct[]> {
    return this.http
      .get<{ success?: SupplierProduct[] | string }>(`${serverApiUrl}api/product-roles/suppliers/id/${supplierId}/products`)
      .pipe(map(body => (Array.isArray(body.success) ? body.success : [])));
  }

  /** Products not yet linked to this supplier. */
  getAddableProducts(supplierId: number): Observable<SupplierProduct[]> {
    return this.http
      .get<{ success?: SupplierProduct[] | string }>(`${serverApiUrl}api/products/suppliers/id/${supplierId}/add`)
      .pipe(map(body => (Array.isArray(body.success) ? body.success : [])));
  }

  addProductsToSupplier(partyId: number, productIds: number[]): Observable<unknown> {
    return this.http.post(`${serverApiUrl}api/product-roles/suppliers/product/add`, { partyId, productIds });
  }

  removeProductFromSupplier(supplierId: number, productId: number): Observable<unknown> {
    return this.http.delete(`${serverApiUrl}api/product-roles/suppliers/id/${supplierId}/product/id/${productId}/delete`);
  }
}
