import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-feature-category-appl.test-samples';

import { ProductFeatureCategoryApplFormService } from './product-feature-category-appl-form.service';

describe('ProductFeatureCategoryAppl Form Service', () => {
  let service: ProductFeatureCategoryApplFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFeatureCategoryApplFormService);
  });

  describe('Service methods', () => {
    describe('createProductFeatureCategoryApplFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFeatureCategoryApplFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            productCategory: expect.any(Object),
            productFeatureCategory: expect.any(Object),
          }),
        );
      });

      it('passing IProductFeatureCategoryAppl should create a new form with FormGroup', () => {
        const formGroup = service.createProductFeatureCategoryApplFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            productCategory: expect.any(Object),
            productFeatureCategory: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductFeatureCategoryAppl', () => {
      it('should return NewProductFeatureCategoryAppl for default ProductFeatureCategoryAppl initial value', () => {
        const formGroup = service.createProductFeatureCategoryApplFormGroup(sampleWithNewData);

        const productFeatureCategoryAppl = service.getProductFeatureCategoryAppl(formGroup);

        expect(productFeatureCategoryAppl).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductFeatureCategoryAppl for empty ProductFeatureCategoryAppl initial value', () => {
        const formGroup = service.createProductFeatureCategoryApplFormGroup();

        const productFeatureCategoryAppl = service.getProductFeatureCategoryAppl(formGroup);

        expect(productFeatureCategoryAppl).toMatchObject({});
      });

      it('should return IProductFeatureCategoryAppl', () => {
        const formGroup = service.createProductFeatureCategoryApplFormGroup(sampleWithRequiredData);

        const productFeatureCategoryAppl = service.getProductFeatureCategoryAppl(formGroup);

        expect(productFeatureCategoryAppl).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductFeatureCategoryAppl should not enable id FormControl', () => {
        const formGroup = service.createProductFeatureCategoryApplFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductFeatureCategoryAppl should disable id FormControl', () => {
        const formGroup = service.createProductFeatureCategoryApplFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
