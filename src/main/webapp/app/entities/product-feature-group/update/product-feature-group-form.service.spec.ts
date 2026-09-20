import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-feature-group.test-samples';

import { ProductFeatureGroupFormService } from './product-feature-group-form.service';

describe('ProductFeatureGroup Form Service', () => {
  let service: ProductFeatureGroupFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFeatureGroupFormService);
  });

  describe('Service methods', () => {
    describe('createProductFeatureGroupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFeatureGroupFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IProductFeatureGroup should create a new form with FormGroup', () => {
        const formGroup = service.createProductFeatureGroupFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductFeatureGroup', () => {
      it('should return NewProductFeatureGroup for default ProductFeatureGroup initial value', () => {
        const formGroup = service.createProductFeatureGroupFormGroup(sampleWithNewData);

        const productFeatureGroup = service.getProductFeatureGroup(formGroup);

        expect(productFeatureGroup).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductFeatureGroup for empty ProductFeatureGroup initial value', () => {
        const formGroup = service.createProductFeatureGroupFormGroup();

        const productFeatureGroup = service.getProductFeatureGroup(formGroup);

        expect(productFeatureGroup).toMatchObject({});
      });

      it('should return IProductFeatureGroup', () => {
        const formGroup = service.createProductFeatureGroupFormGroup(sampleWithRequiredData);

        const productFeatureGroup = service.getProductFeatureGroup(formGroup);

        expect(productFeatureGroup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductFeatureGroup should not enable id FormControl', () => {
        const formGroup = service.createProductFeatureGroupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductFeatureGroup should disable id FormControl', () => {
        const formGroup = service.createProductFeatureGroupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
