import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { toSignal } from '@angular/core/rxjs-interop';

import { SupplierProduct, SupplierRow, SupplierService } from '../supplier.service';

/**
 * Manage the products a supplier can supply — ported from snp21
 * snp/supplier/manage-products.
 *
 * This screen has never worked in snp21, for two independent reasons:
 *  1. All four endpoints it calls were 404s (the controllers served a doubled path
 *     segment, /api/product-roles/product-roles/...).
 *  2. Its own logic was commented out: newProductsBySupplier() pushed form rows with
 *     every patchValue line commented, so the available-product rows rendered blank;
 *     addProductsToSupplier() never pushed into productIds, so it always posted an
 *     empty array; and allSelect()'s loop was commented out too.
 *
 * Rebuilt here against working endpoints, with the selection logic actually present.
 */
@Component({
  selector: 'sys-manage-products',
  templateUrl: './manage-products.html',
  styleUrl: './manage-products.scss',
  imports: [FormsModule, RouterLink, FaIconComponent, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class ManageProducts {
  readonly suppliers = signal<SupplierRow[]>([]);
  readonly selectedSupplierId = signal<number | null>(null);

  readonly linked = signal<SupplierProduct[]>([]);
  readonly addable = signal<SupplierProduct[]>([]);
  readonly checked = signal<Set<number>>(new Set());

  readonly loadingSuppliers = signal(false);
  readonly loadingProducts = signal(false);
  readonly busy = signal(false);
  readonly error = signal<string | null>(null);
  readonly notice = signal<string | null>(null);
  readonly showAddPanel = signal(false);
  readonly confirmRemove = signal<SupplierProduct | null>(null);

  private readonly supplierService = inject(SupplierService);
  private readonly route = inject(ActivatedRoute);
  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });

  readonly selectedSupplier = computed(() => this.suppliers().find(s => s.id === this.selectedSupplierId()) ?? null);
  readonly checkedCount = computed(() => this.checked().size);
  readonly allChecked = computed(() => this.addable().length > 0 && this.checked().size === this.addable().length);

  constructor() {
    this.loadSuppliers();
  }

  private loadSuppliers(): void {
    this.loadingSuppliers.set(true);
    // one page large enough to populate the picker
    this.supplierService.getAllSuppliersList(0, 500).subscribe({
      next: result => {
        this.suppliers.set(result.rows);
        this.loadingSuppliers.set(false);
        const preset = this.params()['id'];
        if (preset && result.rows.some(r => r.id === Number(preset))) {
          this.selectSupplier(Number(preset));
        }
      },
      error: () => {
        this.error.set('Could not load the supplier list.');
        this.loadingSuppliers.set(false);
      },
    });
  }

  onSupplierChange(value: string): void {
    this.selectSupplier(value ? Number(value) : null);
  }

  selectSupplier(id: number | null): void {
    this.selectedSupplierId.set(id);
    this.checked.set(new Set());
    this.showAddPanel.set(false);
    this.notice.set(null);
    this.linked.set([]);
    this.addable.set([]);
    if (id !== null) {
      this.refresh(id);
    }
  }

  private refresh(id: number): void {
    this.loadingProducts.set(true);
    this.error.set(null);
    this.supplierService.getSupplierProductsById(id).subscribe({
      next: rows => {
        this.linked.set(rows);
        this.loadingProducts.set(false);
      },
      error: () => {
        this.error.set('Could not load this supplier’s products.');
        this.loadingProducts.set(false);
      },
    });
    this.supplierService.getAddableProducts(id).subscribe({
      next: rows => this.addable.set(rows),
      error: () => this.addable.set([]),
    });
  }

  toggleAddPanel(): void {
    this.showAddPanel.update(v => !v);
    this.checked.set(new Set());
  }

  toggleChecked(productId: number): void {
    this.checked.update(set => {
      const next = new Set(set);
      if (next.has(productId)) {
        next.delete(productId);
      } else {
        next.add(productId);
      }
      return next;
    });
  }

  toggleAll(): void {
    this.checked.update(set => (set.size === this.addable().length ? new Set<number>() : new Set(this.addable().map(p => p.id))));
  }

  addSelected(): void {
    const id = this.selectedSupplierId();
    const ids = [...this.checked()];
    if (id === null || ids.length === 0) {
      return;
    }
    this.busy.set(true);
    this.error.set(null);
    this.supplierService.addProductsToSupplier(id, ids).subscribe({
      next: () => {
        this.busy.set(false);
        this.notice.set(`${ids.length} product(s) added.`);
        this.checked.set(new Set());
        this.showAddPanel.set(false);
        this.refresh(id);
      },
      error: () => {
        this.busy.set(false);
        this.error.set('Could not add the selected products.');
      },
    });
  }

  askRemove(product: SupplierProduct): void {
    this.confirmRemove.set(product);
  }

  cancelRemove(): void {
    this.confirmRemove.set(null);
  }

  doRemove(): void {
    const id = this.selectedSupplierId();
    const product = this.confirmRemove();
    if (id === null || !product) {
      return;
    }
    this.busy.set(true);
    this.supplierService.removeProductFromSupplier(id, product.id).subscribe({
      next: () => {
        this.busy.set(false);
        this.confirmRemove.set(null);
        this.notice.set(`Removed ${product.name ?? 'product'}.`);
        this.refresh(id);
      },
      error: () => {
        this.busy.set(false);
        this.confirmRemove.set(null);
        this.error.set('Could not remove that product.');
      },
    });
  }
}
