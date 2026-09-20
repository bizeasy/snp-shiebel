import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-feature-type.test-samples';

import { ProductFeatureTypeFormService } from './product-feature-type-form.service';

describe('ProductFeatureType Form Service', () => {
  let service: ProductFeatureTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFeatureTypeFormService);
  });

  describe('Service methods', () => {
    describe('createProductFeatureTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFeatureTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IProductFeatureType should create a new form with FormGroup', () => {
        const formGroup = service.createProductFeatureTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductFeatureType', () => {
      it('should return NewProductFeatureType for default ProductFeatureType initial value', () => {
        const formGroup = service.createProductFeatureTypeFormGroup(sampleWithNewData);

        const productFeatureType = service.getProductFeatureType(formGroup);

        expect(productFeatureType).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductFeatureType for empty ProductFeatureType initial value', () => {
        const formGroup = service.createProductFeatureTypeFormGroup();

        const productFeatureType = service.getProductFeatureType(formGroup);

        expect(productFeatureType).toMatchObject({});
      });

      it('should return IProductFeatureType', () => {
        const formGroup = service.createProductFeatureTypeFormGroup(sampleWithRequiredData);

        const productFeatureType = service.getProductFeatureType(formGroup);

        expect(productFeatureType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductFeatureType should not enable id FormControl', () => {
        const formGroup = service.createProductFeatureTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductFeatureType should disable id FormControl', () => {
        const formGroup = service.createProductFeatureTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
