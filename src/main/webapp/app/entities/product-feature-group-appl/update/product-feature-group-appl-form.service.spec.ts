import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-feature-group-appl.test-samples';

import { ProductFeatureGroupApplFormService } from './product-feature-group-appl-form.service';

describe('ProductFeatureGroupAppl Form Service', () => {
  let service: ProductFeatureGroupApplFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFeatureGroupApplFormService);
  });

  describe('Service methods', () => {
    describe('createProductFeatureGroupApplFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFeatureGroupApplFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            sequenceNo: expect.any(Object),
            productFeatureGroup: expect.any(Object),
            productFeature: expect.any(Object),
          }),
        );
      });

      it('passing IProductFeatureGroupAppl should create a new form with FormGroup', () => {
        const formGroup = service.createProductFeatureGroupApplFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            sequenceNo: expect.any(Object),
            productFeatureGroup: expect.any(Object),
            productFeature: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductFeatureGroupAppl', () => {
      it('should return NewProductFeatureGroupAppl for default ProductFeatureGroupAppl initial value', () => {
        const formGroup = service.createProductFeatureGroupApplFormGroup(sampleWithNewData);

        const productFeatureGroupAppl = service.getProductFeatureGroupAppl(formGroup);

        expect(productFeatureGroupAppl).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductFeatureGroupAppl for empty ProductFeatureGroupAppl initial value', () => {
        const formGroup = service.createProductFeatureGroupApplFormGroup();

        const productFeatureGroupAppl = service.getProductFeatureGroupAppl(formGroup);

        expect(productFeatureGroupAppl).toMatchObject({});
      });

      it('should return IProductFeatureGroupAppl', () => {
        const formGroup = service.createProductFeatureGroupApplFormGroup(sampleWithRequiredData);

        const productFeatureGroupAppl = service.getProductFeatureGroupAppl(formGroup);

        expect(productFeatureGroupAppl).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductFeatureGroupAppl should not enable id FormControl', () => {
        const formGroup = service.createProductFeatureGroupApplFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductFeatureGroupAppl should disable id FormControl', () => {
        const formGroup = service.createProductFeatureGroupApplFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
