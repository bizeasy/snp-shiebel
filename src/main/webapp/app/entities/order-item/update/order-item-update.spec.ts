import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItemType } from 'app/entities/order-item-type/order-item-type.model';
import { OrderItemTypeService } from 'app/entities/order-item-type/service/order-item-type.service';
import { SupplierProductService } from 'app/entities/supplier-product/service/supplier-product.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { IProductFacility } from 'app/entities/product-facility/product-facility.model';
import { ProductFacilityService } from 'app/entities/product-facility/service/product-facility.service';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { IQuoteItem } from 'app/entities/quote-item/quote-item.model';
import { QuoteItemService } from 'app/entities/quote-item/service/quote-item.service';
import { ShoppingListItemService } from 'app/entities/shopping-list-item/service/shopping-list-item.service';
import { IShoppingListItem } from 'app/entities/shopping-list-item/shopping-list-item.model';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { ISupplierProduct } from 'app/entities/supplier-product/supplier-product.model';
import { TaxAuthorityRateTypeService } from 'app/entities/tax-authority-rate-type/service/tax-authority-rate-type.service';
import { ITaxAuthorityRateType } from 'app/entities/tax-authority-rate-type/tax-authority-rate-type.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { IOrderItem } from '../order-item.model';
import { OrderItemService } from '../service/order-item.service';

import { OrderItemFormService } from './order-item-form.service';
import { OrderItemUpdate } from './order-item-update';

