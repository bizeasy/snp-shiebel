import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { serverApiUrl } from 'app/config';

/** A lookup row used by the cash book pickers. */
export interface NamedRef {
  id: number;
  name?: string | null;
  description?: string | null;
  /** Only present on TransactionCategory: which type it is scoped to, if unscoped it applies to every type. */
  transactionType?: { id: number } | null;
}

/** The three transaction types, as seeded: EXPENSE / INCOME / TRANSFER. */
export type TransactionTypeName = 'EXPENSE' | 'INCOME' | 'TRANSFER';

/** The shape a CashBook's fromParty/toParty comes back as - a raw Party, not a CustomerRow/SupplierRow. */
export interface PartyRef {
  id: number;
  isOrganisation?: boolean | null;
  organisationName?: string | null;
  firstName?: string | null;
  lastName?: string | null;
}

/** Mirrors SnpHelperService.partyName() so a linked party reads the same on screen as it does server-side. */
export function partyDisplayName(party: PartyRef | null | undefined): string | null {
  if (!party) {
    return null;
  }
  const org = party.organisationName?.trim();
  if (org) {
    return org;
  }
  const full = `${party.firstName ?? ''} ${party.lastName ?? ''}`.trim();
  return full || String(party.id);
}

export interface CashBookEntry {
  id: number;
  transactionDate?: string | null;
  dueDate?: string | null;
  amount?: number | null;
  description?: string | null;
  vendorCustomerName?: string | null;
  invoiceNo?: string | null;
  invoiceDate?: string | null;
  purpose?: string | null;
  remarks?: string | null;
  project?: string | null;
  usageUnits?: string | null;
  recurringTransaction?: boolean | null;
  frequency?: string | null;
  transactionType?: NamedRef | null;
  transactionCategory?: NamedRef | null;
  paymentMethodType?: NamedRef | null;
  department?: NamedRef | null;
  currencyUom?: NamedRef | null;
  /** Who the money came from - only meaningful (and only ever set) for INCOME entries. */
  fromParty?: PartyRef | null;
  /** Who the money went to - only meaningful (and only ever set) for EXPENSE entries. */
  toParty?: PartyRef | null;
}

export interface CashBookPage {
  rows: CashBookEntry[];
  totalRecords: number;
}

/** Everything the list screen can filter the ledger by. Every field is optional. */
export interface CashBookFilter {
  dateFrom?: string | null;
  dateTo?: string | null;
  transactionTypeId?: number | null;
  transactionCategoryId?: number | null;
  departmentId?: number | null;
  /** Matched against description, purpose, vendor/customer name and invoice no. */
  search?: string | null;
}

export interface CashBookSummary {
  income: number;
  expense: number;
  transfer: number;
  net: number;
  count: number;
  /** True when more rows matched the filter than summary() looked at - see its doc. */
  truncated: boolean;
}

/** Rows fetched per page when computing a summary over an entire filtered range. */
const SUMMARY_PAGE_SIZE = 2000;

/**
 * Cash book, built on the generated CRUD API.
 *
 * snp21 posted to /api/cashbook/create, which nothing served. The JDL gives CashBook
 * service + filter + pagination, and the generated CashBookCriteria/CashBookQueryService
 * already support filtering by date range, type, category, department and free-text
 * fields - /api/cash-books just needed to be asked for it, which is what search() below
 * does instead of list()'s old plain pagination.
 */
@Injectable({ providedIn: 'root' })
export class CashBookService {
  private readonly http = inject(HttpClient);
  private readonly resourceUrl = `${serverApiUrl}api/cash-books`;

  /** Server-side filtered + paginated ledger. */
  search(filter: CashBookFilter, page: number, size: number): Observable<CashBookPage> {
    const params = this.buildParams(filter).set('page', page).set('size', size).set('sort', 'transactionDate,desc');
    return this.http.get<CashBookEntry[]>(this.resourceUrl, { params, observe: 'response' }).pipe(
      map(res => ({
        rows: res.body ?? [],
        totalRecords: Number(res.headers.get('X-Total-Count') ?? res.body?.length ?? 0),
      })),
    );
  }

