import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacilityLocation } from 'app/entities/facility-location/facility-location.model';
import { FacilityLocationService } from 'app/entities/facility-location/service/facility-location.service';
import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IPickList } from 'app/entities/pick-list/pick-list.model';
import { PickListService } from 'app/entities/pick-list/service/pick-list.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { IPickListItem } from '../pick-list-item.model';
import { PickListItemService } from '../service/pick-list-item.service';

import { PickListItemFormService } from './pick-list-item-form.service';
import { PickListItemUpdate } from './pick-list-item-update';

describe('PickListItem Management Update Component', () => {
  let comp: PickListItemUpdate;
  let fixture: ComponentFixture<PickListItemUpdate>;
  let activatedRoute: ActivatedRoute;
  let pickListItemFormService: PickListItemFormService;
  let pickListItemService: PickListItemService;
  let pickListService: PickListService;
  let orderService: OrderService;
  let orderItemService: OrderItemService;
  let inventoryItemService: InventoryItemService;
  let productService: ProductService;
  let facilityLocationService: FacilityLocationService;
  let statusService: StatusService;

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

    fixture = TestBed.createComponent(PickListItemUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pickListItemFormService = TestBed.inject(PickListItemFormService);
    pickListItemService = TestBed.inject(PickListItemService);
    pickListService = TestBed.inject(PickListService);
    orderService = TestBed.inject(OrderService);
    orderItemService = TestBed.inject(OrderItemService);
    inventoryItemService = TestBed.inject(InventoryItemService);
    productService = TestBed.inject(ProductService);
    facilityLocationService = TestBed.inject(FacilityLocationService);
    statusService = TestBed.inject(StatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PickList query and add missing value', () => {
      const pickListItem: IPickListItem = { id: 24944 };
      const pickList: IPickList = { id: 15448 };
      pickListItem.pickList = pickList;

      const pickListCollection: IPickList[] = [{ id: 15448 }];
      vi.spyOn(pickListService, 'query').mockReturnValue(of(new HttpResponse({ body: pickListCollection })));
      const additionalPickLists = [pickList];
      const expectedCollection: IPickList[] = [...additionalPickLists, ...pickListCollection];
      vi.spyOn(pickListService, 'addPickListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pickListItem });
      comp.ngOnInit();

      expect(pickListService.query).toHaveBeenCalled();
      expect(pickListService.addPickListToCollectionIfMissing).toHaveBeenCalledWith(
        pickListCollection,
        ...additionalPickLists.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.pickListsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Order query and add missing value', () => {
      const pickListItem: IPickListItem = { id: 24944 };
      const order: IOrder = { id: 14644 };
      pickListItem.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pickListItem });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderItem query and add missing value', () => {
      const pickListItem: IPickListItem = { id: 24944 };
      const orderItem: IOrderItem = { id: 25971 };
      pickListItem.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pickListItem });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InventoryItem query and add missing value', () => {
      const pickListItem: IPickListItem = { id: 24944 };
      const inventoryItem: IInventoryItem = { id: 7462 };
      pickListItem.inventoryItem = inventoryItem;

      const inventoryItemCollection: IInventoryItem[] = [{ id: 7462 }];
      vi.spyOn(inventoryItemService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryItemCollection })));
      const additionalInventoryItems = [inventoryItem];
      const expectedCollection: IInventoryItem[] = [...additionalInventoryItems, ...inventoryItemCollection];
      vi.spyOn(inventoryItemService, 'addInventoryItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pickListItem });
      comp.ngOnInit();

      expect(inventoryItemService.query).toHaveBeenCalled();
      expect(inventoryItemService.addInventoryItemToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryItemCollection,
        ...additionalInventoryItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.inventoryItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const pickListItem: IPickListItem = { id: 24944 };
      const product: IProduct = { id: 21536 };
      pickListItem.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pickListItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call FacilityLocation query and add missing value', () => {
      const pickListItem: IPickListItem = { id: 24944 };
      const facilityLocation: IFacilityLocation = { id: 76 };
      pickListItem.facilityLocation = facilityLocation;

      const facilityLocationCollection: IFacilityLocation[] = [{ id: 76 }];
      vi.spyOn(facilityLocationService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityLocationCollection })));
      const additionalFacilityLocations = [facilityLocation];
      const expectedCollection: IFacilityLocation[] = [...additionalFacilityLocations, ...facilityLocationCollection];
      vi.spyOn(facilityLocationService, 'addFacilityLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pickListItem });
      comp.ngOnInit();

      expect(facilityLocationService.query).toHaveBeenCalled();
      expect(facilityLocationService.addFacilityLocationToCollectionIfMissing).toHaveBeenCalledWith(
        facilityLocationCollection,
        ...additionalFacilityLocations.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilityLocationsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const pickListItem: IPickListItem = { id: 24944 };
      const status: IStatus = { id: 2927 };
      pickListItem.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pickListItem });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const pickListItem: IPickListItem = { id: 24944 };
      const pickList: IPickList = { id: 15448 };
      pickListItem.pickList = pickList;
      const order: IOrder = { id: 14644 };
      pickListItem.order = order;
      const orderItem: IOrderItem = { id: 25971 };
      pickListItem.orderItem = orderItem;
      const inventoryItem: IInventoryItem = { id: 7462 };
      pickListItem.inventoryItem = inventoryItem;
      const product: IProduct = { id: 21536 };
      pickListItem.product = product;
      const facilityLocation: IFacilityLocation = { id: 76 };
      pickListItem.facilityLocation = facilityLocation;
      const status: IStatus = { id: 2927 };
      pickListItem.status = status;

      activatedRoute.data = of({ pickListItem });
      comp.ngOnInit();

      expect(comp.pickListsSharedCollection()).toContainEqual(pickList);
      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.inventoryItemsSharedCollection()).toContainEqual(inventoryItem);
      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.facilityLocationsSharedCollection()).toContainEqual(facilityLocation);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.pickListItem).toEqual(pickListItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPickListItem>();
      const pickListItem = { id: 14337 };
      vi.spyOn(pickListItemFormService, 'getPickListItem').mockReturnValue(pickListItem);
      vi.spyOn(pickListItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pickListItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(pickListItem);
      saveSubject.complete();

      // THEN
      expect(pickListItemFormService.getPickListItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pickListItemService.update).toHaveBeenCalledWith(expect.objectContaining(pickListItem));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPickListItem>();
      const pickListItem = { id: 14337 };
      vi.spyOn(pickListItemFormService, 'getPickListItem').mockReturnValue({ id: null });
      vi.spyOn(pickListItemService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pickListItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(pickListItem);
      saveSubject.complete();

      // THEN
      expect(pickListItemFormService.getPickListItem).toHaveBeenCalled();
      expect(pickListItemService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPickListItem>();
      const pickListItem = { id: 14337 };
      vi.spyOn(pickListItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pickListItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pickListItemService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePickList', () => {
      it('should forward to pickListService', () => {
        const entity = { id: 15448 };
        const entity2 = { id: 863 };
        vi.spyOn(pickListService, 'comparePickList');
        comp.comparePickList(entity, entity2);
        expect(pickListService.comparePickList).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareInventoryItem', () => {
      it('should forward to inventoryItemService', () => {
        const entity = { id: 7462 };
        const entity2 = { id: 4332 };
        vi.spyOn(inventoryItemService, 'compareInventoryItem');
        comp.compareInventoryItem(entity, entity2);
        expect(inventoryItemService.compareInventoryItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProduct', () => {
      it('should forward to productService', () => {
        const entity = { id: 21536 };
        const entity2 = { id: 11926 };
        vi.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFacilityLocation', () => {
      it('should forward to facilityLocationService', () => {
        const entity = { id: 76 };
        const entity2 = { id: 28655 };
        vi.spyOn(facilityLocationService, 'compareFacilityLocation');
        comp.compareFacilityLocation(entity, entity2);
        expect(facilityLocationService.compareFacilityLocation).toHaveBeenCalledWith(entity, entity2);
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
  });
});
