import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-facility-location.test-samples';

import { ProductFacilityLocationFormService } from './product-facility-location-form.service';

describe('ProductFacilityLocation Form Service', () => {
  let service: ProductFacilityLocationFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFacilityLocationFormService);
  });

  describe('Service methods', () => {
    describe('createProductFacilityLocationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFacilityLocationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            minimumStock: expect.any(Object),
            moveQuantity: expect.any(Object),
            product: expect.any(Object),
            facility: expect.any(Object),
            facilityLocation: expect.any(Object),
          }),
        );
      });

      it('passing IProductFacilityLocation should create a new form with FormGroup', () => {
        const formGroup = service.createProductFacilityLocationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            minimumStock: expect.any(Object),
            moveQuantity: expect.any(Object),
            product: expect.any(Object),
            facility: expect.any(Object),
            facilityLocation: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductFacilityLocation', () => {
      it('should return NewProductFacilityLocation for default ProductFacilityLocation initial value', () => {
        const formGroup = service.createProductFacilityLocationFormGroup(sampleWithNewData);

        const productFacilityLocation = service.getProductFacilityLocation(formGroup);

        expect(productFacilityLocation).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductFacilityLocation for empty ProductFacilityLocation initial value', () => {
        const formGroup = service.createProductFacilityLocationFormGroup();

        const productFacilityLocation = service.getProductFacilityLocation(formGroup);

        expect(productFacilityLocation).toMatchObject({});
      });

      it('should return IProductFacilityLocation', () => {
        const formGroup = service.createProductFacilityLocationFormGroup(sampleWithRequiredData);

        const productFacilityLocation = service.getProductFacilityLocation(formGroup);

        expect(productFacilityLocation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductFacilityLocation should not enable id FormControl', () => {
        const formGroup = service.createProductFacilityLocationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductFacilityLocation should disable id FormControl', () => {
        const formGroup = service.createProductFacilityLocationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
