import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderAdjustmentType } from 'app/entities/order-adjustment-type/order-adjustment-type.model';
import { OrderAdjustmentTypeService } from 'app/entities/order-adjustment-type/service/order-adjustment-type.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { TaxAuthorityService } from 'app/entities/tax-authority/service/tax-authority.service';
import { ITaxAuthority } from 'app/entities/tax-authority/tax-authority.model';
import { IOrderAdjustment } from '../order-adjustment.model';
import { OrderAdjustmentService } from '../service/order-adjustment.service';

import { OrderAdjustmentFormService } from './order-adjustment-form.service';
import { OrderAdjustmentUpdate } from './order-adjustment-update';

describe('OrderAdjustment Management Update Component', () => {
  let comp: OrderAdjustmentUpdate;
  let fixture: ComponentFixture<OrderAdjustmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderAdjustmentFormService: OrderAdjustmentFormService;
  let orderAdjustmentService: OrderAdjustmentService;
  let orderService: OrderService;
  let orderItemService: OrderItemService;
  let orderAdjustmentTypeService: OrderAdjustmentTypeService;
  let productService: ProductService;
  let productFeatureService: ProductFeatureService;
  let taxAuthorityService: TaxAuthorityService;

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

    fixture = TestBed.createComponent(OrderAdjustmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderAdjustmentFormService = TestBed.inject(OrderAdjustmentFormService);
    orderAdjustmentService = TestBed.inject(OrderAdjustmentService);
    orderService = TestBed.inject(OrderService);
    orderItemService = TestBed.inject(OrderItemService);
    orderAdjustmentTypeService = TestBed.inject(OrderAdjustmentTypeService);
    productService = TestBed.inject(ProductService);
    productFeatureService = TestBed.inject(ProductFeatureService);
    taxAuthorityService = TestBed.inject(TaxAuthorityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Order query and add missing value', () => {
      const orderAdjustment: IOrderAdjustment = { id: 25358 };
      const order: IOrder = { id: 14644 };
      orderAdjustment.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderAdjustment });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderItem query and add missing value', () => {
      const orderAdjustment: IOrderAdjustment = { id: 25358 };
      const orderItem: IOrderItem = { id: 25971 };
      orderAdjustment.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderAdjustment });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderAdjustmentType query and add missing value', () => {
      const orderAdjustment: IOrderAdjustment = { id: 25358 };
      const adjustmentType: IOrderAdjustmentType = { id: 1453 };
      orderAdjustment.adjustmentType = adjustmentType;

      const orderAdjustmentTypeCollection: IOrderAdjustmentType[] = [{ id: 1453 }];
      vi.spyOn(orderAdjustmentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: orderAdjustmentTypeCollection })));
      const additionalOrderAdjustmentTypes = [adjustmentType];
      const expectedCollection: IOrderAdjustmentType[] = [...additionalOrderAdjustmentTypes, ...orderAdjustmentTypeCollection];
      vi.spyOn(orderAdjustmentTypeService, 'addOrderAdjustmentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderAdjustment });
      comp.ngOnInit();

      expect(orderAdjustmentTypeService.query).toHaveBeenCalled();
      expect(orderAdjustmentTypeService.addOrderAdjustmentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        orderAdjustmentTypeCollection,
        ...additionalOrderAdjustmentTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderAdjustmentTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const orderAdjustment: IOrderAdjustment = { id: 25358 };
      const product: IProduct = { id: 21536 };
      orderAdjustment.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderAdjustment });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFeature query and add missing value', () => {
      const orderAdjustment: IOrderAdjustment = { id: 25358 };
      const productFeature: IProductFeature = { id: 30957 };
      orderAdjustment.productFeature = productFeature;

      const productFeatureCollection: IProductFeature[] = [{ id: 30957 }];
      vi.spyOn(productFeatureService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureCollection })));
      const additionalProductFeatures = [productFeature];
      const expectedCollection: IProductFeature[] = [...additionalProductFeatures, ...productFeatureCollection];
      vi.spyOn(productFeatureService, 'addProductFeatureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderAdjustment });
      comp.ngOnInit();

      expect(productFeatureService.query).toHaveBeenCalled();
      expect(productFeatureService.addProductFeatureToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureCollection,
        ...additionalProductFeatures.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeaturesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TaxAuthority query and add missing value', () => {
      const orderAdjustment: IOrderAdjustment = { id: 25358 };
      const taxAuthority: ITaxAuthority = { id: 14408 };
      orderAdjustment.taxAuthority = taxAuthority;

      const taxAuthorityCollection: ITaxAuthority[] = [{ id: 14408 }];
      vi.spyOn(taxAuthorityService, 'query').mockReturnValue(of(new HttpResponse({ body: taxAuthorityCollection })));
      const additionalTaxAuthorities = [taxAuthority];
      const expectedCollection: ITaxAuthority[] = [...additionalTaxAuthorities, ...taxAuthorityCollection];
      vi.spyOn(taxAuthorityService, 'addTaxAuthorityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderAdjustment });
      comp.ngOnInit();

      expect(taxAuthorityService.query).toHaveBeenCalled();
      expect(taxAuthorityService.addTaxAuthorityToCollectionIfMissing).toHaveBeenCalledWith(
        taxAuthorityCollection,
        ...additionalTaxAuthorities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.taxAuthoritiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const orderAdjustment: IOrderAdjustment = { id: 25358 };
      const order: IOrder = { id: 14644 };
      orderAdjustment.order = order;
      const orderItem: IOrderItem = { id: 25971 };
      orderAdjustment.orderItem = orderItem;
      const adjustmentType: IOrderAdjustmentType = { id: 1453 };
      orderAdjustment.adjustmentType = adjustmentType;
      const product: IProduct = { id: 21536 };
      orderAdjustment.product = product;
      const productFeature: IProductFeature = { id: 30957 };
      orderAdjustment.productFeature = productFeature;
      const taxAuthority: ITaxAuthority = { id: 14408 };
      orderAdjustment.taxAuthority = taxAuthority;

      activatedRoute.data = of({ orderAdjustment });
      comp.ngOnInit();

      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.orderAdjustmentTypesSharedCollection()).toContainEqual(adjustmentType);
      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.productFeaturesSharedCollection()).toContainEqual(productFeature);
      expect(comp.taxAuthoritiesSharedCollection()).toContainEqual(taxAuthority);
      expect(comp.orderAdjustment).toEqual(orderAdjustment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderAdjustment>();
      const orderAdjustment = { id: 9467 };
      vi.spyOn(orderAdjustmentFormService, 'getOrderAdjustment').mockReturnValue(orderAdjustment);
      vi.spyOn(orderAdjustmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderAdjustment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderAdjustment);
      saveSubject.complete();

      // THEN
      expect(orderAdjustmentFormService.getOrderAdjustment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderAdjustmentService.update).toHaveBeenCalledWith(expect.objectContaining(orderAdjustment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderAdjustment>();
      const orderAdjustment = { id: 9467 };
      vi.spyOn(orderAdjustmentFormService, 'getOrderAdjustment').mockReturnValue({ id: null });
      vi.spyOn(orderAdjustmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderAdjustment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderAdjustment);
      saveSubject.complete();

      // THEN
      expect(orderAdjustmentFormService.getOrderAdjustment).toHaveBeenCalled();
      expect(orderAdjustmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderAdjustment>();
      const orderAdjustment = { id: 9467 };
      vi.spyOn(orderAdjustmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderAdjustment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderAdjustmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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

    describe('compareOrderAdjustmentType', () => {
      it('should forward to orderAdjustmentTypeService', () => {
        const entity = { id: 1453 };
        const entity2 = { id: 11072 };
        vi.spyOn(orderAdjustmentTypeService, 'compareOrderAdjustmentType');
        comp.compareOrderAdjustmentType(entity, entity2);
        expect(orderAdjustmentTypeService.compareOrderAdjustmentType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareProductFeature', () => {
      it('should forward to productFeatureService', () => {
        const entity = { id: 30957 };
        const entity2 = { id: 7728 };
        vi.spyOn(productFeatureService, 'compareProductFeature');
        comp.compareProductFeature(entity, entity2);
        expect(productFeatureService.compareProductFeature).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTaxAuthority', () => {
      it('should forward to taxAuthorityService', () => {
        const entity = { id: 14408 };
        const entity2 = { id: 27648 };
        vi.spyOn(taxAuthorityService, 'compareTaxAuthority');
        comp.compareTaxAuthority(entity, entity2);
        expect(taxAuthorityService.compareTaxAuthority).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
