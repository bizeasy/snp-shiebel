import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IReturnHeader } from 'app/entities/return-header/return-header.model';
import { ReturnHeaderService } from 'app/entities/return-header/service/return-header.service';
import { IReturnHeaderType } from 'app/entities/return-header-type/return-header-type.model';
import { ReturnHeaderTypeService } from 'app/entities/return-header-type/service/return-header-type.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { IReturnItem } from '../return-item.model';
import { ReturnItemService } from '../service/return-item.service';

import { ReturnItemFormService } from './return-item-form.service';
import { ReturnItemUpdate } from './return-item-update';

describe('ReturnItem Management Update Component', () => {
  let comp: ReturnItemUpdate;
  let fixture: ComponentFixture<ReturnItemUpdate>;
  let activatedRoute: ActivatedRoute;
  let returnItemFormService: ReturnItemFormService;
  let returnItemService: ReturnItemService;
  let returnHeaderService: ReturnHeaderService;
  let returnHeaderTypeService: ReturnHeaderTypeService;
  let productService: ProductService;
  let orderItemService: OrderItemService;
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

    fixture = TestBed.createComponent(ReturnItemUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    returnItemFormService = TestBed.inject(ReturnItemFormService);
    returnItemService = TestBed.inject(ReturnItemService);
    returnHeaderService = TestBed.inject(ReturnHeaderService);
    returnHeaderTypeService = TestBed.inject(ReturnHeaderTypeService);
    productService = TestBed.inject(ProductService);
    orderItemService = TestBed.inject(OrderItemService);
    statusService = TestBed.inject(StatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ReturnHeader query and add missing value', () => {
      const returnItem: IReturnItem = { id: 12707 };
      const returnHeader: IReturnHeader = { id: 1951 };
      returnItem.returnHeader = returnHeader;

      const returnHeaderCollection: IReturnHeader[] = [{ id: 1951 }];
      vi.spyOn(returnHeaderService, 'query').mockReturnValue(of(new HttpResponse({ body: returnHeaderCollection })));
      const additionalReturnHeaders = [returnHeader];
      const expectedCollection: IReturnHeader[] = [...additionalReturnHeaders, ...returnHeaderCollection];
      vi.spyOn(returnHeaderService, 'addReturnHeaderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnItem });
      comp.ngOnInit();

      expect(returnHeaderService.query).toHaveBeenCalled();
      expect(returnHeaderService.addReturnHeaderToCollectionIfMissing).toHaveBeenCalledWith(
        returnHeaderCollection,
        ...additionalReturnHeaders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.returnHeadersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ReturnHeaderType query and add missing value', () => {
      const returnItem: IReturnItem = { id: 12707 };
      const returnType: IReturnHeaderType = { id: 14220 };
      returnItem.returnType = returnType;

      const returnHeaderTypeCollection: IReturnHeaderType[] = [{ id: 14220 }];
      vi.spyOn(returnHeaderTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: returnHeaderTypeCollection })));
      const additionalReturnHeaderTypes = [returnType];
      const expectedCollection: IReturnHeaderType[] = [...additionalReturnHeaderTypes, ...returnHeaderTypeCollection];
      vi.spyOn(returnHeaderTypeService, 'addReturnHeaderTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnItem });
      comp.ngOnInit();

      expect(returnHeaderTypeService.query).toHaveBeenCalled();
      expect(returnHeaderTypeService.addReturnHeaderTypeToCollectionIfMissing).toHaveBeenCalledWith(
        returnHeaderTypeCollection,
        ...additionalReturnHeaderTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.returnHeaderTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const returnItem: IReturnItem = { id: 12707 };
      const product: IProduct = { id: 21536 };
      returnItem.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderItem query and add missing value', () => {
      const returnItem: IReturnItem = { id: 12707 };
      const orderItem: IOrderItem = { id: 25971 };
      returnItem.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnItem });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const returnItem: IReturnItem = { id: 12707 };
      const status: IStatus = { id: 2927 };
      returnItem.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnItem });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const returnItem: IReturnItem = { id: 12707 };
      const returnHeader: IReturnHeader = { id: 1951 };
      returnItem.returnHeader = returnHeader;
      const returnType: IReturnHeaderType = { id: 14220 };
      returnItem.returnType = returnType;
      const product: IProduct = { id: 21536 };
      returnItem.product = product;
      const orderItem: IOrderItem = { id: 25971 };
      returnItem.orderItem = orderItem;
      const status: IStatus = { id: 2927 };
      returnItem.status = status;

      activatedRoute.data = of({ returnItem });
      comp.ngOnInit();

      expect(comp.returnHeadersSharedCollection()).toContainEqual(returnHeader);
      expect(comp.returnHeaderTypesSharedCollection()).toContainEqual(returnType);
      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.returnItem).toEqual(returnItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnItem>();
      const returnItem = { id: 28210 };
      vi.spyOn(returnItemFormService, 'getReturnItem').mockReturnValue(returnItem);
      vi.spyOn(returnItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(returnItem);
      saveSubject.complete();

      // THEN
      expect(returnItemFormService.getReturnItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(returnItemService.update).toHaveBeenCalledWith(expect.objectContaining(returnItem));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnItem>();
      const returnItem = { id: 28210 };
      vi.spyOn(returnItemFormService, 'getReturnItem').mockReturnValue({ id: null });
      vi.spyOn(returnItemService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(returnItem);
      saveSubject.complete();

      // THEN
      expect(returnItemFormService.getReturnItem).toHaveBeenCalled();
      expect(returnItemService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnItem>();
      const returnItem = { id: 28210 };
      vi.spyOn(returnItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(returnItemService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReturnHeader', () => {
      it('should forward to returnHeaderService', () => {
        const entity = { id: 1951 };
        const entity2 = { id: 32527 };
        vi.spyOn(returnHeaderService, 'compareReturnHeader');
        comp.compareReturnHeader(entity, entity2);
        expect(returnHeaderService.compareReturnHeader).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareReturnHeaderType', () => {
      it('should forward to returnHeaderTypeService', () => {
        const entity = { id: 14220 };
        const entity2 = { id: 26920 };
        vi.spyOn(returnHeaderTypeService, 'compareReturnHeaderType');
        comp.compareReturnHeaderType(entity, entity2);
        expect(returnHeaderTypeService.compareReturnHeaderType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareOrderItem', () => {
      it('should forward to orderItemService', () => {
        const entity = { id: 25971 };
        const entity2 = { id: 123 };
        vi.spyOn(orderItemService, 'compareOrderItem');
        comp.compareOrderItem(entity, entity2);
        expect(orderItemService.compareOrderItem).toHaveBeenCalledWith(entity, entity2);
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
