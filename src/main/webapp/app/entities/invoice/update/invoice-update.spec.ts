import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IInvoiceType } from 'app/entities/invoice-type/invoice-type.model';
import { InvoiceTypeService } from 'app/entities/invoice-type/service/invoice-type.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { IInvoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';

import { InvoiceFormService } from './invoice-form.service';
import { InvoiceUpdate } from './invoice-update';

describe('Invoice Management Update Component', () => {
  let comp: InvoiceUpdate;
  let fixture: ComponentFixture<InvoiceUpdate>;
  let activatedRoute: ActivatedRoute;
  let invoiceFormService: InvoiceFormService;
  let invoiceService: InvoiceService;
  let invoiceTypeService: InvoiceTypeService;
  let orderService: OrderService;
  let partyService: PartyService;
  let roleTypeService: RoleTypeService;
  let statusService: StatusService;
  let contactMechService: ContactMechService;
  let uomService: UomService;

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

    fixture = TestBed.createComponent(InvoiceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceFormService = TestBed.inject(InvoiceFormService);
    invoiceService = TestBed.inject(InvoiceService);
    invoiceTypeService = TestBed.inject(InvoiceTypeService);
    orderService = TestBed.inject(OrderService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);
    statusService = TestBed.inject(StatusService);
    contactMechService = TestBed.inject(ContactMechService);
    uomService = TestBed.inject(UomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call InvoiceType query and add missing value', () => {
      const invoice: IInvoice = { id: 1801 };
      const invoiceType: IInvoiceType = { id: 15913 };
      invoice.invoiceType = invoiceType;

      const invoiceTypeCollection: IInvoiceType[] = [{ id: 15913 }];
      vi.spyOn(invoiceTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceTypeCollection })));
      const additionalInvoiceTypes = [invoiceType];
      const expectedCollection: IInvoiceType[] = [...additionalInvoiceTypes, ...invoiceTypeCollection];
      vi.spyOn(invoiceTypeService, 'addInvoiceTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(invoiceTypeService.query).toHaveBeenCalled();
      expect(invoiceTypeService.addInvoiceTypeToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceTypeCollection,
        ...additionalInvoiceTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoiceTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Order query and add missing value', () => {
      const invoice: IInvoice = { id: 1801 };
      const order: IOrder = { id: 14644 };
      invoice.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const invoice: IInvoice = { id: 1801 };
      const fromParty: IParty = { id: 24527 };
      invoice.fromParty = fromParty;
      const toParty: IParty = { id: 24527 };
      invoice.toParty = toParty;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [fromParty, toParty];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const invoice: IInvoice = { id: 1801 };
      const roleType: IRoleType = { id: 17576 };
      invoice.roleType = roleType;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleType];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const invoice: IInvoice = { id: 1801 };
      const status: IStatus = { id: 2927 };
      invoice.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ContactMech query and add missing value', () => {
      const invoice: IInvoice = { id: 1801 };
      const contactMech: IContactMech = { id: 5019 };
      invoice.contactMech = contactMech;

      const contactMechCollection: IContactMech[] = [{ id: 5019 }];
      vi.spyOn(contactMechService, 'query').mockReturnValue(of(new HttpResponse({ body: contactMechCollection })));
      const additionalContactMeches = [contactMech];
      const expectedCollection: IContactMech[] = [...additionalContactMeches, ...contactMechCollection];
      vi.spyOn(contactMechService, 'addContactMechToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(contactMechService.query).toHaveBeenCalled();
      expect(contactMechService.addContactMechToCollectionIfMissing).toHaveBeenCalledWith(
        contactMechCollection,
        ...additionalContactMeches.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactMechesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const invoice: IInvoice = { id: 1801 };
      const currencyUom: IUom = { id: 6576 };
      invoice.currencyUom = currencyUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [currencyUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const invoice: IInvoice = { id: 1801 };
      const invoiceType: IInvoiceType = { id: 15913 };
      invoice.invoiceType = invoiceType;
      const order: IOrder = { id: 14644 };
      invoice.order = order;
      const fromParty: IParty = { id: 24527 };
      invoice.fromParty = fromParty;
      const toParty: IParty = { id: 24527 };
      invoice.toParty = toParty;
      const roleType: IRoleType = { id: 17576 };
      invoice.roleType = roleType;
      const status: IStatus = { id: 2927 };
      invoice.status = status;
      const contactMech: IContactMech = { id: 5019 };
      invoice.contactMech = contactMech;
      const currencyUom: IUom = { id: 6576 };
      invoice.currencyUom = currencyUom;

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(comp.invoiceTypesSharedCollection()).toContainEqual(invoiceType);
      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.partiesSharedCollection()).toContainEqual(fromParty);
      expect(comp.partiesSharedCollection()).toContainEqual(toParty);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleType);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.contactMechesSharedCollection()).toContainEqual(contactMech);
      expect(comp.uomsSharedCollection()).toContainEqual(currencyUom);
      expect(comp.invoice).toEqual(invoice);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoice>();
      const invoice = { id: 1362 };
      vi.spyOn(invoiceFormService, 'getInvoice').mockReturnValue(invoice);
      vi.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoice);
      saveSubject.complete();

      // THEN
      expect(invoiceFormService.getInvoice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceService.update).toHaveBeenCalledWith(expect.objectContaining(invoice));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoice>();
      const invoice = { id: 1362 };
      vi.spyOn(invoiceFormService, 'getInvoice').mockReturnValue({ id: null });
      vi.spyOn(invoiceService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoice);
      saveSubject.complete();

      // THEN
      expect(invoiceFormService.getInvoice).toHaveBeenCalled();
      expect(invoiceService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoice>();
      const invoice = { id: 1362 };
      vi.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInvoiceType', () => {
      it('should forward to invoiceTypeService', () => {
        const entity = { id: 15913 };
        const entity2 = { id: 3910 };
        vi.spyOn(invoiceTypeService, 'compareInvoiceType');
        comp.compareInvoiceType(entity, entity2);
        expect(invoiceTypeService.compareInvoiceType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRoleType', () => {
      it('should forward to roleTypeService', () => {
        const entity = { id: 17576 };
        const entity2 = { id: 27052 };
        vi.spyOn(roleTypeService, 'compareRoleType');
        comp.compareRoleType(entity, entity2);
        expect(roleTypeService.compareRoleType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareContactMech', () => {
      it('should forward to contactMechService', () => {
        const entity = { id: 5019 };
        const entity2 = { id: 29939 };
        vi.spyOn(contactMechService, 'compareContactMech');
        comp.compareContactMech(entity, entity2);
        expect(contactMechService.compareContactMech).toHaveBeenCalledWith(entity, entity2);
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
  });
});