  /**
   * Income / expense / transfer / net over every row matching the filter, signed by
   * transaction type since amounts are stored as unsigned magnitudes. Computed from up
   * to SUMMARY_PAGE_SIZE rows rather than a dedicated aggregate endpoint - fine for a
   * cash book's realistic volume, and `truncated` tells the caller when that cap was hit
   * so the total can be shown as a lower bound instead of silently wrong.
   */
  summary(filter: CashBookFilter): Observable<CashBookSummary> {
    const params = this.buildParams(filter).set('page', 0).set('size', SUMMARY_PAGE_SIZE);
    return this.http.get<CashBookEntry[]>(this.resourceUrl, { params, observe: 'response' }).pipe(
      map(res => {
        const rows = res.body ?? [];
        const totalRecords = Number(res.headers.get('X-Total-Count') ?? rows.length);
        const summary: CashBookSummary = {
          income: 0,
          expense: 0,
          transfer: 0,
          net: 0,
          count: totalRecords,
          truncated: totalRecords > rows.length,
        };
        for (const r of rows) {
          const amount = r.amount ?? 0;
          const type = (r.transactionType?.name ?? '').toUpperCase();
          if (type === 'INCOME') {
            summary.income += amount;
          } else if (type === 'TRANSFER') {
            summary.transfer += amount;
          } else {
            summary.expense += amount;
          }
        }
        summary.net = summary.income - summary.expense;
        return summary;
      }),
    );
  }

  private buildParams(filter: CashBookFilter): HttpParams {
    let params = new HttpParams();
    if (filter.dateFrom) {
      params = params.set('transactionDate.greaterThanOrEqual', filter.dateFrom);
    }
    if (filter.dateTo) {
      params = params.set('transactionDate.lessThanOrEqual', filter.dateTo);
    }
    if (filter.transactionTypeId != null) {
      params = params.set('transactionTypeId.equals', filter.transactionTypeId);
    }
    if (filter.transactionCategoryId != null) {
      params = params.set('transactionCategoryId.equals', filter.transactionCategoryId);
    }
    if (filter.departmentId != null) {
      params = params.set('departmentId.equals', filter.departmentId);
    }
    if (filter.search) {
      params = params.set('description.contains', filter.search);
    }
    return params;
  }

  find(id: number): Observable<CashBookEntry> {
    return this.http.get<CashBookEntry>(`${this.resourceUrl}/${id}`);
  }

  create(entry: Partial<CashBookEntry>): Observable<CashBookEntry> {
    return this.http.post<CashBookEntry>(this.resourceUrl, entry);
  }

  update(entry: Partial<CashBookEntry> & { id: number }): Observable<CashBookEntry> {
    return this.http.put<CashBookEntry>(`${this.resourceUrl}/${entry.id}`, entry);
  }

  remove(id: number): Observable<unknown> {
    return this.http.delete(`${this.resourceUrl}/${id}`);
  }

  private lookup(path: string): Observable<NamedRef[]> {
    return this.http.get<NamedRef[]>(`${serverApiUrl}api/${path}`, { params: new HttpParams().set('size', 200) });
  }

  transactionTypes(): Observable<NamedRef[]> {
    return this.lookup('transaction-types');
  }

  transactionCategories(): Observable<NamedRef[]> {
    return this.lookup('transaction-categories');
  }

  paymentMethodTypes(): Observable<NamedRef[]> {
    return this.lookup('payment-method-types');
  }

  departments(): Observable<NamedRef[]> {
    return this.lookup('departments');
  }

  currencyUoms(): Observable<NamedRef[]> {
    return this.http
      .get<{ success?: NamedRef[] }>(`${serverApiUrl}api/uoms/currency-measure`)
      .pipe(map(b => (Array.isArray(b.success) ? b.success : [])));
  }
}
