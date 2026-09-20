import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductReview } from '../product-review.model';
import { ProductReviewService } from '../service/product-review.service';

import { ProductReviewFormService } from './product-review-form.service';
import { ProductReviewUpdate } from './product-review-update';

describe('ProductReview Management Update Component', () => {
  let comp: ProductReviewUpdate;
  let fixture: ComponentFixture<ProductReviewUpdate>;
  let activatedRoute: ActivatedRoute;
  let productReviewFormService: ProductReviewFormService;
  let productReviewService: ProductReviewService;
  let productService: ProductService;
  let partyService: PartyService;
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

    fixture = TestBed.createComponent(ProductReviewUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productReviewFormService = TestBed.inject(ProductReviewFormService);
    productReviewService = TestBed.inject(ProductReviewService);
    productService = TestBed.inject(ProductService);
    partyService = TestBed.inject(PartyService);
    orderItemService = TestBed.inject(OrderItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const productReview: IProductReview = { id: 17685 };
      const product: IProduct = { id: 21536 };
      productReview.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productReview });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const productReview: IProductReview = { id: 17685 };
      const party: IParty = { id: 24527 };
      productReview.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productReview });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderItem query and add missing value', () => {
      const productReview: IProductReview = { id: 17685 };
      const orderItem: IOrderItem = { id: 25971 };
      productReview.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productReview });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productReview: IProductReview = { id: 17685 };
      const product: IProduct = { id: 21536 };
      productReview.product = product;
      const party: IParty = { id: 24527 };
      productReview.party = party;
      const orderItem: IOrderItem = { id: 25971 };
      productReview.orderItem = orderItem;

      activatedRoute.data = of({ productReview });
      comp.ngOnInit();

      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.productReview).toEqual(productReview);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductReview>();
      const productReview = { id: 3443 };
      vi.spyOn(productReviewFormService, 'getProductReview').mockReturnValue(productReview);
      vi.spyOn(productReviewService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productReview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productReview);
      saveSubject.complete();

      // THEN
      expect(productReviewFormService.getProductReview).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productReviewService.update).toHaveBeenCalledWith(expect.objectContaining(productReview));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductReview>();
      const productReview = { id: 3443 };
      vi.spyOn(productReviewFormService, 'getProductReview').mockReturnValue({ id: null });
      vi.spyOn(productReviewService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productReview: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productReview);
      saveSubject.complete();

      // THEN
      expect(productReviewFormService.getProductReview).toHaveBeenCalled();
      expect(productReviewService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductReview>();
      const productReview = { id: 3443 };
      vi.spyOn(productReviewService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productReview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productReviewService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('should forward to productService', () => {
        const entity = { id: 21536 };
        const entity2 = { id: 11926 };
        vi.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
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
