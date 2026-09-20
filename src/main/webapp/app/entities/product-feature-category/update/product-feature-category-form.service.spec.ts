import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-feature-category.test-samples';

import { ProductFeatureCategoryFormService } from './product-feature-category-form.service';

describe('ProductFeatureCategory Form Service', () => {
  let service: ProductFeatureCategoryFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFeatureCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createProductFeatureCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFeatureCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IProductFeatureCategory should create a new form with FormGroup', () => {
        const formGroup = service.createProductFeatureCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductFeatureCategory', () => {
      it('should return NewProductFeatureCategory for default ProductFeatureCategory initial value', () => {
        const formGroup = service.createProductFeatureCategoryFormGroup(sampleWithNewData);

        const productFeatureCategory = service.getProductFeatureCategory(formGroup);

        expect(productFeatureCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductFeatureCategory for empty ProductFeatureCategory initial value', () => {
        const formGroup = service.createProductFeatureCategoryFormGroup();

        const productFeatureCategory = service.getProductFeatureCategory(formGroup);

        expect(productFeatureCategory).toMatchObject({});
      });

      it('should return IProductFeatureCategory', () => {
        const formGroup = service.createProductFeatureCategoryFormGroup(sampleWithRequiredData);

        const productFeatureCategory = service.getProductFeatureCategory(formGroup);

        expect(productFeatureCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductFeatureCategory should not enable id FormControl', () => {
        const formGroup = service.createProductFeatureCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductFeatureCategory should disable id FormControl', () => {
        const formGroup = service.createProductFeatureCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
