import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IFacilityLocation } from 'app/entities/facility-location/facility-location.model';
import { FacilityLocationService } from 'app/entities/facility-location/service/facility-location.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFacilityLocation } from '../product-facility-location.model';
import { ProductFacilityLocationService } from '../service/product-facility-location.service';

import { ProductFacilityLocationFormService } from './product-facility-location-form.service';
import { ProductFacilityLocationUpdate } from './product-facility-location-update';

describe('ProductFacilityLocation Management Update Component', () => {
  let comp: ProductFacilityLocationUpdate;
  let fixture: ComponentFixture<ProductFacilityLocationUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFacilityLocationFormService: ProductFacilityLocationFormService;
  let productFacilityLocationService: ProductFacilityLocationService;
  let productService: ProductService;
  let facilityService: FacilityService;
  let facilityLocationService: FacilityLocationService;

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

    fixture = TestBed.createComponent(ProductFacilityLocationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFacilityLocationFormService = TestBed.inject(ProductFacilityLocationFormService);
    productFacilityLocationService = TestBed.inject(ProductFacilityLocationService);
    productService = TestBed.inject(ProductService);
    facilityService = TestBed.inject(FacilityService);
    facilityLocationService = TestBed.inject(FacilityLocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const productFacilityLocation: IProductFacilityLocation = { id: 15870 };
      const product: IProduct = { id: 21536 };
      productFacilityLocation.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFacilityLocation });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Facility query and add missing value', () => {
      const productFacilityLocation: IProductFacilityLocation = { id: 15870 };
      const facility: IFacility = { id: 4866 };
      productFacilityLocation.facility = facility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [facility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFacilityLocation });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call FacilityLocation query and add missing value', () => {
      const productFacilityLocation: IProductFacilityLocation = { id: 15870 };
      const facilityLocation: IFacilityLocation = { id: 76 };
      productFacilityLocation.facilityLocation = facilityLocation;

      const facilityLocationCollection: IFacilityLocation[] = [{ id: 76 }];
      vi.spyOn(facilityLocationService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityLocationCollection })));
      const additionalFacilityLocations = [facilityLocation];
      const expectedCollection: IFacilityLocation[] = [...additionalFacilityLocations, ...facilityLocationCollection];
      vi.spyOn(facilityLocationService, 'addFacilityLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFacilityLocation });
      comp.ngOnInit();

      expect(facilityLocationService.query).toHaveBeenCalled();
      expect(facilityLocationService.addFacilityLocationToCollectionIfMissing).toHaveBeenCalledWith(
        facilityLocationCollection,
        ...additionalFacilityLocations.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilityLocationsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productFacilityLocation: IProductFacilityLocation = { id: 15870 };
      const product: IProduct = { id: 21536 };
      productFacilityLocation.product = product;
      const facility: IFacility = { id: 4866 };
      productFacilityLocation.facility = facility;
      const facilityLocation: IFacilityLocation = { id: 76 };
      productFacilityLocation.facilityLocation = facilityLocation;

      activatedRoute.data = of({ productFacilityLocation });
      comp.ngOnInit();

      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.facilitiesSharedCollection()).toContainEqual(facility);
      expect(comp.facilityLocationsSharedCollection()).toContainEqual(facilityLocation);
      expect(comp.productFacilityLocation).toEqual(productFacilityLocation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFacilityLocation>();
      const productFacilityLocation = { id: 22461 };
      vi.spyOn(productFacilityLocationFormService, 'getProductFacilityLocation').mockReturnValue(productFacilityLocation);
      vi.spyOn(productFacilityLocationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFacilityLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFacilityLocation);
      saveSubject.complete();

      // THEN
      expect(productFacilityLocationFormService.getProductFacilityLocation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productFacilityLocationService.update).toHaveBeenCalledWith(expect.objectContaining(productFacilityLocation));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFacilityLocation>();
      const productFacilityLocation = { id: 22461 };
      vi.spyOn(productFacilityLocationFormService, 'getProductFacilityLocation').mockReturnValue({ id: null });
      vi.spyOn(productFacilityLocationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFacilityLocation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFacilityLocation);
      saveSubject.complete();

      // THEN
      expect(productFacilityLocationFormService.getProductFacilityLocation).toHaveBeenCalled();
      expect(productFacilityLocationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFacilityLocation>();
      const productFacilityLocation = { id: 22461 };
      vi.spyOn(productFacilityLocationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFacilityLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productFacilityLocationService.update).toHaveBeenCalled();
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

    describe('compareFacilityLocation', () => {
      it('should forward to facilityLocationService', () => {
        const entity = { id: 76 };
        const entity2 = { id: 28655 };
        vi.spyOn(facilityLocationService, 'compareFacilityLocation');
        comp.compareFacilityLocation(entity, entity2);
        expect(facilityLocationService.compareFacilityLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
