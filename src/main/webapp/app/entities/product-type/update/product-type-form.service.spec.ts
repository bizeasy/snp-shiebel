import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-type.test-samples';

import { ProductTypeFormService } from './product-type-form.service';

describe('ProductType Form Service', () => {
  let service: ProductTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductTypeFormService);
  });

  describe('Service methods', () => {
    describe('createProductTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IProductType should create a new form with FormGroup', () => {
        const formGroup = service.createProductTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductType', () => {
      it('should return NewProductType for default ProductType initial value', () => {
        const formGroup = service.createProductTypeFormGroup(sampleWithNewData);

        const productType = service.getProductType(formGroup);

        expect(productType).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductType for empty ProductType initial value', () => {
        const formGroup = service.createProductTypeFormGroup();

        const productType = service.getProductType(formGroup);

        expect(productType).toMatchObject({});
      });

      it('should return IProductType', () => {
        const formGroup = service.createProductTypeFormGroup(sampleWithRequiredData);

        const productType = service.getProductType(formGroup);

        expect(productType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductType should not enable id FormControl', () => {
        const formGroup = service.createProductTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductType should disable id FormControl', () => {
        const formGroup = service.createProductTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
