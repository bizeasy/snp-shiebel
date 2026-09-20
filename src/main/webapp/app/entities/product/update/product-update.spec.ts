import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInventoryItemType } from 'app/entities/inventory-item-type/inventory-item-type.model';
import { InventoryItemTypeService } from 'app/entities/inventory-item-type/service/inventory-item-type.service';
import { IProductCategory } from 'app/entities/product-category/product-category.model';
import { ProductCategoryService } from 'app/entities/product-category/service/product-category.service';
import { IProductType } from 'app/entities/product-type/product-type.model';
import { ProductTypeService } from 'app/entities/product-type/service/product-type.service';
import { TaxSlabService } from 'app/entities/tax-slab/service/tax-slab.service';
import { ITaxSlab } from 'app/entities/tax-slab/tax-slab.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';

import { ProductFormService } from './product-form.service';
import { ProductUpdate } from './product-update';

describe('Product Management Update Component', () => {
  let comp: ProductUpdate;
  let fixture: ComponentFixture<ProductUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFormService: ProductFormService;
  let productService: ProductService;
  let productTypeService: ProductTypeService;
  let productCategoryService: ProductCategoryService;
  let inventoryItemTypeService: InventoryItemTypeService;
  let taxSlabService: TaxSlabService;
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

    fixture = TestBed.createComponent(ProductUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFormService = TestBed.inject(ProductFormService);
    productService = TestBed.inject(ProductService);
    productTypeService = TestBed.inject(ProductTypeService);
    productCategoryService = TestBed.inject(ProductCategoryService);
    inventoryItemTypeService = TestBed.inject(InventoryItemTypeService);
    taxSlabService = TestBed.inject(TaxSlabService);
    uomService = TestBed.inject(UomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ProductType query and add missing value', () => {
      const product: IProduct = { id: 11926 };
      const productType: IProductType = { id: 28647 };
      product.productType = productType;

      const productTypeCollection: IProductType[] = [{ id: 28647 }];
      vi.spyOn(productTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: productTypeCollection })));
      const additionalProductTypes = [productType];
      const expectedCollection: IProductType[] = [...additionalProductTypes, ...productTypeCollection];
      vi.spyOn(productTypeService, 'addProductTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(productTypeService.query).toHaveBeenCalled();
      expect(productTypeService.addProductTypeToCollectionIfMissing).toHaveBeenCalledWith(
        productTypeCollection,
        ...additionalProductTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductCategory query and add missing value', () => {
      const product: IProduct = { id: 11926 };
      const primaryProductCategory: IProductCategory = { id: 29286 };
      product.primaryProductCategory = primaryProductCategory;

      const productCategoryCollection: IProductCategory[] = [{ id: 29286 }];
      vi.spyOn(productCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: productCategoryCollection })));
      const additionalProductCategories = [primaryProductCategory];
      const expectedCollection: IProductCategory[] = [...additionalProductCategories, ...productCategoryCollection];
      vi.spyOn(productCategoryService, 'addProductCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(productCategoryService.query).toHaveBeenCalled();
      expect(productCategoryService.addProductCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        productCategoryCollection,
        ...additionalProductCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const product: IProduct = { id: 11926 };
      const virtualProduct: IProduct = { id: 21536 };
      product.virtualProduct = virtualProduct;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [virtualProduct];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InventoryItemType query and add missing value', () => {
      const product: IProduct = { id: 11926 };
      const inventoryItemType: IInventoryItemType = { id: 9002 };
      product.inventoryItemType = inventoryItemType;

      const inventoryItemTypeCollection: IInventoryItemType[] = [{ id: 9002 }];
      vi.spyOn(inventoryItemTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryItemTypeCollection })));
      const additionalInventoryItemTypes = [inventoryItemType];
      const expectedCollection: IInventoryItemType[] = [...additionalInventoryItemTypes, ...inventoryItemTypeCollection];
      vi.spyOn(inventoryItemTypeService, 'addInventoryItemTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(inventoryItemTypeService.query).toHaveBeenCalled();
      expect(inventoryItemTypeService.addInventoryItemTypeToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryItemTypeCollection,
        ...additionalInventoryItemTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.inventoryItemTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TaxSlab query and add missing value', () => {
      const product: IProduct = { id: 11926 };
      const taxSlab: ITaxSlab = { id: 22491 };
      product.taxSlab = taxSlab;

      const taxSlabCollection: ITaxSlab[] = [{ id: 22491 }];
      vi.spyOn(taxSlabService, 'query').mockReturnValue(of(new HttpResponse({ body: taxSlabCollection })));
      const additionalTaxSlabs = [taxSlab];
      const expectedCollection: ITaxSlab[] = [...additionalTaxSlabs, ...taxSlabCollection];
      vi.spyOn(taxSlabService, 'addTaxSlabToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(taxSlabService.query).toHaveBeenCalled();
      expect(taxSlabService.addTaxSlabToCollectionIfMissing).toHaveBeenCalledWith(
        taxSlabCollection,
        ...additionalTaxSlabs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.taxSlabsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const product: IProduct = { id: 11926 };
      const uom: IUom = { id: 6576 };
      product.uom = uom;
      const quantityUom: IUom = { id: 6576 };
      product.quantityUom = quantityUom;
      const weightUom: IUom = { id: 6576 };
      product.weightUom = weightUom;
      const sizeUom: IUom = { id: 6576 };
      product.sizeUom = sizeUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [uom, quantityUom, weightUom, sizeUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const product: IProduct = { id: 11926 };
      const productType: IProductType = { id: 28647 };
      product.productType = productType;
      const primaryProductCategory: IProductCategory = { id: 29286 };
      product.primaryProductCategory = primaryProductCategory;
      const virtualProduct: IProduct = { id: 21536 };
      product.virtualProduct = virtualProduct;
      const inventoryItemType: IInventoryItemType = { id: 9002 };
      product.inventoryItemType = inventoryItemType;
      const taxSlab: ITaxSlab = { id: 22491 };
      product.taxSlab = taxSlab;
      const uom: IUom = { id: 6576 };
      product.uom = uom;
      const quantityUom: IUom = { id: 6576 };
      product.quantityUom = quantityUom;
      const weightUom: IUom = { id: 6576 };
      product.weightUom = weightUom;
      const sizeUom: IUom = { id: 6576 };
      product.sizeUom = sizeUom;

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.productTypesSharedCollection()).toContainEqual(productType);
      expect(comp.productCategoriesSharedCollection()).toContainEqual(primaryProductCategory);
      expect(comp.productsSharedCollection()).toContainEqual(virtualProduct);
      expect(comp.inventoryItemTypesSharedCollection()).toContainEqual(inventoryItemType);
      expect(comp.taxSlabsSharedCollection()).toContainEqual(taxSlab);
      expect(comp.uomsSharedCollection()).toContainEqual(uom);
      expect(comp.uomsSharedCollection()).toContainEqual(quantityUom);
      expect(comp.uomsSharedCollection()).toContainEqual(weightUom);
      expect(comp.uomsSharedCollection()).toContainEqual(sizeUom);
      expect(comp.product).toEqual(product);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProduct>();
      const product = { id: 21536 };
      vi.spyOn(productFormService, 'getProduct').mockReturnValue(product);
      vi.spyOn(productService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(product);
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(expect.objectContaining(product));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProduct>();
      const product = { id: 21536 };
      vi.spyOn(productFormService, 'getProduct').mockReturnValue({ id: null });
      vi.spyOn(productService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(product);
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(productService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProduct>();
      const product = { id: 21536 };
      vi.spyOn(productService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProductType', () => {
      it('should forward to productTypeService', () => {
        const entity = { id: 28647 };
        const entity2 = { id: 20361 };
        vi.spyOn(productTypeService, 'compareProductType');
        comp.compareProductType(entity, entity2);
        expect(productTypeService.compareProductType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductCategory', () => {
      it('should forward to productCategoryService', () => {
        const entity = { id: 29286 };
        const entity2 = { id: 19244 };
        vi.spyOn(productCategoryService, 'compareProductCategory');
        comp.compareProductCategory(entity, entity2);
        expect(productCategoryService.compareProductCategory).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareInventoryItemType', () => {
      it('should forward to inventoryItemTypeService', () => {
        const entity = { id: 9002 };
        const entity2 = { id: 3434 };
        vi.spyOn(inventoryItemTypeService, 'compareInventoryItemType');
        comp.compareInventoryItemType(entity, entity2);
        expect(inventoryItemTypeService.compareInventoryItemType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTaxSlab', () => {
      it('should forward to taxSlabService', () => {
        const entity = { id: 22491 };
        const entity2 = { id: 24819 };
        vi.spyOn(taxSlabService, 'compareTaxSlab');
        comp.compareTaxSlab(entity, entity2);
        expect(taxSlabService.compareTaxSlab).toHaveBeenCalledWith(entity, entity2);
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
