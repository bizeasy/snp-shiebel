import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentApplication } from '../payment-application.model';
import { PaymentApplicationService } from '../service/payment-application.service';

import { PaymentApplicationFormGroup, PaymentApplicationFormService } from './payment-application-form.service';

@Component({
  selector: 'sys-payment-application-update',
  templateUrl: './payment-application-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PaymentApplicationUpdate implements OnInit {
  readonly isSaving = signal(false);
  paymentApplication: IPaymentApplication | null = null;

  paymentsSharedCollection = signal<IPayment[]>([]);
  invoicesSharedCollection = signal<IInvoice[]>([]);
  invoiceItemsSharedCollection = signal<IInvoiceItem[]>([]);
  ordersSharedCollection = signal<IOrder[]>([]);
  orderItemsSharedCollection = signal<IOrderItem[]>([]);

  protected paymentApplicationService = inject(PaymentApplicationService);
  protected paymentApplicationFormService = inject(PaymentApplicationFormService);
  protected paymentService = inject(PaymentService);
  protected invoiceService = inject(InvoiceService);
  protected invoiceItemService = inject(InvoiceItemService);
  protected orderService = inject(OrderService);
  protected orderItemService = inject(OrderItemService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentApplicationFormGroup = this.paymentApplicationFormService.createPaymentApplicationFormGroup();

  comparePayment = (o1: IPayment | null, o2: IPayment | null): boolean => this.paymentService.comparePayment(o1, o2);

  compareInvoice = (o1: IInvoice | null, o2: IInvoice | null): boolean => this.invoiceService.compareInvoice(o1, o2);

  compareInvoiceItem = (o1: IInvoiceItem | null, o2: IInvoiceItem | null): boolean => this.invoiceItemService.compareInvoiceItem(o1, o2);

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentApplication }) => {
      this.paymentApplication = paymentApplication;
      if (paymentApplication) {
        this.updateForm(paymentApplication);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const paymentApplication = this.paymentApplicationFormService.getPaymentApplication(this.editForm);
    if (paymentApplication.id === null) {
      this.subscribeToSaveResponse(this.paymentApplicationService.create(paymentApplication));
    } else {
      this.subscribeToSaveResponse(this.paymentApplicationService.update(paymentApplication));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPaymentApplication | null>): void {
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

  protected updateForm(paymentApplication: IPaymentApplication): void {
    this.paymentApplication = paymentApplication;
    this.paymentApplicationFormService.resetForm(this.editForm, paymentApplication);

    this.paymentsSharedCollection.update(payments =>
      this.paymentService.addPaymentToCollectionIfMissing<IPayment>(payments, paymentApplication.payment, paymentApplication.toPayment),
    );
    this.invoicesSharedCollection.update(invoices =>
      this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, paymentApplication.invoice),
    );
    this.invoiceItemsSharedCollection.update(invoiceItems =>
      this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, paymentApplication.invoiceItem),
    );
    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, paymentApplication.order));
    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, paymentApplication.orderItem),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentService
      .query()
      .pipe(map((res: HttpResponse<IPayment[]>) => res.body ?? []))
      .pipe(
        map((payments: IPayment[]) =>
          this.paymentService.addPaymentToCollectionIfMissing<IPayment>(
            payments,
            this.paymentApplication?.payment,
            this.paymentApplication?.toPayment,
          ),
        ),
      )
      .subscribe((payments: IPayment[]) => this.paymentsSharedCollection.set(payments));

    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) =>
          this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, this.paymentApplication?.invoice),
        ),
      )
      .subscribe((invoices: IInvoice[]) => this.invoicesSharedCollection.set(invoices));

    this.invoiceItemService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceItem[]>) => res.body ?? []))
      .pipe(
        map((invoiceItems: IInvoiceItem[]) =>
          this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, this.paymentApplication?.invoiceItem),
        ),
      )
      .subscribe((invoiceItems: IInvoiceItem[]) => this.invoiceItemsSharedCollection.set(invoiceItems));

    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.paymentApplication?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.paymentApplication?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));
  }
}
