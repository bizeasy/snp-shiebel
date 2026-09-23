import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { toSignal } from '@angular/core/rxjs-interop';

import { ProdDetail, ProductService } from '../product.service';

/**
 * Create / view a product — ported from snp21 snp/product/create-product.
 *
 * As in snp21 one component serves both: the list's View action routes here with
 * ?view=true. snp21's view never worked because getProductDataById called a path the
 * controller did not serve; that is fixed, and in view mode the form is disabled
 * rather than pretending to be editable (snp21 left the inputs live but had no update
 * endpoint, so edits silently went nowhere).
 */
@Component({
  selector: 'sys-product-form',
  templateUrl: './product-form.html',
  styleUrl: './product-form.scss',
  imports: [ReactiveFormsModule, RouterLink, FaIconComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class ProductForm {
  readonly saving = signal(false);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly productService = inject(ProductService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(100)]],
    price: [null as number | null],
    prodListDTO: this.fb.array([this.newDetail()]),
  });

  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });

  readonly productId = computed<number | null>(() => {
    const raw = this.params()['id'];
    return raw ? Number(raw) : null;
  });

  readonly isView = computed(() => this.params()['view'] === 'true' && this.productId() !== null);
  readonly title = computed(() => (this.isView() ? 'View Product' : 'Create Product'));

  private loadedFor: number | null = null;

  constructor() {
    queueMicrotask(() => this.maybeLoad());
  }

  get details(): FormArray {
    return this.form.controls.prodListDTO as FormArray;
  }

  private newDetail(test = '', specification = '') {
    return this.fb.nonNullable.group({
      test: [test],
      specification: [specification],
    });
  }

  private maybeLoad(): void {
    const id = this.productId();
    if (!this.isView() || id === null || this.loadedFor === id) {
      return;
    }
    this.loadedFor = id;
    this.loading.set(true);
    this.productService.getProductById(id).subscribe({
      next: row => {
        this.form.patchValue({ name: row?.productName ?? '', price: row?.price ?? null });
        const parsed: ProdDetail[] = this.productService.parseDetails(row);
        this.details.clear();
        if (parsed.length === 0) {
          this.details.push(this.newDetail());
        } else {
          parsed.forEach(d => this.details.push(this.newDetail(d.test ?? '', d.specification ?? '')));
        }
        this.form.disable();
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load this product.');
        this.loading.set(false);
      },
    });
  }

  addDetail(): void {
    this.details.push(this.newDetail());
  }

  removeDetail(index: number): void {
    if (this.details.length > 1) {
      this.details.removeAt(index);
    }
  }

  nameInvalid(): boolean {
    const c = this.form.controls.name;
    return c.invalid && (c.dirty || c.touched);
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    const raw = this.form.getRawValue();
    this.productService
      .createProduct({
        name: raw.name,
        price: raw.price,
        prodListDTO: (raw.prodListDTO as ProdDetail[]).filter(d => d.test || d.specification),
      })
      .subscribe({
        next: () => {
          this.saving.set(false);
          void this.router.navigate(['/product/product-list']);
        },
        error: () => {
          this.saving.set(false);
          this.error.set('Could not create the product.');
        },
      });
  }

  cancel(): void {
    void this.router.navigate(['/product/product-list']);
  }
}
