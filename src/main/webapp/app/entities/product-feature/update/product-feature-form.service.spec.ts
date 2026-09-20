import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-feature.test-samples';

import { ProductFeatureFormService } from './product-feature-form.service';

describe('ProductFeature Form Service', () => {
  let service: ProductFeatureFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFeatureFormService);
  });

  describe('Service methods', () => {
    describe('createProductFeatureFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFeatureFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            amount: expect.any(Object),
            sequenceNo: expect.any(Object),
            abbrev: expect.any(Object),
            productFeatureType: expect.any(Object),
            productFeatureCategory: expect.any(Object),
          }),
        );
      });

      it('passing IProductFeature should create a new form with FormGroup', () => {
        const formGroup = service.createProductFeatureFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            amount: expect.any(Object),
            sequenceNo: expect.any(Object),
            abbrev: expect.any(Object),
            productFeatureType: expect.any(Object),
            productFeatureCategory: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductFeature', () => {
      it('should return NewProductFeature for default ProductFeature initial value', () => {
        const formGroup = service.createProductFeatureFormGroup(sampleWithNewData);

        const productFeature = service.getProductFeature(formGroup);

        expect(productFeature).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductFeature for empty ProductFeature initial value', () => {
        const formGroup = service.createProductFeatureFormGroup();

        const productFeature = service.getProductFeature(formGroup);

        expect(productFeature).toMatchObject({});
      });

      it('should return IProductFeature', () => {
        const formGroup = service.createProductFeatureFormGroup(sampleWithRequiredData);

        const productFeature = service.getProductFeature(formGroup);

        expect(productFeature).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductFeature should not enable id FormControl', () => {
        const formGroup = service.createProductFeatureFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductFeature should disable id FormControl', () => {
        const formGroup = service.createProductFeatureFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
