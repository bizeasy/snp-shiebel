import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';
import { TransactionCategoryService } from 'app/entities/transaction-category/service/transaction-category.service';
import { ITransactionCategory } from 'app/entities/transaction-category/transaction-category.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { ICashBook } from '../cash-book.model';
import { CashBookService } from '../service/cash-book.service';

import { CashBookFormGroup, CashBookFormService } from './cash-book-form.service';
import { PaymentMethodTypeService } from 'app/entities/payment-method-type/service/payment-method-type.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IUom } from 'app/entities/uom/uom.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { RecurrenceFrequency } from 'app/entities/enumerations/recurrence-frequency.model';

@Component({
  selector: 'sys-cash-book-update',
  templateUrl: './cash-book-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class CashBookUpdate implements OnInit {
  readonly isSaving = signal(false);
  cashBook: ICashBook | null = null;
  recurrenceFrequencyValues = Object.keys(RecurrenceFrequency);

  transactionTypesSharedCollection = signal<ITransactionType[]>([]);
  transactionCategoriesSharedCollection = signal<ITransactionCategory[]>([]);
  paymentMethodTypesSharedCollection = signal<IPaymentMethodType[]>([]);
  departmentsSharedCollection = signal<IDepartment[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  ordersSharedCollection = signal<IOrder[]>([]);
  invoicesSharedCollection = signal<IInvoice[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);

  protected cashBookService = inject(CashBookService);
  protected cashBookFormService = inject(CashBookFormService);
  protected transactionTypeService = inject(TransactionTypeService);
  protected transactionCategoryService = inject(TransactionCategoryService);
  protected paymentMethodTypeService = inject(PaymentMethodTypeService);
  protected departmentService = inject(DepartmentService);
  protected uomService = inject(UomService);
  protected statusService = inject(StatusService);
  protected orderService = inject(OrderService);
  protected invoiceService = inject(InvoiceService);
  protected partyService = inject(PartyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CashBookFormGroup = this.cashBookFormService.createCashBookFormGroup();

  compareTransactionType = (o1: ITransactionType | null, o2: ITransactionType | null): boolean =>
    this.transactionTypeService.compareTransactionType(o1, o2);

  compareTransactionCategory = (o1: ITransactionCategory | null, o2: ITransactionCategory | null): boolean =>
    this.transactionCategoryService.compareTransactionCategory(o1, o2);

  comparePaymentMethodType = (o1: IPaymentMethodType | null, o2: IPaymentMethodType | null): boolean =>
    this.paymentMethodTypeService.comparePaymentMethodType(o1, o2);

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareInvoice = (o1: IInvoice | null, o2: IInvoice | null): boolean => this.invoiceService.compareInvoice(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashBook }) => {
      this.cashBook = cashBook;
      if (cashBook) {
        this.updateForm(cashBook);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const cashBook = this.cashBookFormService.getCashBook(this.editForm);
    if (cashBook.id === null) {
      this.subscribeToSaveResponse(this.cashBookService.create(cashBook));
    } else {
      this.subscribeToSaveResponse(this.cashBookService.update(cashBook));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICashBook | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(cashBook: ICashBook): void {
    this.cashBook = cashBook;
    this.cashBookFormService.resetForm(this.editForm, cashBook);

    this.transactionTypesSharedCollection.update(transactionTypes =>
      this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(transactionTypes, cashBook.transactionType),
    );
    this.transactionCategoriesSharedCollection.update(transactionCategories =>
      this.transactionCategoryService.addTransactionCategoryToCollectionIfMissing<ITransactionCategory>(
        transactionCategories,
        cashBook.transactionCategory,
      ),
    );
    this.paymentMethodTypesSharedCollection.update(paymentMethodTypes =>
      this.paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing<IPaymentMethodType>(
        paymentMethodTypes,
        cashBook.paymentMethodType,
      ),
    );
    this.departmentsSharedCollection.update(departments =>
      this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, cashBook.department),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, cashBook.currencyUom));
    this.statusesSharedCollection.update(statuses => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, cashBook.status));
    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, cashBook.order));
    this.invoicesSharedCollection.update(invoices =>
      this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, cashBook.invoice),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, cashBook.fromParty, cashBook.toParty),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transactionTypeService
      .query()
      .pipe(map((res: HttpResponse<ITransactionType[]>) => res.body ?? []))
      .pipe(
        map((transactionTypes: ITransactionType[]) =>
          this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
            transactionTypes,
            this.cashBook?.transactionType,
          ),
        ),
      )
      .subscribe((transactionTypes: ITransactionType[]) => this.transactionTypesSharedCollection.set(transactionTypes));

    this.transactionCategoryService
      .query()
      .pipe(map((res: HttpResponse<ITransactionCategory[]>) => res.body ?? []))
      .pipe(
        map((transactionCategories: ITransactionCategory[]) =>
          this.transactionCategoryService.addTransactionCategoryToCollectionIfMissing<ITransactionCategory>(
            transactionCategories,
            this.cashBook?.transactionCategory,
          ),
        ),
      )
      .subscribe((transactionCategories: ITransactionCategory[]) => this.transactionCategoriesSharedCollection.set(transactionCategories));

    this.paymentMethodTypeService
      .query()
      .pipe(map((res: HttpResponse<IPaymentMethodType[]>) => res.body ?? []))
      .pipe(
        map((paymentMethodTypes: IPaymentMethodType[]) =>
          this.paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing<IPaymentMethodType>(
            paymentMethodTypes,
            this.cashBook?.paymentMethodType,
          ),
        ),
      )
      .subscribe((paymentMethodTypes: IPaymentMethodType[]) => this.paymentMethodTypesSharedCollection.set(paymentMethodTypes));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.cashBook?.department),
        ),
      )
      .subscribe((departments: IDepartment[]) => this.departmentsSharedCollection.set(departments));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.cashBook?.currencyUom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.cashBook?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.cashBook?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(map((invoices: IInvoice[]) => this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, this.cashBook?.invoice)))
      .subscribe((invoices: IInvoice[]) => this.invoicesSharedCollection.set(invoices));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.cashBook?.fromParty, this.cashBook?.toParty),
        ),
      )
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));
  }
}
