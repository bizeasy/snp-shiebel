import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

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
import { IPaymentApplication } from '../payment-application.model';
import { PaymentApplicationService } from '../service/payment-application.service';

import { PaymentApplicationFormService } from './payment-application-form.service';
import { PaymentApplicationUpdate } from './payment-application-update';

describe('PaymentApplication Management Update Component', () => {
  let comp: PaymentApplicationUpdate;
  let fixture: ComponentFixture<PaymentApplicationUpdate>;
  let activatedRoute: ActivatedRoute;
  let paymentApplicationFormService: PaymentApplicationFormService;
  let paymentApplicationService: PaymentApplicationService;
  let paymentService: PaymentService;
  let invoiceService: InvoiceService;
  let invoiceItemService: InvoiceItemService;
  let orderService: OrderService;
  let orderItemService: OrderItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(PaymentApplicationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentApplicationFormService = TestBed.inject(PaymentApplicationFormService);
    paymentApplicationService = TestBed.inject(PaymentApplicationService);
    paymentService = TestBed.inject(PaymentService);
    invoiceService = TestBed.inject(InvoiceService);
    invoiceItemService = TestBed.inject(InvoiceItemService);
    orderService = TestBed.inject(OrderService);
    orderItemService = TestBed.inject(OrderItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Payment query and add missing value', () => {
      const paymentApplication: IPaymentApplication = { id: 18400 };
      const payment: IPayment = { id: 20208 };
      paymentApplication.payment = payment;
      const toPayment: IPayment = { id: 20208 };
      paymentApplication.toPayment = toPayment;

      const paymentCollection: IPayment[] = [{ id: 20208 }];
      vi.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCollection })));
      const additionalPayments = [payment, toPayment];
      const expectedCollection: IPayment[] = [...additionalPayments, ...paymentCollection];
      vi.spyOn(paymentService, 'addPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentApplication });
      comp.ngOnInit();

      expect(paymentService.query).toHaveBeenCalled();
      expect(paymentService.addPaymentToCollectionIfMissing).toHaveBeenCalledWith(
        paymentCollection,
        ...additionalPayments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.paymentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Invoice query and add missing value', () => {
      const paymentApplication: IPaymentApplication = { id: 18400 };
      const invoice: IInvoice = { id: 1362 };
      paymentApplication.invoice = invoice;

      const invoiceCollection: IInvoice[] = [{ id: 1362 }];
      vi.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
      const additionalInvoices = [invoice];
      const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
      vi.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentApplication });
      comp.ngOnInit();

      expect(invoiceService.query).toHaveBeenCalled();
      expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceCollection,
        ...additionalInvoices.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoicesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InvoiceItem query and add missing value', () => {
      const paymentApplication: IPaymentApplication = { id: 18400 };
      const invoiceItem: IInvoiceItem = { id: 29231 };
      paymentApplication.invoiceItem = invoiceItem;

      const invoiceItemCollection: IInvoiceItem[] = [{ id: 29231 }];
      vi.spyOn(invoiceItemService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceItemCollection })));
      const additionalInvoiceItems = [invoiceItem];
      const expectedCollection: IInvoiceItem[] = [...additionalInvoiceItems, ...invoiceItemCollection];
      vi.spyOn(invoiceItemService, 'addInvoiceItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentApplication });
      comp.ngOnInit();

      expect(invoiceItemService.query).toHaveBeenCalled();
      expect(invoiceItemService.addInvoiceItemToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceItemCollection,
        ...additionalInvoiceItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoiceItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Order query and add missing value', () => {
      const paymentApplication: IPaymentApplication = { id: 18400 };
      const order: IOrder = { id: 14644 };
      paymentApplication.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentApplication });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderItem query and add missing value', () => {
      const paymentApplication: IPaymentApplication = { id: 18400 };
      const orderItem: IOrderItem = { id: 25971 };
      paymentApplication.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentApplication });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const paymentApplication: IPaymentApplication = { id: 18400 };
      const payment: IPayment = { id: 20208 };
      paymentApplication.payment = payment;
      const toPayment: IPayment = { id: 20208 };
      paymentApplication.toPayment = toPayment;
      const invoice: IInvoice = { id: 1362 };
      paymentApplication.invoice = invoice;
      const invoiceItem: IInvoiceItem = { id: 29231 };
      paymentApplication.invoiceItem = invoiceItem;
      const order: IOrder = { id: 14644 };
      paymentApplication.order = order;
      const orderItem: IOrderItem = { id: 25971 };
      paymentApplication.orderItem = orderItem;

      activatedRoute.data = of({ paymentApplication });
      comp.ngOnInit();

      expect(comp.paymentsSharedCollection()).toContainEqual(payment);
      expect(comp.paymentsSharedCollection()).toContainEqual(toPayment);
      expect(comp.invoicesSharedCollection()).toContainEqual(invoice);
      expect(comp.invoiceItemsSharedCollection()).toContainEqual(invoiceItem);
      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.paymentApplication).toEqual(paymentApplication);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentApplication>();
      const paymentApplication = { id: 29590 };
      vi.spyOn(paymentApplicationFormService, 'getPaymentApplication').mockReturnValue(paymentApplication);
      vi.spyOn(paymentApplicationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentApplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentApplication);
      saveSubject.complete();

      // THEN
      expect(paymentApplicationFormService.getPaymentApplication).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentApplicationService.update).toHaveBeenCalledWith(expect.objectContaining(paymentApplication));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentApplication>();
      const paymentApplication = { id: 29590 };
      vi.spyOn(paymentApplicationFormService, 'getPaymentApplication').mockReturnValue({ id: null });
      vi.spyOn(paymentApplicationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentApplication: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentApplication);
      saveSubject.complete();

      // THEN
      expect(paymentApplicationFormService.getPaymentApplication).toHaveBeenCalled();
      expect(paymentApplicationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentApplication>();
      const paymentApplication = { id: 29590 };
      vi.spyOn(paymentApplicationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentApplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentApplicationService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePayment', () => {
      it('should forward to paymentService', () => {
        const entity = { id: 20208 };
        const entity2 = { id: 31232 };
        vi.spyOn(paymentService, 'comparePayment');
        comp.comparePayment(entity, entity2);
        expect(paymentService.comparePayment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInvoice', () => {
      it('should forward to invoiceService', () => {
        const entity = { id: 1362 };
        const entity2 = { id: 1801 };
        vi.spyOn(invoiceService, 'compareInvoice');
        comp.compareInvoice(entity, entity2);
        expect(invoiceService.compareInvoice).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInvoiceItem', () => {
      it('should forward to invoiceItemService', () => {
        const entity = { id: 29231 };
        const entity2 = { id: 28999 };
        vi.spyOn(invoiceItemService, 'compareInvoiceItem');
        comp.compareInvoiceItem(entity, entity2);
        expect(invoiceItemService.compareInvoiceItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrder', () => {
      it('should forward to orderService', () => {
        const entity = { id: 14644 };
        const entity2 = { id: 4253 };
        vi.spyOn(orderService, 'compareOrder');
        comp.compareOrder(entity, entity2);
        expect(orderService.compareOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrderItem', () => {
      it('should forward to orderItemService', () => {
        const entity = { id: 25971 };
        const entity2 = { id: 123 };
        vi.spyOn(orderItemService, 'compareOrderItem');
        comp.compareOrderItem(entity, entity2);
        expect(orderItemService.compareOrderItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
