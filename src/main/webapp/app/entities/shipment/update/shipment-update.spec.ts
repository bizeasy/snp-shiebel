import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IOrder } from 'app/entities/order/order.model';
import { ShipmentMethodTypeService } from 'app/entities/shipment-method-type/service/shipment-method-type.service';
import { IShipmentMethodType } from 'app/entities/shipment-method-type/shipment-method-type.model';
import { ShipmentTypeService } from 'app/entities/shipment-type/service/shipment-type.service';
import { IShipmentType } from 'app/entities/shipment-type/shipment-type.model';
import { ShipmentService } from '../service/shipment.service';
import { IShipment } from '../shipment.model';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

import { ShipmentFormService } from './shipment-form.service';
import { ShipmentUpdate } from './shipment-update';

describe('Shipment Management Update Component', () => {
  let comp: ShipmentUpdate;
  let fixture: ComponentFixture<ShipmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let shipmentFormService: ShipmentFormService;
  let shipmentService: ShipmentService;
  let shipmentTypeService: ShipmentTypeService;
  let shipmentMethodTypeService: ShipmentMethodTypeService;
  let statusService: StatusService;
  let orderService: OrderService;
  let orderItemService: OrderItemService;
  let facilityService: FacilityService;
  let partyService: PartyService;
  let contactMechService: ContactMechService;

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

    fixture = TestBed.createComponent(ShipmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shipmentFormService = TestBed.inject(ShipmentFormService);
    shipmentService = TestBed.inject(ShipmentService);
    shipmentTypeService = TestBed.inject(ShipmentTypeService);
    shipmentMethodTypeService = TestBed.inject(ShipmentMethodTypeService);
    statusService = TestBed.inject(StatusService);
    orderService = TestBed.inject(OrderService);
    orderItemService = TestBed.inject(OrderItemService);
    facilityService = TestBed.inject(FacilityService);
    partyService = TestBed.inject(PartyService);
    contactMechService = TestBed.inject(ContactMechService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ShipmentType query and add missing value', () => {
      const shipment: IShipment = { id: 9190 };
      const shipmentType: IShipmentType = { id: 16845 };
      shipment.shipmentType = shipmentType;

      const shipmentTypeCollection: IShipmentType[] = [{ id: 16845 }];
      vi.spyOn(shipmentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentTypeCollection })));
      const additionalShipmentTypes = [shipmentType];
      const expectedCollection: IShipmentType[] = [...additionalShipmentTypes, ...shipmentTypeCollection];
      vi.spyOn(shipmentTypeService, 'addShipmentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(shipmentTypeService.query).toHaveBeenCalled();
      expect(shipmentTypeService.addShipmentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentTypeCollection,
        ...additionalShipmentTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ShipmentMethodType query and add missing value', () => {
      const shipment: IShipment = { id: 9190 };
      const shipmentMethodType: IShipmentMethodType = { id: 6802 };
      shipment.shipmentMethodType = shipmentMethodType;

      const shipmentMethodTypeCollection: IShipmentMethodType[] = [{ id: 6802 }];
      vi.spyOn(shipmentMethodTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentMethodTypeCollection })));
      const additionalShipmentMethodTypes = [shipmentMethodType];
      const expectedCollection: IShipmentMethodType[] = [...additionalShipmentMethodTypes, ...shipmentMethodTypeCollection];
      vi.spyOn(shipmentMethodTypeService, 'addShipmentMethodTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(shipmentMethodTypeService.query).toHaveBeenCalled();
      expect(shipmentMethodTypeService.addShipmentMethodTypeToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentMethodTypeCollection,
        ...additionalShipmentMethodTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentMethodTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const shipment: IShipment = { id: 9190 };
      const status: IStatus = { id: 2927 };
      shipment.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Order query and add missing value', () => {
      const shipment: IShipment = { id: 9190 };
      const order: IOrder = { id: 14644 };
      shipment.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderItem query and add missing value', () => {
      const shipment: IShipment = { id: 9190 };
      const orderItem: IOrderItem = { id: 25971 };
      shipment.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Facility query and add missing value', () => {
      const shipment: IShipment = { id: 9190 };
      const originFacility: IFacility = { id: 4866 };
      shipment.originFacility = originFacility;
      const destinationFacility: IFacility = { id: 4866 };
      shipment.destinationFacility = destinationFacility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [originFacility, destinationFacility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const shipment: IShipment = { id: 9190 };
      const fromParty: IParty = { id: 24527 };
      shipment.fromParty = fromParty;
      const toParty: IParty = { id: 24527 };
      shipment.toParty = toParty;
      const carrierParty: IParty = { id: 24527 };
      shipment.carrierParty = carrierParty;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [fromParty, toParty, carrierParty];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ContactMech query and add missing value', () => {
      const shipment: IShipment = { id: 9190 };
      const contactMech: IContactMech = { id: 5019 };
      shipment.contactMech = contactMech;

      const contactMechCollection: IContactMech[] = [{ id: 5019 }];
      vi.spyOn(contactMechService, 'query').mockReturnValue(of(new HttpResponse({ body: contactMechCollection })));
      const additionalContactMeches = [contactMech];
      const expectedCollection: IContactMech[] = [...additionalContactMeches, ...contactMechCollection];
      vi.spyOn(contactMechService, 'addContactMechToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(contactMechService.query).toHaveBeenCalled();
      expect(contactMechService.addContactMechToCollectionIfMissing).toHaveBeenCalledWith(
        contactMechCollection,
        ...additionalContactMeches.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactMechesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const shipment: IShipment = { id: 9190 };
      const shipmentType: IShipmentType = { id: 16845 };
      shipment.shipmentType = shipmentType;
      const shipmentMethodType: IShipmentMethodType = { id: 6802 };
      shipment.shipmentMethodType = shipmentMethodType;
      const status: IStatus = { id: 2927 };
      shipment.status = status;
      const order: IOrder = { id: 14644 };
      shipment.order = order;
      const orderItem: IOrderItem = { id: 25971 };
      shipment.orderItem = orderItem;
      const originFacility: IFacility = { id: 4866 };
      shipment.originFacility = originFacility;
      const destinationFacility: IFacility = { id: 4866 };
      shipment.destinationFacility = destinationFacility;
      const fromParty: IParty = { id: 24527 };
      shipment.fromParty = fromParty;
      const toParty: IParty = { id: 24527 };
      shipment.toParty = toParty;
      const carrierParty: IParty = { id: 24527 };
      shipment.carrierParty = carrierParty;
      const contactMech: IContactMech = { id: 5019 };
      shipment.contactMech = contactMech;

      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      expect(comp.shipmentTypesSharedCollection()).toContainEqual(shipmentType);
      expect(comp.shipmentMethodTypesSharedCollection()).toContainEqual(shipmentMethodType);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.facilitiesSharedCollection()).toContainEqual(originFacility);
      expect(comp.facilitiesSharedCollection()).toContainEqual(destinationFacility);
      expect(comp.partiesSharedCollection()).toContainEqual(fromParty);
      expect(comp.partiesSharedCollection()).toContainEqual(toParty);
      expect(comp.partiesSharedCollection()).toContainEqual(carrierParty);
      expect(comp.contactMechesSharedCollection()).toContainEqual(contactMech);
      expect(comp.shipment).toEqual(shipment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipment>();
      const shipment = { id: 9594 };
      vi.spyOn(shipmentFormService, 'getShipment').mockReturnValue(shipment);
      vi.spyOn(shipmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipment);
      saveSubject.complete();

      // THEN
      expect(shipmentFormService.getShipment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shipmentService.update).toHaveBeenCalledWith(expect.objectContaining(shipment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipment>();
      const shipment = { id: 9594 };
      vi.spyOn(shipmentFormService, 'getShipment').mockReturnValue({ id: null });
      vi.spyOn(shipmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipment);
      saveSubject.complete();

      // THEN
      expect(shipmentFormService.getShipment).toHaveBeenCalled();
      expect(shipmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IShipment>();
      const shipment = { id: 9594 };
      vi.spyOn(shipmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shipmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareShipmentType', () => {
      it('should forward to shipmentTypeService', () => {
        const entity = { id: 16845 };
        const entity2 = { id: 32676 };
        vi.spyOn(shipmentTypeService, 'compareShipmentType');
        comp.compareShipmentType(entity, entity2);
        expect(shipmentTypeService.compareShipmentType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareShipmentMethodType', () => {
      it('should forward to shipmentMethodTypeService', () => {
        const entity = { id: 6802 };
        const entity2 = { id: 25543 };
        vi.spyOn(shipmentMethodTypeService, 'compareShipmentMethodType');
        comp.compareShipmentMethodType(entity, entity2);
        expect(shipmentMethodTypeService.compareShipmentMethodType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareOrderItem', () => {
      it('should forward to orderItemService', () => {
        const entity = { id: 25971 };
        const entity2 = { id: 123 };
        vi.spyOn(orderItemService, 'compareOrderItem');
        comp.compareOrderItem(entity, entity2);
        expect(orderItemService.compareOrderItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFacility', () => {
      it('should forward to facilityService', () => {
        const entity = { id: 4866 };
        const entity2 = { id: 6958 };
        vi.spyOn(facilityService, 'compareFacility');
        comp.compareFacility(entity, entity2);
        expect(facilityService.compareFacility).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareContactMech', () => {
      it('should forward to contactMechService', () => {
        const entity = { id: 5019 };
        const entity2 = { id: 29939 };
        vi.spyOn(contactMechService, 'compareContactMech');
        comp.compareContactMech(entity, entity2);
        expect(contactMechService.compareContactMech).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
