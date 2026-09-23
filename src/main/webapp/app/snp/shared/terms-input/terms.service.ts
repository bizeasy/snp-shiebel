import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Observable, of, tap } from 'rxjs';

import { serverApiUrl } from 'app/config';

export interface TermsSuggestions {
  paymentTerms: string[];
  deliveryTerms: string[];
}

/**
 * Payment and delivery terms already used on real orders.
 *
 * Cached for the session: the list changes only when someone saves an order, and every
 * order and invoice form would otherwise fetch the same thing.
 */
@Injectable({ providedIn: 'root' })
export class TermsService {
  private readonly http = inject(HttpClient);
  private readonly cache = signal<TermsSuggestions | null>(null);

  suggestions(): Observable<TermsSuggestions> {
    const cached = this.cache();
    if (cached) {
      return of(cached);
    }
    return this.http.get<TermsSuggestions>(`${serverApiUrl}api/orders/terms/suggestions`).pipe(tap(value => this.cache.set(value)));
  }

  /** Called after a save, so a newly typed term shows up next time. */
  invalidate(): void {
    this.cache.set(null);
  }
}
