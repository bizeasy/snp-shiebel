import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';
import { PaymentMethodTypeService } from 'app/entities/payment-method-type/service/payment-method-type.service';
import { TransactionCategoryService } from 'app/entities/transaction-category/service/transaction-category.service';
import { ITransactionCategory } from 'app/entities/transaction-category/transaction-category.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { IUom } from 'app/entities/uom/uom.model';
import { ICashBook } from '../cash-book.model';
import { CashBookService } from '../service/cash-book.service';

import { CashBookFormService } from './cash-book-form.service';
import { CashBookUpdate } from './cash-book-update';

describe('CashBook Management Update Component', () => {
  let comp: CashBookUpdate;
  let fixture: ComponentFixture<CashBookUpdate>;
  let activatedRoute: ActivatedRoute;
  let cashBookFormService: CashBookFormService;
  let cashBookService: CashBookService;
  let transactionTypeService: TransactionTypeService;
  let transactionCategoryService: TransactionCategoryService;
  let paymentMethodTypeService: PaymentMethodTypeService;
  let departmentService: DepartmentService;
  let uomService: UomService;
  let statusService: StatusService;
  let orderService: OrderService;
  let invoiceService: InvoiceService;
  let partyService: PartyService;

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

    fixture = TestBed.createComponent(CashBookUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cashBookFormService = TestBed.inject(CashBookFormService);
    cashBookService = TestBed.inject(CashBookService);
    transactionTypeService = TestBed.inject(TransactionTypeService);
    transactionCategoryService = TestBed.inject(TransactionCategoryService);
    paymentMethodTypeService = TestBed.inject(PaymentMethodTypeService);
    departmentService = TestBed.inject(DepartmentService);
    uomService = TestBed.inject(UomService);
    statusService = TestBed.inject(StatusService);
    orderService = TestBed.inject(OrderService);
    invoiceService = TestBed.inject(InvoiceService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TransactionType query and add missing value', () => {
      const cashBook: ICashBook = { id: 26481 };
      const transactionType: ITransactionType = { id: 4045 };
      cashBook.transactionType = transactionType;

      const transactionTypeCollection: ITransactionType[] = [{ id: 4045 }];
      vi.spyOn(transactionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionTypeCollection })));
      const additionalTransactionTypes = [transactionType];
      const expectedCollection: ITransactionType[] = [...additionalTransactionTypes, ...transactionTypeCollection];
      vi.spyOn(transactionTypeService, 'addTransactionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(transactionTypeService.query).toHaveBeenCalled();
      expect(transactionTypeService.addTransactionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        transactionTypeCollection,
        ...additionalTransactionTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.transactionTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TransactionCategory query and add missing value', () => {
      const cashBook: ICashBook = { id: 26481 };
      const transactionCategory: ITransactionCategory = { id: 23111 };
      cashBook.transactionCategory = transactionCategory;

      const transactionCategoryCollection: ITransactionCategory[] = [{ id: 23111 }];
      vi.spyOn(transactionCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionCategoryCollection })));
      const additionalTransactionCategories = [transactionCategory];
      const expectedCollection: ITransactionCategory[] = [...additionalTransactionCategories, ...transactionCategoryCollection];
      vi.spyOn(transactionCategoryService, 'addTransactionCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(transactionCategoryService.query).toHaveBeenCalled();
      expect(transactionCategoryService.addTransactionCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        transactionCategoryCollection,
        ...additionalTransactionCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.transactionCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PaymentMethodType query and add missing value', () => {
      const cashBook: ICashBook = { id: 26481 };
      const paymentMethodType: IPaymentMethodType = { id: 6662 };
      cashBook.paymentMethodType = paymentMethodType;

      const paymentMethodTypeCollection: IPaymentMethodType[] = [{ id: 6662 }];
      vi.spyOn(paymentMethodTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentMethodTypeCollection })));
      const additionalPaymentMethodTypes = [paymentMethodType];
      const expectedCollection: IPaymentMethodType[] = [...additionalPaymentMethodTypes, ...paymentMethodTypeCollection];
      vi.spyOn(paymentMethodTypeService, 'addPaymentMethodTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(paymentMethodTypeService.query).toHaveBeenCalled();
      expect(paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing).toHaveBeenCalledWith(
        paymentMethodTypeCollection,
        ...additionalPaymentMethodTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.paymentMethodTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Department query and add missing value', () => {
      const cashBook: ICashBook = { id: 26481 };
      const department: IDepartment = { id: 29518 };
      cashBook.department = department;

      const departmentCollection: IDepartment[] = [{ id: 29518 }];
      vi.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      vi.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.departmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const cashBook: ICashBook = { id: 26481 };
      const currencyUom: IUom = { id: 6576 };
      cashBook.currencyUom = currencyUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [currencyUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const cashBook: ICashBook = { id: 26481 };
      const status: IStatus = { id: 2927 };
      cashBook.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Order query and add missing value', () => {
      const cashBook: ICashBook = { id: 26481 };
      const order: IOrder = { id: 14644 };
      cashBook.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Invoice query and add missing value', () => {
      const cashBook: ICashBook = { id: 26481 };
      const invoice: IInvoice = { id: 1362 };
      cashBook.invoice = invoice;

      const invoiceCollection: IInvoice[] = [{ id: 1362 }];
      vi.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
      const additionalInvoices = [invoice];
      const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
      vi.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(invoiceService.query).toHaveBeenCalled();
      expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceCollection,
        ...additionalInvoices.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoicesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const cashBook: ICashBook = { id: 26481 };
      const fromParty: IParty = { id: 24527 };
      cashBook.fromParty = fromParty;
      const toParty: IParty = { id: 24527 };
      cashBook.toParty = toParty;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [fromParty, toParty];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const cashBook: ICashBook = { id: 26481 };
      const transactionType: ITransactionType = { id: 4045 };
      cashBook.transactionType = transactionType;
      const transactionCategory: ITransactionCategory = { id: 23111 };
      cashBook.transactionCategory = transactionCategory;
      const paymentMethodType: IPaymentMethodType = { id: 6662 };
      cashBook.paymentMethodType = paymentMethodType;
      const department: IDepartment = { id: 29518 };
      cashBook.department = department;
      const currencyUom: IUom = { id: 6576 };
      cashBook.currencyUom = currencyUom;
      const status: IStatus = { id: 2927 };
      cashBook.status = status;
      const order: IOrder = { id: 14644 };
      cashBook.order = order;
      const invoice: IInvoice = { id: 1362 };
      cashBook.invoice = invoice;
      const fromParty: IParty = { id: 24527 };
      cashBook.fromParty = fromParty;
      const toParty: IParty = { id: 24527 };
      cashBook.toParty = toParty;

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(comp.transactionTypesSharedCollection()).toContainEqual(transactionType);
      expect(comp.transactionCategoriesSharedCollection()).toContainEqual(transactionCategory);
      expect(comp.paymentMethodTypesSharedCollection()).toContainEqual(paymentMethodType);
      expect(comp.departmentsSharedCollection()).toContainEqual(department);
      expect(comp.uomsSharedCollection()).toContainEqual(currencyUom);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.invoicesSharedCollection()).toContainEqual(invoice);
      expect(comp.partiesSharedCollection()).toContainEqual(fromParty);
      expect(comp.partiesSharedCollection()).toContainEqual(toParty);
      expect(comp.cashBook).toEqual(cashBook);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICashBook>();
      const cashBook = { id: 22220 };
      vi.spyOn(cashBookFormService, 'getCashBook').mockReturnValue(cashBook);
      vi.spyOn(cashBookService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cashBook);
      saveSubject.complete();

      // THEN
      expect(cashBookFormService.getCashBook).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cashBookService.update).toHaveBeenCalledWith(expect.objectContaining(cashBook));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICashBook>();
      const cashBook = { id: 22220 };
      vi.spyOn(cashBookFormService, 'getCashBook').mockReturnValue({ id: null });
      vi.spyOn(cashBookService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBook: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cashBook);
      saveSubject.complete();

      // THEN
      expect(cashBookFormService.getCashBook).toHaveBeenCalled();
      expect(cashBookService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICashBook>();
      const cashBook = { id: 22220 };
      vi.spyOn(cashBookService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cashBookService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTransactionType', () => {
      it('should forward to transactionTypeService', () => {
        const entity = { id: 4045 };
        const entity2 = { id: 32159 };
        vi.spyOn(transactionTypeService, 'compareTransactionType');
        comp.compareTransactionType(entity, entity2);
        expect(transactionTypeService.compareTransactionType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTransactionCategory', () => {
      it('should forward to transactionCategoryService', () => {
        const entity = { id: 23111 };
        const entity2 = { id: 5448 };
        vi.spyOn(transactionCategoryService, 'compareTransactionCategory');
        comp.compareTransactionCategory(entity, entity2);
        expect(transactionCategoryService.compareTransactionCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePaymentMethodType', () => {
      it('should forward to paymentMethodTypeService', () => {
        const entity = { id: 6662 };
        const entity2 = { id: 13122 };
        vi.spyOn(paymentMethodTypeService, 'comparePaymentMethodType');
        comp.comparePaymentMethodType(entity, entity2);
        expect(paymentMethodTypeService.comparePaymentMethodType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepartment', () => {
      it('should forward to departmentService', () => {
        const entity = { id: 29518 };
        const entity2 = { id: 15970 };
        vi.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUom', () => {
      it('should forward to uomService', () => {
        const entity = { id: 6576 };
        const entity2 = { id: 20168 };
        vi.spyOn(uomService, 'compareUom');
        comp.compareUom(entity, entity2);
        expect(uomService.compareUom).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStatus', () => {
      it('should forward to statusService', () => {
        const entity = { id: 2927 };
        const entity2 = { id: 22202 };
        vi.spyOn(statusService, 'compareStatus');
        comp.compareStatus(entity, entity2);
        expect(statusService.compareStatus).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareInvoice', () => {
      it('should forward to invoiceService', () => {
        const entity = { id: 1362 };
        const entity2 = { id: 1801 };
        vi.spyOn(invoiceService, 'compareInvoice');
        comp.compareInvoice(entity, entity2);
        expect(invoiceService.compareInvoice).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