describe('OrderItem Management Update Component', () => {
  let comp: OrderItemUpdate;
  let fixture: ComponentFixture<OrderItemUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderItemFormService: OrderItemFormService;
  let orderItemService: OrderItemService;
  let orderService: OrderService;
  let orderItemTypeService: OrderItemTypeService;
  let productService: ProductService;
  let uomService: UomService;
  let supplierProductService: SupplierProductService;
  let partyService: PartyService;
  let productFeatureService: ProductFeatureService;
  let productFacilityService: ProductFacilityService;
  let inventoryItemService: InventoryItemService;
  let shoppingListItemService: ShoppingListItemService;
  let quoteItemService: QuoteItemService;
  let statusService: StatusService;
  let taxAuthorityRateTypeService: TaxAuthorityRateTypeService;

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

    fixture = TestBed.createComponent(OrderItemUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderItemFormService = TestBed.inject(OrderItemFormService);
    orderItemService = TestBed.inject(OrderItemService);
    orderService = TestBed.inject(OrderService);
    orderItemTypeService = TestBed.inject(OrderItemTypeService);
    productService = TestBed.inject(ProductService);
    uomService = TestBed.inject(UomService);
    supplierProductService = TestBed.inject(SupplierProductService);
    partyService = TestBed.inject(PartyService);
    productFeatureService = TestBed.inject(ProductFeatureService);
    productFacilityService = TestBed.inject(ProductFacilityService);
    inventoryItemService = TestBed.inject(InventoryItemService);
    shoppingListItemService = TestBed.inject(ShoppingListItemService);
    quoteItemService = TestBed.inject(QuoteItemService);
    statusService = TestBed.inject(StatusService);
    taxAuthorityRateTypeService = TestBed.inject(TaxAuthorityRateTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Order query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const order: IOrder = { id: 14644 };
      orderItem.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderItemType query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const orderItemType: IOrderItemType = { id: 31138 };
      orderItem.orderItemType = orderItemType;

      const orderItemTypeCollection: IOrderItemType[] = [{ id: 31138 }];
      vi.spyOn(orderItemTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemTypeCollection })));
      const additionalOrderItemTypes = [orderItemType];
      const expectedCollection: IOrderItemType[] = [...additionalOrderItemTypes, ...orderItemTypeCollection];
      vi.spyOn(orderItemTypeService, 'addOrderItemTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(orderItemTypeService.query).toHaveBeenCalled();
      expect(orderItemTypeService.addOrderItemTypeToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemTypeCollection,
        ...additionalOrderItemTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const product: IProduct = { id: 21536 };
      orderItem.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const uom: IUom = { id: 6576 };
      orderItem.uom = uom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [uom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call SupplierProduct query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const supplierProduct: ISupplierProduct = { id: 1977 };
      orderItem.supplierProduct = supplierProduct;

      const supplierProductCollection: ISupplierProduct[] = [{ id: 1977 }];
      vi.spyOn(supplierProductService, 'query').mockReturnValue(of(new HttpResponse({ body: supplierProductCollection })));
      const additionalSupplierProducts = [supplierProduct];
      const expectedCollection: ISupplierProduct[] = [...additionalSupplierProducts, ...supplierProductCollection];
      vi.spyOn(supplierProductService, 'addSupplierProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(supplierProductService.query).toHaveBeenCalled();
      expect(supplierProductService.addSupplierProductToCollectionIfMissing).toHaveBeenCalledWith(
        supplierProductCollection,
        ...additionalSupplierProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.supplierProductsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const supplier: IParty = { id: 24527 };
      orderItem.supplier = supplier;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [supplier];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFeature query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const productFeature: IProductFeature = { id: 30957 };
      orderItem.productFeature = productFeature;

      const productFeatureCollection: IProductFeature[] = [{ id: 30957 }];
      vi.spyOn(productFeatureService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureCollection })));
      const additionalProductFeatures = [productFeature];
      const expectedCollection: IProductFeature[] = [...additionalProductFeatures, ...productFeatureCollection];
      vi.spyOn(productFeatureService, 'addProductFeatureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(productFeatureService.query).toHaveBeenCalled();
      expect(productFeatureService.addProductFeatureToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureCollection,
        ...additionalProductFeatures.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeaturesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFacility query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const productFacility: IProductFacility = { id: 30505 };
      orderItem.productFacility = productFacility;

      const productFacilityCollection: IProductFacility[] = [{ id: 30505 }];
      vi.spyOn(productFacilityService, 'query').mockReturnValue(of(new HttpResponse({ body: productFacilityCollection })));
      const additionalProductFacilities = [productFacility];
      const expectedCollection: IProductFacility[] = [...additionalProductFacilities, ...productFacilityCollection];
      vi.spyOn(productFacilityService, 'addProductFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(productFacilityService.query).toHaveBeenCalled();
      expect(productFacilityService.addProductFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        productFacilityCollection,
        ...additionalProductFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFacilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InventoryItem query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const fromInventoryItem: IInventoryItem = { id: 7462 };
      orderItem.fromInventoryItem = fromInventoryItem;

      const inventoryItemCollection: IInventoryItem[] = [{ id: 7462 }];
      vi.spyOn(inventoryItemService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryItemCollection })));
      const additionalInventoryItems = [fromInventoryItem];
      const expectedCollection: IInventoryItem[] = [...additionalInventoryItems, ...inventoryItemCollection];
      vi.spyOn(inventoryItemService, 'addInventoryItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(inventoryItemService.query).toHaveBeenCalled();
      expect(inventoryItemService.addInventoryItemToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryItemCollection,
        ...additionalInventoryItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.inventoryItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ShoppingListItem query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const shoppingListItem: IShoppingListItem = { id: 26675 };
      orderItem.shoppingListItem = shoppingListItem;

      const shoppingListItemCollection: IShoppingListItem[] = [{ id: 26675 }];
      vi.spyOn(shoppingListItemService, 'query').mockReturnValue(of(new HttpResponse({ body: shoppingListItemCollection })));
      const additionalShoppingListItems = [shoppingListItem];
      const expectedCollection: IShoppingListItem[] = [...additionalShoppingListItems, ...shoppingListItemCollection];
      vi.spyOn(shoppingListItemService, 'addShoppingListItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(shoppingListItemService.query).toHaveBeenCalled();
      expect(shoppingListItemService.addShoppingListItemToCollectionIfMissing).toHaveBeenCalledWith(
        shoppingListItemCollection,
        ...additionalShoppingListItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shoppingListItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call QuoteItem query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const quoteItem: IQuoteItem = { id: 28072 };
      orderItem.quoteItem = quoteItem;

      const quoteItemCollection: IQuoteItem[] = [{ id: 28072 }];
      vi.spyOn(quoteItemService, 'query').mockReturnValue(of(new HttpResponse({ body: quoteItemCollection })));
      const additionalQuoteItems = [quoteItem];
      const expectedCollection: IQuoteItem[] = [...additionalQuoteItems, ...quoteItemCollection];
      vi.spyOn(quoteItemService, 'addQuoteItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(quoteItemService.query).toHaveBeenCalled();
      expect(quoteItemService.addQuoteItemToCollectionIfMissing).toHaveBeenCalledWith(
        quoteItemCollection,
        ...additionalQuoteItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quoteItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const status: IStatus = { id: 2927 };
      orderItem.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TaxAuthorityRateType query and add missing value', () => {
      const orderItem: IOrderItem = { id: 123 };
      const taxAuthRate: ITaxAuthorityRateType = { id: 11026 };
      orderItem.taxAuthRate = taxAuthRate;

      const taxAuthorityRateTypeCollection: ITaxAuthorityRateType[] = [{ id: 11026 }];
      vi.spyOn(taxAuthorityRateTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: taxAuthorityRateTypeCollection })));
      const additionalTaxAuthorityRateTypes = [taxAuthRate];
      const expectedCollection: ITaxAuthorityRateType[] = [...additionalTaxAuthorityRateTypes, ...taxAuthorityRateTypeCollection];
      vi.spyOn(taxAuthorityRateTypeService, 'addTaxAuthorityRateTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(taxAuthorityRateTypeService.query).toHaveBeenCalled();
      expect(taxAuthorityRateTypeService.addTaxAuthorityRateTypeToCollectionIfMissing).toHaveBeenCalledWith(
        taxAuthorityRateTypeCollection,
        ...additionalTaxAuthorityRateTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.taxAuthorityRateTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const orderItem: IOrderItem = { id: 123 };
      const order: IOrder = { id: 14644 };
      orderItem.order = order;
      const orderItemType: IOrderItemType = { id: 31138 };
      orderItem.orderItemType = orderItemType;
      const product: IProduct = { id: 21536 };
      orderItem.product = product;
      const uom: IUom = { id: 6576 };
      orderItem.uom = uom;
      const supplierProduct: ISupplierProduct = { id: 1977 };
      orderItem.supplierProduct = supplierProduct;
      const supplier: IParty = { id: 24527 };
      orderItem.supplier = supplier;
      const productFeature: IProductFeature = { id: 30957 };
      orderItem.productFeature = productFeature;
      const productFacility: IProductFacility = { id: 30505 };
      orderItem.productFacility = productFacility;
      const fromInventoryItem: IInventoryItem = { id: 7462 };
      orderItem.fromInventoryItem = fromInventoryItem;
      const shoppingListItem: IShoppingListItem = { id: 26675 };
      orderItem.shoppingListItem = shoppingListItem;
      const quoteItem: IQuoteItem = { id: 28072 };
      orderItem.quoteItem = quoteItem;
      const status: IStatus = { id: 2927 };
      orderItem.status = status;
      const taxAuthRate: ITaxAuthorityRateType = { id: 11026 };
      orderItem.taxAuthRate = taxAuthRate;

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.orderItemTypesSharedCollection()).toContainEqual(orderItemType);
      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.uomsSharedCollection()).toContainEqual(uom);
      expect(comp.supplierProductsSharedCollection()).toContainEqual(supplierProduct);
      expect(comp.partiesSharedCollection()).toContainEqual(supplier);
      expect(comp.productFeaturesSharedCollection()).toContainEqual(productFeature);
      expect(comp.productFacilitiesSharedCollection()).toContainEqual(productFacility);
      expect(comp.inventoryItemsSharedCollection()).toContainEqual(fromInventoryItem);
      expect(comp.shoppingListItemsSharedCollection()).toContainEqual(shoppingListItem);
      expect(comp.quoteItemsSharedCollection()).toContainEqual(quoteItem);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.taxAuthorityRateTypesSharedCollection()).toContainEqual(taxAuthRate);
      expect(comp.orderItem).toEqual(orderItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItem>();
      const orderItem = { id: 25971 };
      vi.spyOn(orderItemFormService, 'getOrderItem').mockReturnValue(orderItem);
      vi.spyOn(orderItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderItem);
      saveSubject.complete();

      // THEN
      expect(orderItemFormService.getOrderItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderItemService.update).toHaveBeenCalledWith(expect.objectContaining(orderItem));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItem>();
      const orderItem = { id: 25971 };
      vi.spyOn(orderItemFormService, 'getOrderItem').mockReturnValue({ id: null });
      vi.spyOn(orderItemService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderItem);
      saveSubject.complete();

      // THEN
      expect(orderItemFormService.getOrderItem).toHaveBeenCalled();
      expect(orderItemService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItem>();
      const orderItem = { id: 25971 };
      vi.spyOn(orderItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderItemService.update).toHaveBeenCalled();
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

    describe('compareOrderItemType', () => {
      it('should forward to orderItemTypeService', () => {
        const entity = { id: 31138 };
        const entity2 = { id: 18748 };
        vi.spyOn(orderItemTypeService, 'compareOrderItemType');
        comp.compareOrderItemType(entity, entity2);
        expect(orderItemTypeService.compareOrderItemType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareUom', () => {
      it('should forward to uomService', () => {
        const entity = { id: 6576 };
        const entity2 = { id: 20168 };
        vi.spyOn(uomService, 'compareUom');
        comp.compareUom(entity, entity2);
        expect(uomService.compareUom).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSupplierProduct', () => {
      it('should forward to supplierProductService', () => {
        const entity = { id: 1977 };
        const entity2 = { id: 11598 };
        vi.spyOn(supplierProductService, 'compareSupplierProduct');
        comp.compareSupplierProduct(entity, entity2);
        expect(supplierProductService.compareSupplierProduct).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareProductFeature', () => {
      it('should forward to productFeatureService', () => {
        const entity = { id: 30957 };
        const entity2 = { id: 7728 };
        vi.spyOn(productFeatureService, 'compareProductFeature');
        comp.compareProductFeature(entity, entity2);
        expect(productFeatureService.compareProductFeature).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductFacility', () => {
      it('should forward to productFacilityService', () => {
        const entity = { id: 30505 };
        const entity2 = { id: 4242 };
        vi.spyOn(productFacilityService, 'compareProductFacility');
        comp.compareProductFacility(entity, entity2);
        expect(productFacilityService.compareProductFacility).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareShoppingListItem', () => {
      it('should forward to shoppingListItemService', () => {
        const entity = { id: 26675 };
        const entity2 = { id: 6768 };
        vi.spyOn(shoppingListItemService, 'compareShoppingListItem');
        comp.compareShoppingListItem(entity, entity2);
        expect(shoppingListItemService.compareShoppingListItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareQuoteItem', () => {
      it('should forward to quoteItemService', () => {
        const entity = { id: 28072 };
        const entity2 = { id: 30045 };
        vi.spyOn(quoteItemService, 'compareQuoteItem');
        comp.compareQuoteItem(entity, entity2);
        expect(quoteItemService.compareQuoteItem).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareTaxAuthorityRateType', () => {
      it('should forward to taxAuthorityRateTypeService', () => {
        const entity = { id: 11026 };
        const entity2 = { id: 3035 };
        vi.spyOn(taxAuthorityRateTypeService, 'compareTaxAuthorityRateType');
        comp.compareTaxAuthorityRateType(entity, entity2);
        expect(taxAuthorityRateTypeService.compareTaxAuthorityRateType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
