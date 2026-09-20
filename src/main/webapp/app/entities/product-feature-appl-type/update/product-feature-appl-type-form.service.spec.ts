import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-feature-appl-type.test-samples';

import { ProductFeatureApplTypeFormService } from './product-feature-appl-type-form.service';

describe('ProductFeatureApplType Form Service', () => {
  let service: ProductFeatureApplTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFeatureApplTypeFormService);
  });

  describe('Service methods', () => {
    describe('createProductFeatureApplTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFeatureApplTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IProductFeatureApplType should create a new form with FormGroup', () => {
        const formGroup = service.createProductFeatureApplTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductFeatureApplType', () => {
      it('should return NewProductFeatureApplType for default ProductFeatureApplType initial value', () => {
        const formGroup = service.createProductFeatureApplTypeFormGroup(sampleWithNewData);

        const productFeatureApplType = service.getProductFeatureApplType(formGroup);

        expect(productFeatureApplType).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductFeatureApplType for empty ProductFeatureApplType initial value', () => {
        const formGroup = service.createProductFeatureApplTypeFormGroup();

        const productFeatureApplType = service.getProductFeatureApplType(formGroup);

        expect(productFeatureApplType).toMatchObject({});
      });

      it('should return IProductFeatureApplType', () => {
        const formGroup = service.createProductFeatureApplTypeFormGroup(sampleWithRequiredData);

        const productFeatureApplType = service.getProductFeatureApplType(formGroup);

        expect(productFeatureApplType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductFeatureApplType should not enable id FormControl', () => {
        const formGroup = service.createProductFeatureApplTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductFeatureApplType should disable id FormControl', () => {
        const formGroup = service.createProductFeatureApplTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
