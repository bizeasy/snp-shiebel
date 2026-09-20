import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-facility-user.test-samples';

import { ProductFacilityUserFormService } from './product-facility-user-form.service';

describe('ProductFacilityUser Form Service', () => {
  let service: ProductFacilityUserFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFacilityUserFormService);
  });

  describe('Service methods', () => {
    describe('createProductFacilityUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFacilityUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            costPerDay: expect.any(Object),
            totalCost: expect.any(Object),
            discount: expect.any(Object),
            productFacility: expect.any(Object),
            party: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });

      it('passing IProductFacilityUser should create a new form with FormGroup', () => {
        const formGroup = service.createProductFacilityUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            costPerDay: expect.any(Object),
            totalCost: expect.any(Object),
            discount: expect.any(Object),
            productFacility: expect.any(Object),
            party: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductFacilityUser', () => {
      it('should return NewProductFacilityUser for default ProductFacilityUser initial value', () => {
        const formGroup = service.createProductFacilityUserFormGroup(sampleWithNewData);

        const productFacilityUser = service.getProductFacilityUser(formGroup);

        expect(productFacilityUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductFacilityUser for empty ProductFacilityUser initial value', () => {
        const formGroup = service.createProductFacilityUserFormGroup();

        const productFacilityUser = service.getProductFacilityUser(formGroup);

        expect(productFacilityUser).toMatchObject({});
      });

      it('should return IProductFacilityUser', () => {
        const formGroup = service.createProductFacilityUserFormGroup(sampleWithRequiredData);

        const productFacilityUser = service.getProductFacilityUser(formGroup);

        expect(productFacilityUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductFacilityUser should not enable id FormControl', () => {
        const formGroup = service.createProductFacilityUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductFacilityUser should disable id FormControl', () => {
        const formGroup = service.createProductFacilityUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
