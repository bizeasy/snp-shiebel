import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CdkDrag, CdkDragDrop, CdkDragHandle, CdkDropList } from '@angular/cdk/drag-drop';
import { DecimalPipe } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { looksLikeId } from '../../shared/looks-like-id';
import { TermsInput } from '../../shared/terms-input/terms-input';
import { TermsService } from '../../shared/terms-input/terms.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs/operators';

import { CurrencyUom, OrderDetail, OrderLinePayload, SalesOrderService } from '../sales-order.service';
import { CustomerRow, CustomerService } from '../../sales-management/customer.service';
import { SupplierRow, SupplierService } from '../../supplier/supplier.service';

/**
 * Create / edit a sales or purchase order.
 *
 * Ported from snp21 create-sales-order (641 lines TS + 837 HTML) and
 * create-purchase-order (625 lines), which were near-duplicates. One component serves
 * all four routes; the order type comes from route data.
 *
 * Deliberately not carried over:
 *  - The seven file-attachment slots. They need multipart upload plus a per-slot
 *    ContentType mapping, which is separate work.
 *  - The unreachable helper calls the snp21 form made: /api/parties/agents,
 *    /api/product-facility-users/unbilled, /apiapi/ticket-types, and
 *    /api/order-adjustment-types whose URL literal contained a zero-width space.
 */
