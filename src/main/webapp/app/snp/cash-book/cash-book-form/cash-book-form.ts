import { ChangeDetectionStrategy, Component, computed, effect, inject, signal } from '@angular/core';
import { LowerCasePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { toSignal } from '@angular/core/rxjs-interop';

import { CashBookService, NamedRef, TransactionTypeName } from '../cash-book.service';
import { CustomerRow, CustomerService } from '../../sales-management/customer.service';
import { SupplierRow, SupplierService } from '../../supplier/supplier.service';

/**
 * Create / edit a cash book entry — rebuilt from snp21 snp/cash-book.
 *
 * snp21's screen could not run: its template bound 24 formControlNames while the form
 * group declared 7, so Angular threw at render, and it posted to /api/cashbook/create
 * which nothing served.
 *
 * The fields here follow the CashBook entity. snp21's template listed the utility
 * expenses (electricity, water, internet, phone, fuel, salaries, stationery, office
 * lease, salik charges, district cooling, miscellaneous) as separate inputs on one form;
 * they are transaction categories on a single entry instead, which is what the schema
 * models and lets each be dated, costed and reported on separately.
 *
 * The category list, the Description label and the Frequency control all adapt to the
 * selected transaction type/Recurring flag rather than staying fixed for every kind of
 * entry - a form for "any transaction" should not read like it was written for expenses
 * only, and an entry that isn't recurring should not carry a stray frequency value.
 *
 * vendorCustomerName stays free text (an ad-hoc payee like a utility company doesn't
 * need to exist as a party record), but INCOME/EXPENSE entries can also link to a real
 * Customer/Supplier via fromParty/toParty - that convention (money received FROM a
 * customer, paid TO a supplier) isn't documented on the entity, it's decided here.
 * TRANSFER gets no picker: neither direction fits an internal movement of funds.
 */
@Component({
  selector: 'sys-cash-book-form',
  templateUrl: './cash-book-form.html',
  styleUrl: './cash-book-form.scss',
  imports: [ReactiveFormsModule, RouterLink, FaIconComponent, LowerCasePipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class CashBookForm {
  readonly saving = signal(false);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  readonly types = signal<NamedRef[]>([]);
  readonly categories = signal<NamedRef[]>([]);
  readonly paymentMethods = signal<NamedRef[]>([]);
  readonly departments = signal<NamedRef[]>([]);
  readonly currencies = signal<NamedRef[]>([]);
  readonly customers = signal<CustomerRow[]>([]);
  readonly suppliers = signal<SupplierRow[]>([]);

  readonly frequencies = ['DAILY', 'WEEKLY', 'FORTNIGHTLY', 'MONTHLY', 'QUARTERLY', 'HALF_YEARLY', 'YEARLY'];

  private readonly fb = inject(FormBuilder);
  private readonly cashBookService = inject(CashBookService);
  private readonly customerService = inject(CustomerService);
  private readonly supplierService = inject(SupplierService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });

  readonly entryId = computed<number | null>(() => {
    const raw = this.params()['id'];
    return raw ? Number(raw) : null;
  });
  readonly isEdit = computed(() => this.params()['edit'] === 'true' && this.entryId() !== null);
  readonly title = computed(() => (this.isEdit() ? 'Edit Cash Book Entry' : 'New Cash Book Entry'));

  readonly form = this.fb.nonNullable.group({
    transactionDate: ['', Validators.required],
    amount: [null as number | null, [Validators.required, Validators.min(0)]],
    transactionTypeId: [null as number | null, Validators.required],
    transactionCategoryId: [null as number | null],
    paymentMethodTypeId: [null as number | null],
    departmentId: [null as number | null],
    currencyUomId: [null as number | null],
    partyId: [null as number | null],
    description: ['', [Validators.maxLength(2000)]],
    purpose: ['', [Validators.maxLength(255)]],
    vendorCustomerName: ['', [Validators.maxLength(200)]],
    invoiceNo: ['', [Validators.maxLength(60)]],
    invoiceDate: [''],
    dueDate: [''],
    project: ['', [Validators.maxLength(255)]],
    usageUnits: ['', [Validators.maxLength(60)]],
    remarks: ['', [Validators.maxLength(2000)]],
    recurringTransaction: [false],
    frequency: ['' as string],
  });

  private readonly typeId = toSignal(this.form.controls.transactionTypeId.valueChanges, { initialValue: null as number | null });
  readonly recurring = toSignal(this.form.controls.recurringTransaction.valueChanges, { initialValue: false });

  /** The selected type's name (EXPENSE/INCOME/TRANSFER), used to adapt labels. */
  readonly selectedTypeName = computed<TransactionTypeName | null>(() => {
    const id = this.typeId() ?? this.form.controls.transactionTypeId.value;
    const type = this.types().find(t => t.id === id);
    return (type?.name as TransactionTypeName | undefined) ?? null;
  });

  /** Categories scoped to the selected type, plus unscoped catch-alls (e.g. Miscellaneous). */
  readonly categoryOptions = computed<NamedRef[]>(() => {
    const id = this.typeId() ?? this.form.controls.transactionTypeId.value;
    const all = this.categories();
    return id == null ? all : all.filter(c => c.transactionType == null || c.transactionType.id === id);
  });

  readonly descriptionLabel = computed(() => {
    switch (this.selectedTypeName()) {
      case 'INCOME':
        return 'Description of the Receipt';
      case 'TRANSFER':
        return 'Description of the Transfer';
      case 'EXPENSE':
        return 'Description of the Expense';
      default:
        return 'Description';
    }
  });

  readonly partyLabel = computed(() => (this.selectedTypeName() === 'INCOME' ? 'Received From' : 'Vendor / Paid To'));

  /** 'CUSTOMER' for INCOME, 'SUPPLIER' for EXPENSE, null for TRANSFER/no type - see the class doc. */
  readonly partyKind = computed<'CUSTOMER' | 'SUPPLIER' | null>(() => {
    switch (this.selectedTypeName()) {
      case 'INCOME':
        return 'CUSTOMER';
      case 'EXPENSE':
        return 'SUPPLIER';
      default:
        return null;
    }
  });

  readonly partyPickerLabel = computed(() => (this.partyKind() === 'CUSTOMER' ? 'Customer' : 'Supplier'));

  readonly partyOptions = computed<NamedRef[]>(() => {
    const kind = this.partyKind();
    if (kind === 'CUSTOMER') {
      return this.customers().map(c => ({ id: c.id, name: c.name }));
    }
    if (kind === 'SUPPLIER') {
      return this.suppliers().map(s => ({ id: s.id, name: s.name }));
    }
    return [];
  });

  private loadedFor: number | null = null;

  constructor() {
    this.cashBookService.transactionTypes().subscribe({ next: r => this.types.set(r), error: () => this.types.set([]) });
    this.cashBookService.transactionCategories().subscribe({ next: r => this.categories.set(r), error: () => this.categories.set([]) });
    this.cashBookService
      .paymentMethodTypes()
      .subscribe({ next: r => this.paymentMethods.set(r), error: () => this.paymentMethods.set([]) });
    this.cashBookService.departments().subscribe({ next: r => this.departments.set(r), error: () => this.departments.set([]) });
    this.cashBookService.currencyUoms().subscribe({ next: r => this.currencies.set(r), error: () => this.currencies.set([]) });
    this.customerService
      .getAllCustomersList(0, 200)
      .subscribe({ next: p => this.customers.set(p.rows), error: () => this.customers.set([]) });
    this.supplierService
      .getAllSuppliersList(0, 200)
      .subscribe({ next: p => this.suppliers.set(p.rows), error: () => this.suppliers.set([]) });
    queueMicrotask(() => this.maybeLoad());

    // A category chosen under one type rarely belongs to another - clear it rather than
    // silently keep a stale selection out of the now-filtered dropdown.
    effect(() => {
      const type = this.typeId();
      const options = this.categoryOptions();
      const current = this.form.controls.transactionCategoryId.value;
      if (current != null && !options.some(c => c.id === current) && this.form.controls.transactionTypeId.dirty) {
        this.form.controls.transactionCategoryId.setValue(null);
      }
      void type;
    });

    // A customer picked under INCOME isn't a valid supplier under EXPENSE (and TRANSFER
    // has no picker at all) - clear it rather than silently submit the wrong party.
    effect(() => {
      const options = this.partyOptions();
      const current = this.form.controls.partyId.value;
      if (current != null && !options.some(p => p.id === current) && this.form.controls.transactionTypeId.dirty) {
        this.form.controls.partyId.setValue(null);
      }
    });

    // Frequency only means something when Recurring is checked; keep it in sync so a
    // one-off entry never carries a stray frequency value in the payload.
    effect(() => {
      const isRecurring = this.recurring();
      const control = this.form.controls.frequency;
      if (isRecurring) {
        if (!control.value) {
          control.setValue('MONTHLY');
        }
        control.enable({ emitEvent: false });
      } else {
        control.setValue('', { emitEvent: false });
        control.disable({ emitEvent: false });
      }
    });
  }

  private maybeLoad(): void {
    const id = this.entryId();
    if (!this.isEdit() || id === null || this.loadedFor === id) {
      return;
    }
    this.loadedFor = id;
    this.loading.set(true);
    this.cashBookService.find(id).subscribe({
      next: e => {
        const typeName = e.transactionType?.name;
        this.form.patchValue({
          transactionDate: e.transactionDate ? String(e.transactionDate).slice(0, 10) : '',
          amount: e.amount ?? null,
          transactionTypeId: e.transactionType?.id ?? null,
          transactionCategoryId: e.transactionCategory?.id ?? null,
          paymentMethodTypeId: e.paymentMethodType?.id ?? null,
          departmentId: e.department?.id ?? null,
          currencyUomId: e.currencyUom?.id ?? null,
          partyId: typeName === 'INCOME' ? (e.fromParty?.id ?? null) : typeName === 'EXPENSE' ? (e.toParty?.id ?? null) : null,
          description: e.description ?? '',
          purpose: e.purpose ?? '',
          vendorCustomerName: e.vendorCustomerName ?? '',
          invoiceNo: e.invoiceNo ?? '',
          invoiceDate: e.invoiceDate ? String(e.invoiceDate).slice(0, 10) : '',
          dueDate: e.dueDate ? String(e.dueDate).slice(0, 10) : '',
          project: e.project ?? '',
          usageUnits: e.usageUnits ?? '',
          remarks: e.remarks ?? '',
          recurringTransaction: e.recurringTransaction ?? false,
          frequency: e.frequency && e.frequency !== 'NONE' ? e.frequency : '',
        });
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load this entry.');
        this.loading.set(false);
      },
    });
  }

  invalid(name: 'transactionDate' | 'amount' | 'transactionTypeId'): boolean {
    const c = this.form.controls[name];
    return c.invalid && (c.dirty || c.touched);
  }

  private ref(id: number | null): { id: number } | null {
    return id == null ? null : { id };
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    const v = this.form.getRawValue();
    const body = {
      transactionDate: v.transactionDate,
      dueDate: v.dueDate || null,
      amount: v.amount,
      description: v.description || null,
      vendorCustomerName: v.vendorCustomerName || null,
      invoiceNo: v.invoiceNo || null,
      invoiceDate: v.invoiceDate || null,
      purpose: v.purpose || null,
      remarks: v.remarks || null,
      project: v.project || null,
      usageUnits: v.usageUnits || null,
      recurringTransaction: v.recurringTransaction,
      frequency: v.frequency || null,
      transactionType: this.ref(v.transactionTypeId),
      transactionCategory: this.ref(v.transactionCategoryId),
      paymentMethodType: this.ref(v.paymentMethodTypeId),
      department: this.ref(v.departmentId),
      currencyUom: this.ref(v.currencyUomId),
      fromParty: this.partyKind() === 'CUSTOMER' ? this.ref(v.partyId) : null,
      toParty: this.partyKind() === 'SUPPLIER' ? this.ref(v.partyId) : null,
    };

    const id = this.entryId();
    const request = this.isEdit() && id !== null ? this.cashBookService.update({ ...body, id }) : this.cashBookService.create(body);

    request.subscribe({
      next: () => {
        this.saving.set(false);
        void this.router.navigate(['/cash-book/cash-book-list']);
      },
      error: () => {
        this.saving.set(false);
        this.error.set(this.isEdit() ? 'Could not update the entry.' : 'Could not create the entry.');
      },
    });
  }

  cancel(): void {
    void this.router.navigate(['/cash-book/cash-book-list']);
  }
}
