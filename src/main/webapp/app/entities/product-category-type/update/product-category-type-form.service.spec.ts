import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-category-type.test-samples';

import { ProductCategoryTypeFormService } from './product-category-type-form.service';

describe('ProductCategoryType Form Service', () => {
  let service: ProductCategoryTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductCategoryTypeFormService);
  });

  describe('Service methods', () => {
    describe('createProductCategoryTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductCategoryTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IProductCategoryType should create a new form with FormGroup', () => {
        const formGroup = service.createProductCategoryTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductCategoryType', () => {
      it('should return NewProductCategoryType for default ProductCategoryType initial value', () => {
        const formGroup = service.createProductCategoryTypeFormGroup(sampleWithNewData);

        const productCategoryType = service.getProductCategoryType(formGroup);

        expect(productCategoryType).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductCategoryType for empty ProductCategoryType initial value', () => {
        const formGroup = service.createProductCategoryTypeFormGroup();

        const productCategoryType = service.getProductCategoryType(formGroup);

        expect(productCategoryType).toMatchObject({});
      });

      it('should return IProductCategoryType', () => {
        const formGroup = service.createProductCategoryTypeFormGroup(sampleWithRequiredData);

        const productCategoryType = service.getProductCategoryType(formGroup);

        expect(productCategoryType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductCategoryType should not enable id FormControl', () => {
        const formGroup = service.createProductCategoryTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductCategoryType should disable id FormControl', () => {
        const formGroup = service.createProductCategoryTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
