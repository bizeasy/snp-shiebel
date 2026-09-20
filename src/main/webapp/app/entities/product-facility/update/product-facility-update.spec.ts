import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFacility } from '../product-facility.model';
import { ProductFacilityService } from '../service/product-facility.service';

import { ProductFacilityFormService } from './product-facility-form.service';
import { ProductFacilityUpdate } from './product-facility-update';

describe('ProductFacility Management Update Component', () => {
  let comp: ProductFacilityUpdate;
  let fixture: ComponentFixture<ProductFacilityUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFacilityFormService: ProductFacilityFormService;
  let productFacilityService: ProductFacilityService;
  let productService: ProductService;
  let facilityService: FacilityService;

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

    fixture = TestBed.createComponent(ProductFacilityUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFacilityFormService = TestBed.inject(ProductFacilityFormService);
    productFacilityService = TestBed.inject(ProductFacilityService);
    productService = TestBed.inject(ProductService);
    facilityService = TestBed.inject(FacilityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const productFacility: IProductFacility = { id: 4242 };
      const product: IProduct = { id: 21536 };
      productFacility.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFacility });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Facility query and add missing value', () => {
      const productFacility: IProductFacility = { id: 4242 };
      const facility: IFacility = { id: 4866 };
      productFacility.facility = facility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [facility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFacility });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productFacility: IProductFacility = { id: 4242 };
      const product: IProduct = { id: 21536 };
      productFacility.product = product;
      const facility: IFacility = { id: 4866 };
      productFacility.facility = facility;

      activatedRoute.data = of({ productFacility });
      comp.ngOnInit();

      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.facilitiesSharedCollection()).toContainEqual(facility);
      expect(comp.productFacility).toEqual(productFacility);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFacility>();
      const productFacility = { id: 30505 };
      vi.spyOn(productFacilityFormService, 'getProductFacility').mockReturnValue(productFacility);
      vi.spyOn(productFacilityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFacility });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFacility);
      saveSubject.complete();

      // THEN
      expect(productFacilityFormService.getProductFacility).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productFacilityService.update).toHaveBeenCalledWith(expect.objectContaining(productFacility));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFacility>();
      const productFacility = { id: 30505 };
      vi.spyOn(productFacilityFormService, 'getProductFacility').mockReturnValue({ id: null });
      vi.spyOn(productFacilityService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFacility: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFacility);
      saveSubject.complete();

      // THEN
      expect(productFacilityFormService.getProductFacility).toHaveBeenCalled();
      expect(productFacilityService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFacility>();
      const productFacility = { id: 30505 };
      vi.spyOn(productFacilityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFacility });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productFacilityService.update).toHaveBeenCalled();
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

    describe('compareFacility', () => {
      it('should forward to facilityService', () => {
        const entity = { id: 4866 };
        const entity2 = { id: 6958 };
        vi.spyOn(facilityService, 'compareFacility');
        comp.compareFacility(entity, entity2);
        expect(facilityService.compareFacility).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
