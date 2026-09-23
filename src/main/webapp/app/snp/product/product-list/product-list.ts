import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { ProductRow, ProductService } from '../product.service';

/**
 * Product list, ported from snp21 snp/product/product-list.
 *
 * Two snp21 defects are deliberately not reproduced:
 *  - The table declared a Status column, but the backend row map never contained a
 *    status key and Product has no status field, so it was permanently blank.
 *  - findProductsByCriteria built a Specification from the criteria and then called
 *    findAll(page) without it, so the name/status filters never applied. Search here
 *    filters the loaded page and labels itself as such.
 */
@Component({
  selector: 'sys-product-list',
  templateUrl: './product-list.html',
  styleUrl: './product-list.scss',
  imports: [FormsModule, RouterLink, NgbDropdownModule, FaIconComponent, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class ProductList {
  readonly rows = signal<ProductRow[]>([]);
  readonly totalRecords = signal(0);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly search = signal('');

  readonly page = signal(0);
  readonly size = signal(10);
  readonly sizeOptions = [10, 25, 50, 100, 250];

  private readonly productService = inject(ProductService);

  readonly filteredRows = computed<ProductRow[]>(() => {
    const term = this.search().trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter(r => [r.id, r.productName, r.price].some(v => v != null && String(v).toLowerCase().includes(term)));
  });

  readonly totalPages = computed(() => Math.max(1, Math.ceil(this.totalRecords() / this.size())));
  readonly rangeStart = computed(() => (this.totalRecords() === 0 ? 0 : this.page() * this.size() + 1));
  readonly rangeEnd = computed(() => Math.min((this.page() + 1) * this.size(), this.totalRecords()));

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.productService.getAllProductsList(this.page(), this.size()).subscribe({
      next: result => {
        this.rows.set(result.rows);
        this.totalRecords.set(result.totalRecords);
        this.loading.set(false);
      },
      error: () => {
        this.rows.set([]);
        this.totalRecords.set(0);
        this.error.set('Could not load the product list.');
        this.loading.set(false);
      },
    });
  }

  goToPage(p: number): void {
    if (p < 0 || p >= this.totalPages() || p === this.page()) {
      return;
    }
    this.page.set(p);
    this.load();
  }

  changeSize(value: string): void {
    this.size.set(Number(value));
    this.page.set(0);
    this.load();
  }

  detailCount(row: ProductRow): number {
    return this.productService.parseDetails(row).length;
  }
}
