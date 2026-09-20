import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IInvoiceItemType } from 'app/entities/invoice-item-type/invoice-item-type.model';
import { InvoiceItemTypeService } from 'app/entities/invoice-item-type/service/invoice-item-type.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFacility } from 'app/entities/product-facility/product-facility.model';
import { ProductFacilityService } from 'app/entities/product-facility/service/product-facility.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { TaxAuthorityRateTypeService } from 'app/entities/tax-authority-rate-type/service/tax-authority-rate-type.service';
import { ITaxAuthorityRateType } from 'app/entities/tax-authority-rate-type/tax-authority-rate-type.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { IInvoiceItem } from '../invoice-item.model';
import { InvoiceItemService } from '../service/invoice-item.service';

import { InvoiceItemFormService } from './invoice-item-form.service';
import { InvoiceItemUpdate } from './invoice-item-update';

describe('InvoiceItem Management Update Component', () => {
  let comp: InvoiceItemUpdate;
  let fixture: ComponentFixture<InvoiceItemUpdate>;
  let activatedRoute: ActivatedRoute;
  let invoiceItemFormService: InvoiceItemFormService;
  let invoiceItemService: InvoiceItemService;
  let invoiceService: InvoiceService;
  let invoiceItemTypeService: InvoiceItemTypeService;
  let productService: ProductService;
  let uomService: UomService;
  let productFeatureService: ProductFeatureService;
  let productFacilityService: ProductFacilityService;
  let inventoryItemService: InventoryItemService;
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

    fixture = TestBed.createComponent(InvoiceItemUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceItemFormService = TestBed.inject(InvoiceItemFormService);
    invoiceItemService = TestBed.inject(InvoiceItemService);
    invoiceService = TestBed.inject(InvoiceService);
    invoiceItemTypeService = TestBed.inject(InvoiceItemTypeService);
    productService = TestBed.inject(ProductService);
    uomService = TestBed.inject(UomService);
    productFeatureService = TestBed.inject(ProductFeatureService);
    productFacilityService = TestBed.inject(ProductFacilityService);
    inventoryItemService = TestBed.inject(InventoryItemService);
    taxAuthorityRateTypeService = TestBed.inject(TaxAuthorityRateTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Invoice query and add missing value', () => {
      const invoiceItem: IInvoiceItem = { id: 28999 };
      const invoice: IInvoice = { id: 1362 };
      invoiceItem.invoice = invoice;
      const parentInvoice: IInvoice = { id: 1362 };
      invoiceItem.parentInvoice = parentInvoice;

      const invoiceCollection: IInvoice[] = [{ id: 1362 }];
      vi.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
      const additionalInvoices = [invoice, parentInvoice];
      const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
      vi.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(invoiceService.query).toHaveBeenCalled();
      expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceCollection,
        ...additionalInvoices.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoicesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InvoiceItemType query and add missing value', () => {
      const invoiceItem: IInvoiceItem = { id: 28999 };
      const invoiceItemType: IInvoiceItemType = { id: 32383 };
      invoiceItem.invoiceItemType = invoiceItemType;

      const invoiceItemTypeCollection: IInvoiceItemType[] = [{ id: 32383 }];
      vi.spyOn(invoiceItemTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceItemTypeCollection })));
      const additionalInvoiceItemTypes = [invoiceItemType];
      const expectedCollection: IInvoiceItemType[] = [...additionalInvoiceItemTypes, ...invoiceItemTypeCollection];
      vi.spyOn(invoiceItemTypeService, 'addInvoiceItemTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(invoiceItemTypeService.query).toHaveBeenCalled();
      expect(invoiceItemTypeService.addInvoiceItemTypeToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceItemTypeCollection,
        ...additionalInvoiceItemTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoiceItemTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const invoiceItem: IInvoiceItem = { id: 28999 };
      const product: IProduct = { id: 21536 };
      invoiceItem.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const invoiceItem: IInvoiceItem = { id: 28999 };
      const uom: IUom = { id: 6576 };
      invoiceItem.uom = uom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [uom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFeature query and add missing value', () => {
      const invoiceItem: IInvoiceItem = { id: 28999 };
      const productFeature: IProductFeature = { id: 30957 };
      invoiceItem.productFeature = productFeature;

      const productFeatureCollection: IProductFeature[] = [{ id: 30957 }];
      vi.spyOn(productFeatureService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureCollection })));
      const additionalProductFeatures = [productFeature];
      const expectedCollection: IProductFeature[] = [...additionalProductFeatures, ...productFeatureCollection];
      vi.spyOn(productFeatureService, 'addProductFeatureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(productFeatureService.query).toHaveBeenCalled();
      expect(productFeatureService.addProductFeatureToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureCollection,
        ...additionalProductFeatures.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeaturesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFacility query and add missing value', () => {
      const invoiceItem: IInvoiceItem = { id: 28999 };
      const productFacility: IProductFacility = { id: 30505 };
      invoiceItem.productFacility = productFacility;

      const productFacilityCollection: IProductFacility[] = [{ id: 30505 }];
      vi.spyOn(productFacilityService, 'query').mockReturnValue(of(new HttpResponse({ body: productFacilityCollection })));
      const additionalProductFacilities = [productFacility];
      const expectedCollection: IProductFacility[] = [...additionalProductFacilities, ...productFacilityCollection];
      vi.spyOn(productFacilityService, 'addProductFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(productFacilityService.query).toHaveBeenCalled();
      expect(productFacilityService.addProductFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        productFacilityCollection,
        ...additionalProductFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFacilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InventoryItem query and add missing value', () => {
      const invoiceItem: IInvoiceItem = { id: 28999 };
      const fromInventoryItem: IInventoryItem = { id: 7462 };
      invoiceItem.fromInventoryItem = fromInventoryItem;

      const inventoryItemCollection: IInventoryItem[] = [{ id: 7462 }];
      vi.spyOn(inventoryItemService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryItemCollection })));
      const additionalInventoryItems = [fromInventoryItem];
      const expectedCollection: IInventoryItem[] = [...additionalInventoryItems, ...inventoryItemCollection];
      vi.spyOn(inventoryItemService, 'addInventoryItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(inventoryItemService.query).toHaveBeenCalled();
      expect(inventoryItemService.addInventoryItemToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryItemCollection,
        ...additionalInventoryItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.inventoryItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TaxAuthorityRateType query and add missing value', () => {
      const invoiceItem: IInvoiceItem = { id: 28999 };
      const taxAuthRate: ITaxAuthorityRateType = { id: 11026 };
      invoiceItem.taxAuthRate = taxAuthRate;

      const taxAuthorityRateTypeCollection: ITaxAuthorityRateType[] = [{ id: 11026 }];
      vi.spyOn(taxAuthorityRateTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: taxAuthorityRateTypeCollection })));
      const additionalTaxAuthorityRateTypes = [taxAuthRate];
      const expectedCollection: ITaxAuthorityRateType[] = [...additionalTaxAuthorityRateTypes, ...taxAuthorityRateTypeCollection];
      vi.spyOn(taxAuthorityRateTypeService, 'addTaxAuthorityRateTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(taxAuthorityRateTypeService.query).toHaveBeenCalled();
      expect(taxAuthorityRateTypeService.addTaxAuthorityRateTypeToCollectionIfMissing).toHaveBeenCalledWith(
        taxAuthorityRateTypeCollection,
        ...additionalTaxAuthorityRateTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.taxAuthorityRateTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InvoiceItem query and add missing value', () => {
      const invoiceItem: IInvoiceItem = { id: 28999 };
      const parentInvoiceItem: IInvoiceItem = { id: 29231 };
      invoiceItem.parentInvoiceItem = parentInvoiceItem;

      const invoiceItemCollection: IInvoiceItem[] = [{ id: 29231 }];
      vi.spyOn(invoiceItemService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceItemCollection })));
      const additionalInvoiceItems = [parentInvoiceItem];
      const expectedCollection: IInvoiceItem[] = [...additionalInvoiceItems, ...invoiceItemCollection];
      vi.spyOn(invoiceItemService, 'addInvoiceItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(invoiceItemService.query).toHaveBeenCalled();
      expect(invoiceItemService.addInvoiceItemToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceItemCollection,
        ...additionalInvoiceItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoiceItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const invoiceItem: IInvoiceItem = { id: 28999 };
      const invoice: IInvoice = { id: 1362 };
      invoiceItem.invoice = invoice;
      const parentInvoice: IInvoice = { id: 1362 };
      invoiceItem.parentInvoice = parentInvoice;
      const invoiceItemType: IInvoiceItemType = { id: 32383 };
      invoiceItem.invoiceItemType = invoiceItemType;
      const product: IProduct = { id: 21536 };
      invoiceItem.product = product;
      const uom: IUom = { id: 6576 };
      invoiceItem.uom = uom;
      const productFeature: IProductFeature = { id: 30957 };
      invoiceItem.productFeature = productFeature;
      const productFacility: IProductFacility = { id: 30505 };
      invoiceItem.productFacility = productFacility;
      const fromInventoryItem: IInventoryItem = { id: 7462 };
      invoiceItem.fromInventoryItem = fromInventoryItem;
      const taxAuthRate: ITaxAuthorityRateType = { id: 11026 };
      invoiceItem.taxAuthRate = taxAuthRate;
      const parentInvoiceItem: IInvoiceItem = { id: 29231 };
      invoiceItem.parentInvoiceItem = parentInvoiceItem;

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(comp.invoicesSharedCollection()).toContainEqual(invoice);
      expect(comp.invoicesSharedCollection()).toContainEqual(parentInvoice);
      expect(comp.invoiceItemTypesSharedCollection()).toContainEqual(invoiceItemType);
      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.uomsSharedCollection()).toContainEqual(uom);
      expect(comp.productFeaturesSharedCollection()).toContainEqual(productFeature);
      expect(comp.productFacilitiesSharedCollection()).toContainEqual(productFacility);
      expect(comp.inventoryItemsSharedCollection()).toContainEqual(fromInventoryItem);
      expect(comp.taxAuthorityRateTypesSharedCollection()).toContainEqual(taxAuthRate);
      expect(comp.invoiceItemsSharedCollection()).toContainEqual(parentInvoiceItem);
      expect(comp.invoiceItem).toEqual(invoiceItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceItem>();
      const invoiceItem = { id: 29231 };
      vi.spyOn(invoiceItemFormService, 'getInvoiceItem').mockReturnValue(invoiceItem);
      vi.spyOn(invoiceItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoiceItem);
      saveSubject.complete();

      // THEN
      expect(invoiceItemFormService.getInvoiceItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceItemService.update).toHaveBeenCalledWith(expect.objectContaining(invoiceItem));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceItem>();
      const invoiceItem = { id: 29231 };
      vi.spyOn(invoiceItemFormService, 'getInvoiceItem').mockReturnValue({ id: null });
      vi.spyOn(invoiceItemService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoiceItem);
      saveSubject.complete();

      // THEN
      expect(invoiceItemFormService.getInvoiceItem).toHaveBeenCalled();
      expect(invoiceItemService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceItem>();
      const invoiceItem = { id: 29231 };
      vi.spyOn(invoiceItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceItemService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInvoice', () => {
      it('should forward to invoiceService', () => {
        const entity = { id: 1362 };
        const entity2 = { id: 1801 };
        vi.spyOn(invoiceService, 'compareInvoice');
        comp.compareInvoice(entity, entity2);
        expect(invoiceService.compareInvoice).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInvoiceItemType', () => {
      it('should forward to invoiceItemTypeService', () => {
        const entity = { id: 32383 };
        const entity2 = { id: 25434 };
        vi.spyOn(invoiceItemTypeService, 'compareInvoiceItemType');
        comp.compareInvoiceItemType(entity, entity2);
        expect(invoiceItemTypeService.compareInvoiceItemType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareTaxAuthorityRateType', () => {
      it('should forward to taxAuthorityRateTypeService', () => {
        const entity = { id: 11026 };
        const entity2 = { id: 3035 };
        vi.spyOn(taxAuthorityRateTypeService, 'compareTaxAuthorityRateType');
        comp.compareTaxAuthorityRateType(entity, entity2);
        expect(taxAuthorityRateTypeService.compareTaxAuthorityRateType).toHaveBeenCalledWith(entity, entity2);
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
  });
});
