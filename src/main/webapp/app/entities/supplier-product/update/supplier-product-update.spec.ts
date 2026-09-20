import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { SupplierProductService } from '../service/supplier-product.service';
import { ISupplierProduct } from '../supplier-product.model';

import { SupplierProductFormService } from './supplier-product-form.service';
import { SupplierProductUpdate } from './supplier-product-update';

describe('SupplierProduct Management Update Component', () => {
  let comp: SupplierProductUpdate;
  let fixture: ComponentFixture<SupplierProductUpdate>;
  let activatedRoute: ActivatedRoute;
  let supplierProductFormService: SupplierProductFormService;
  let supplierProductService: SupplierProductService;
  let productService: ProductService;
  let partyService: PartyService;
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

    fixture = TestBed.createComponent(SupplierProductUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    supplierProductFormService = TestBed.inject(SupplierProductFormService);
    supplierProductService = TestBed.inject(SupplierProductService);
    productService = TestBed.inject(ProductService);
    partyService = TestBed.inject(PartyService);
    uomService = TestBed.inject(UomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const supplierProduct: ISupplierProduct = { id: 11598 };
      const product: IProduct = { id: 21536 };
      supplierProduct.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ supplierProduct });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const supplierProduct: ISupplierProduct = { id: 11598 };
      const party: IParty = { id: 24527 };
      supplierProduct.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ supplierProduct });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const supplierProduct: ISupplierProduct = { id: 11598 };
      const quantityUom: IUom = { id: 6576 };
      supplierProduct.quantityUom = quantityUom;
      const currencyUom: IUom = { id: 6576 };
      supplierProduct.currencyUom = currencyUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [quantityUom, currencyUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ supplierProduct });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const supplierProduct: ISupplierProduct = { id: 11598 };
      const product: IProduct = { id: 21536 };
      supplierProduct.product = product;
      const party: IParty = { id: 24527 };
      supplierProduct.party = party;
      const quantityUom: IUom = { id: 6576 };
      supplierProduct.quantityUom = quantityUom;
      const currencyUom: IUom = { id: 6576 };
      supplierProduct.currencyUom = currencyUom;

      activatedRoute.data = of({ supplierProduct });
      comp.ngOnInit();

      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.uomsSharedCollection()).toContainEqual(quantityUom);
      expect(comp.uomsSharedCollection()).toContainEqual(currencyUom);
      expect(comp.supplierProduct).toEqual(supplierProduct);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISupplierProduct>();
      const supplierProduct = { id: 1977 };
      vi.spyOn(supplierProductFormService, 'getSupplierProduct').mockReturnValue(supplierProduct);
      vi.spyOn(supplierProductService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplierProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(supplierProduct);
      saveSubject.complete();

      // THEN
      expect(supplierProductFormService.getSupplierProduct).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(supplierProductService.update).toHaveBeenCalledWith(expect.objectContaining(supplierProduct));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISupplierProduct>();
      const supplierProduct = { id: 1977 };
      vi.spyOn(supplierProductFormService, 'getSupplierProduct').mockReturnValue({ id: null });
      vi.spyOn(supplierProductService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplierProduct: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(supplierProduct);
      saveSubject.complete();

      // THEN
      expect(supplierProductFormService.getSupplierProduct).toHaveBeenCalled();
      expect(supplierProductService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ISupplierProduct>();
      const supplierProduct = { id: 1977 };
      vi.spyOn(supplierProductService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplierProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(supplierProductService.update).toHaveBeenCalled();
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
