import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { serverApiUrl } from 'app/config';

/** One test / specification row, stored as JSON in Product.info. */
export interface ProdDetail {
  test: string;
  specification: string;
}

/** A product list row, as shaped by SnpHelperService.transformProduct. */
export interface ProductRow {
  id: number;
  productName: string | null;
  price: number | null;
  /** JSON string of ProdDetail[] — held in Product.info. */
  specification: string | null;
  test: string | null;
}

export interface ProductPage {
  rows: ProductRow[];
  totalRecords: number;
  totalPages: number;
}

export interface CreateProductPayload {
  name: string;
  price: number | null;
  prodListDTO: ProdDetail[];
}

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly http = inject(HttpClient);
  private readonly resourceUrl = `${serverApiUrl}api/products`;

  getAllProductsList(page: number, size: number): Observable<ProductPage> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http
      .get<{ response?: { rows?: ProductRow[]; totalRecords?: number; totalPages?: number } }>(`${this.resourceUrl}/products/all`, {
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

  createProduct(payload: CreateProductPayload): Observable<unknown> {
    return this.http.post(`${this.resourceUrl}/products/create/product`, payload);
  }

  /**
   * snp21 called /api/products/product/id/{id}, but the controller serves
   * /api/products/products/product/id/{id} — so the product view always 404'd.
   * Corrected here.
   */
  getProductById(id: number): Observable<ProductRow> {
    return this.http
      .get<{ success?: ProductRow }>(`${this.resourceUrl}/products/product/id/${id}`)
      .pipe(map(body => body.success as ProductRow));
  }

  /** Product.info holds a JSON array of {test, specification}; tolerate bad/absent JSON. */
  parseDetails(row: ProductRow | null): ProdDetail[] {
    if (!row?.specification) {
      return [];
    }
    try {
      const parsed: unknown = JSON.parse(row.specification);
      return Array.isArray(parsed) ? (parsed as ProdDetail[]) : [];
    } catch {
      return [];
    }
  }
}