@Component({
  selector: 'sys-order-form',
  templateUrl: './order-form.html',
  styleUrl: './order-form.scss',
  imports: [ReactiveFormsModule, RouterLink, FaIconComponent, DecimalPipe, TermsInput, CdkDropList, CdkDrag, CdkDragHandle],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class OrderForm {
  readonly saving = signal(false);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly refNoTaken = signal(false);

  readonly customers = signal<CustomerRow[]>([]);
  readonly suppliers = signal<SupplierRow[]>([]);
  readonly currencies = signal<CurrencyUom[]>([]);

  private readonly fb = inject(FormBuilder);
  private readonly orderService = inject(SalesOrderService);
  private readonly customerService = inject(CustomerService);
  private readonly supplierService = inject(SupplierService);
  private readonly router = inject(Router);
  private readonly termsService = inject(TermsService);

  private readonly route = inject(ActivatedRoute);

  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });
  private readonly routeData = toSignal(this.route.data, { initialValue: {} as Record<string, unknown> });

  readonly kind = computed<'sales' | 'purchase'>(() => (this.routeData()['orderKind'] === 'purchase' ? 'purchase' : 'sales'));

  readonly orderId = computed<number | null>(() => {
    const raw = this.params()['id'];
    return raw ? Number(raw) : null;
  });

  /**
   * Revise: snp21's "Recreate SO". Despite its title reading "Create Sales Order", it set
   * pageType to updatePage and called updateSalesOrder with version + 1 - so it produces a
   * new version of the same order, not a new order. Same behaviour here, honestly labelled.
   */
  readonly isRecreate = computed(() => this.params()['recreate'] === 'true' && this.orderId() !== null);

  /** Copy an existing order into a brand new one, used by "Create PO" on the sales list. */
  readonly fromOrderId = computed<number | null>(() => {
    const raw = this.params()['fromOrder'];
    const n = Number(raw);
    return raw && Number.isFinite(n) && n > 0 ? n : null;
  });

  readonly isEdit = computed(
    () => (this.params()['edit'] === 'true' || this.params()['update'] === 'true' || this.isRecreate()) && this.orderId() !== null,
  );

  readonly title = computed(() => {
    const noun = this.kind() === 'purchase' ? 'Purchase Order' : 'Sales Order';
    if (this.isRecreate()) {
      return 'Revise ' + noun + (this.nextVersion() ? ' (v' + this.nextVersion() + ')' : '');
    }
    if (this.fromOrderId()) {
      return 'Create ' + noun + ' from Sales Order';
    }
    return this.isEdit() ? 'Edit ' + noun : 'Create ' + noun;
  });

  readonly listRoute = computed(() =>
    this.kind() === 'purchase' ? '/purchase-order/purchase-order-list' : '/sales-order/sales-order-list',
  );

  readonly form = this.fb.nonNullable.group({
    existingPartyId: [null as number | null, Validators.required],
    supplierId: [null as number | null],
    projectName: ['', [Validators.required, Validators.maxLength(255)]],
    refNo: ['', [Validators.maxLength(25)]],
    customerOrderRef: ['', [Validators.maxLength(60)]],
    factoryRef: ['', [Validators.maxLength(255)]],
    deliveryDate: [''],
    poType: [''],
    currencyId: [null as number | null],
    paymentTerms: [''],
    deliveryTerms: [''],
    remarks: [''],
    productDetailsList: this.fb.array([this.newLine(1)]),
  });
  /** Same hint as the terms boxes: snp21 left id-like numbers in this column too. */
  readonly factoryRefLooksLikeId = toSignal(this.form.controls.factoryRef.valueChanges.pipe(map(v => looksLikeId(v))), {
    initialValue: false,
  });

  /**
   * Plain method, not a computed().
   *
   * A computed() reading form.controls.value has no signal dependency, so it evaluates
   * once and caches forever — the total sat at 0.00 while the line totals updated.
   * Reading it from the template re-evaluates on each change-detection pass, which the
   * input events already trigger.
   */
  grandTotal(): number {
    const raw = this.form.controls.productDetailsList.value as { qty: number | null; unitPrice: number | null }[];
    return raw.reduce((sum, l) => sum + (l.qty ?? 0) * (l.unitPrice ?? 0), 0);
  }

  /** The version the loaded order already has, so an edit can round-trip it. */
  readonly loadedVersion = signal<number | null>(null);

  /** Set when the form was seeded from another order, so the screen can say so. */
  readonly copiedFrom = signal<{ id: number; refNo: string } | null>(null);

  readonly nextVersion = computed(() => {
    const v = this.loadedVersion();
    return v === null ? null : v + 1;
  });

  private loadedFor: number | null = null;

  constructor() {
    this.customerService.getAllCustomersList(0, 500).subscribe({
      next: r => this.customers.set(r.rows),
      error: () => this.customers.set([]),
    });
    this.supplierService.getAllSuppliersList(0, 500).subscribe({
      next: r => this.suppliers.set(r.rows),
      error: () => this.suppliers.set([]),
    });
    this.orderService.getCurrencyUoms().subscribe({
      next: r => this.currencies.set(r),
      error: () => this.currencies.set([]),
    });
    queueMicrotask(() => this.init());
  }

  get lines(): FormArray {
    return this.form.controls.productDetailsList as FormArray;
  }

  private newLine(serialNo: number) {
    return this.fb.nonNullable.group({
      // Null for a line the user just added; carried back for a line that already exists so
      // the server updates it in place and its invoice billings survive the edit.
      orderItemId: [null as number | null],
      serialNo: [serialNo],
      tagNo: ['', Validators.required],
      description: ['', Validators.required],
      flange: [''],
      actuatorMotorDuty: [''],
      actuatorControlVoltage: [''],
      actuatorFailCondition: [''],
      remoteOperation: [''],
      qty: [null as number | null, [Validators.required, Validators.min(0.0001)]],
      unitPrice: [null as number | null, [Validators.required, Validators.min(0)]],
    });
  }

  private init(): void {
    const id = this.orderId();
    if (this.isEdit() && id !== null && this.loadedFor !== id) {
      this.loadedFor = id;
      this.loadExisting(id);
      return;
    }

    // Create PO from a sales order: load the source for its customer, terms and lines, then
    // strip everything that has to be unique to the new order.
    const from = this.fromOrderId();
    if (!this.isEdit() && from !== null && this.loadedFor !== from) {
      this.loadedFor = from;
      this.orderService.getSalesOrderById(from).subscribe({
        next: d => {
          this.applyDetail(d);
          this.copiedFrom.set({ id: from, refNo: d.refNo ?? String(from) });
          this.loadedVersion.set(null);
          this.form.patchValue({ refNo: '' }, { emitEvent: false });
          this.lines.controls.forEach(c => c.patchValue({ orderItemId: null }, { emitEvent: false }));
          this.orderService.generateOrderNo(this.kind()).subscribe({
            next: ref => this.form.patchValue({ refNo: ref.trim() }),
            error: () => undefined,
          });
        },
        error: () => this.error.set('Could not load the order to copy from.'),
      });
      return;
    }

    if (!this.isEdit() && from === null) {
      this.orderService.generateOrderNo(this.kind()).subscribe({
        next: ref => this.form.patchValue({ refNo: ref.trim() }),
        error: () => undefined,
      });
    }
  }

  private loadExisting(id: number): void {
    this.loading.set(true);
    const load = this.kind() === 'purchase' ? this.orderService.getPurchaseOrderById(id) : this.orderService.getSalesOrderById(id);
    load.subscribe({
      next: d => {
        this.applyDetail(d);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load this order.');
        this.loading.set(false);
      },
    });
  }

  /** Fill the form from an order detail. Shared by edit, revise and copy-to-new. */
  private applyDetail(d: OrderDetail): void {
    this.loadedVersion.set(d.version ?? null);
    this.form.patchValue({
      existingPartyId: d.customerDetails?.id ?? null,
      supplierId: d.supplierDetails?.id ?? null,
      projectName: d.projectName ?? d.name ?? '',
      refNo: d.refNo ?? '',
      customerOrderRef: d.customerOrderRef ?? '',
      factoryRef: d.factoryRef ?? '',
      deliveryDate: d.deliveryDate ? String(d.deliveryDate).slice(0, 10) : '',
      poType: d.poType ?? '',
      currencyId: d.termsReq?.currencyId ?? null,
      paymentTerms: d.termsReq?.paymentTerms ?? '',
      deliveryTerms: d.termsReq?.deliveryTerms ?? '',
      remarks: d.termsReq?.remarks ?? '',
    });
    this.lines.clear();
    const items = d.productDetailesList ?? [];
    if (items.length === 0) {
      this.lines.push(this.newLine(1));
    } else {
      items.forEach((it, idx) => {
        const g = this.newLine(it.serialNo ?? idx + 1);
        g.patchValue({
          orderItemId: it.orderItemId ?? null,
          tagNo: it.tagNo ?? '',
          description: it.description ?? '',
          flange: it.flange ?? '',
          actuatorMotorDuty: it.actuatorMotorDuty ?? '',
          actuatorControlVoltage: it.actuatorControlVoltage ?? '',
          actuatorFailCondition: it.actuatorFailCondition ?? '',
          qty: it.qty ?? null,
          unitPrice: it.unitPrice ?? null,
        });
        this.lines.push(g);
      });
    }
  }

  addLine(): void {
    this.lines.push(this.newLine(this.lines.length + 1));
  }

  removeLine(index: number): void {
    if (this.lines.length > 1) {
      this.lines.removeAt(index);
      this.renumber();
    }
  }

  /**
   * Copy a line and drop the copy in directly below it.
   *
   * orderItemId is deliberately NOT copied. It identifies an existing order line, and the
   * server matches submitted lines to order lines by that id; two lines carrying the same
   * one would both bind to a single order item, so the copy has to look new.
   */
  duplicateLine(index: number): void {
    const source = this.lines.at(index).getRawValue() as Record<string, unknown>;
    const copy = this.newLine(0);
    copy.patchValue({ ...source, orderItemId: null }, { emitEvent: false });
    this.lines.insert(index + 1, copy);
    this.renumber();
  }

  /** snp21 called this "Copy to next row": duplicate the last line and append it. */
  duplicateLastLine(): void {
    if (this.lines.length > 0) {
      this.duplicateLine(this.lines.length - 1);
    }
  }

  moveLine(index: number, delta: number): void {
    const target = index + delta;
    if (target < 0 || target >= this.lines.length) {
      return;
    }
    const control = this.lines.at(index);
    this.lines.removeAt(index);
    this.lines.insert(target, control);
    this.renumber();
  }

  /** Drag handler. Uses the FormArray API rather than splicing controls, so value follows. */
  dropLine(event: CdkDragDrop<unknown>): void {
    if (event.previousIndex === event.currentIndex) {
      return;
    }
    const control = this.lines.at(event.previousIndex);
    this.lines.removeAt(event.previousIndex);
    this.lines.insert(event.currentIndex, control);
    this.renumber();
  }

  /**
   * snp21's "Rearrange Data": order the lines by the serial number typed into them.
   *
   * It sorted by the typed value and left those values untouched, so two lines numbered 3
   * stayed numbered 3. Here the sort is stable on the current position for ties, and the
   * serials are rewritten afterwards so the column always reads 1..n.
   */
  sortLinesBySerial(): void {
    const controls = this.lines.controls.map((c, i) => ({ c, i }));
    controls.sort((a, b) => {
      const av = Number(a.c.get('serialNo')?.value) || 0;
      const bv = Number(b.c.get('serialNo')?.value) || 0;
      return av - bv || a.i - b.i;
    });
    this.lines.clear({ emitEvent: false });
    controls.forEach(({ c }) => this.lines.push(c, { emitEvent: false }));
    this.renumber();
  }

  /** Resequence the serial column to 1..n in the order the rows are now in. */
  renumber(): void {
    this.lines.controls.forEach((c, i) => c.patchValue({ serialNo: i + 1 }, { emitEvent: false }));
    this.lines.updateValueAndValidity();
  }

  lineTotal(index: number): number {
    const v = this.lines.at(index).value as { qty: number | null; unitPrice: number | null };
    return (v.qty ?? 0) * (v.unitPrice ?? 0);
  }

  invalid(name: 'existingPartyId' | 'projectName' | 'refNo'): boolean {
    const c = this.form.controls[name];
    return c.invalid && (c.dirty || c.touched);
  }

  lineInvalid(index: number, field: 'tagNo' | 'description' | 'qty' | 'unitPrice'): boolean {
    const c = this.lines.at(index).get(field);
    return !!c && c.invalid && (c.dirty || c.touched);
  }

  checkRefNo(): void {
    const ref = this.form.controls.refNo.value?.trim();
    if (!ref || this.isEdit()) {
      this.refNoTaken.set(false);
      return;
    }
    this.orderService.isRefNoAvailable(ref).subscribe({
      next: free => this.refNoTaken.set(!free),
      error: () => this.refNoTaken.set(false),
    });
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    if (this.refNoTaken()) {
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    const v = this.form.getRawValue();
    const payload = {
      existingCustomer: 'true',
      existingPartyId: v.existingPartyId,
      supplierId: v.supplierId,
      projectName: v.projectName,
      refNo: v.refNo,
      customerOrderRef: v.customerOrderRef,
      factoryRef: v.factoryRef,
      deliveryDate: v.deliveryDate,
      poType: v.poType,
      version: this.isRecreate() ? this.nextVersion() : this.loadedVersion(),
      termsReq: {
        currencyId: v.currencyId,
        paymentTerms: v.paymentTerms,
        deliveryTerms: v.deliveryTerms,
        remarks: v.remarks,
      },
      productDetailsList: v.productDetailsList as OrderLinePayload[],
    };

    const id = this.orderId();
    const purchase = this.kind() === 'purchase';
    let request;
    if (this.isEdit() && id !== null) {
      request = purchase ? this.orderService.editPurchaseOrder(id, payload) : this.orderService.editSalesOrder(id, payload);
    } else {
      request = purchase ? this.orderService.createPurchaseOrder(payload) : this.orderService.createSalesOrder(payload);
    }

    request.subscribe({
      next: () => {
        this.saving.set(false);
        // Terms just saved should appear as a suggestion on the next order.
        this.termsService.invalidate();
        void this.router.navigate([this.listRoute()]);
      },
      error: (err: { error?: { error?: string } }) => {
        this.saving.set(false);
        this.error.set(err?.error?.error ?? 'Could not save the order.');
      },
    });
  }

  cancel(): void {
    void this.router.navigate([this.listRoute()]);
  }
}
