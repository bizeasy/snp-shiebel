import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-facility.test-samples';

import { ProductFacilityFormService } from './product-facility-form.service';

describe('ProductFacility Form Service', () => {
  let service: ProductFacilityFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFacilityFormService);
  });

  describe('Service methods', () => {
    describe('createProductFacilityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFacilityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            minimumStock: expect.any(Object),
            reorderQty: expect.any(Object),
            daysToShip: expect.any(Object),
            lastInventoryCount: expect.any(Object),
            product: expect.any(Object),
            facility: expect.any(Object),
          }),
        );
      });

      it('passing IProductFacility should create a new form with FormGroup', () => {
        const formGroup = service.createProductFacilityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            minimumStock: expect.any(Object),
            reorderQty: expect.any(Object),
            daysToShip: expect.any(Object),
            lastInventoryCount: expect.any(Object),
            product: expect.any(Object),
            facility: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductFacility', () => {
      it('should return NewProductFacility for default ProductFacility initial value', () => {
        const formGroup = service.createProductFacilityFormGroup(sampleWithNewData);

        const productFacility = service.getProductFacility(formGroup);

        expect(productFacility).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductFacility for empty ProductFacility initial value', () => {
        const formGroup = service.createProductFacilityFormGroup();

        const productFacility = service.getProductFacility(formGroup);

        expect(productFacility).toMatchObject({});
      });

      it('should return IProductFacility', () => {
        const formGroup = service.createProductFacilityFormGroup(sampleWithRequiredData);

        const productFacility = service.getProductFacility(formGroup);

        expect(productFacility).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductFacility should not enable id FormControl', () => {
        const formGroup = service.createProductFacilityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductFacility should disable id FormControl', () => {
        const formGroup = service.createProductFacilityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
