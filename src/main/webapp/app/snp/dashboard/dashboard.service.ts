import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';

export interface DashboardKpis {
  salesOrderCount: number;
  salesOrderValue: number;
  purchaseOrderCount: number;
  purchaseOrderValue: number;
  customerCount: number;
  supplierCount: number;
  productCount: number;
}

export interface MonthPoint {
  month: string;
  sales: number;
  purchase: number;
}

export interface CategorySpend {
  category: string;
  amount: number;
}

export interface RecentOrder {
  id: number;
  name: string | null;
  refNo: string | null;
  customer: string | null;
  orderDate: string | null;
  grandTotal: number | null;
  status: string | null;
}

export interface DashboardSummary {
  kpis: DashboardKpis;
  orderValueByMonth: MonthPoint[];
  cashBookByCategory: CategorySpend[];
  recentSalesOrders: RecentOrder[];
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);

  summary(months: number): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(`${serverApiUrl}api/dashboard/summary`, {
      params: new HttpParams().set('months', months),
    });
  }
}
