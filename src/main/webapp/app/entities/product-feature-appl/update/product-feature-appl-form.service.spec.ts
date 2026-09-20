import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-feature-appl.test-samples';

import { ProductFeatureApplFormService } from './product-feature-appl-form.service';

describe('ProductFeatureAppl Form Service', () => {
  let service: ProductFeatureApplFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFeatureApplFormService);
  });

  describe('Service methods', () => {
    describe('createProductFeatureApplFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFeatureApplFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            sequenceNo: expect.any(Object),
            amount: expect.any(Object),
            product: expect.any(Object),
            productFeature: expect.any(Object),
            featureApplType: expect.any(Object),
          }),
        );
      });

      it('passing IProductFeatureAppl should create a new form with FormGroup', () => {
        const formGroup = service.createProductFeatureApplFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            sequenceNo: expect.any(Object),
            amount: expect.any(Object),
            product: expect.any(Object),
            productFeature: expect.any(Object),
            featureApplType: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductFeatureAppl', () => {
      it('should return NewProductFeatureAppl for default ProductFeatureAppl initial value', () => {
        const formGroup = service.createProductFeatureApplFormGroup(sampleWithNewData);

        const productFeatureAppl = service.getProductFeatureAppl(formGroup);

        expect(productFeatureAppl).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductFeatureAppl for empty ProductFeatureAppl initial value', () => {
        const formGroup = service.createProductFeatureApplFormGroup();

        const productFeatureAppl = service.getProductFeatureAppl(formGroup);

        expect(productFeatureAppl).toMatchObject({});
      });

      it('should return IProductFeatureAppl', () => {
        const formGroup = service.createProductFeatureApplFormGroup(sampleWithRequiredData);

        const productFeatureAppl = service.getProductFeatureAppl(formGroup);

        expect(productFeatureAppl).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductFeatureAppl should not enable id FormControl', () => {
        const formGroup = service.createProductFeatureApplFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductFeatureAppl should disable id FormControl', () => {
        const formGroup = service.createProductFeatureApplFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
