import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-price-type.test-samples';

import { ProductPriceTypeFormService } from './product-price-type-form.service';

describe('ProductPriceType Form Service', () => {
  let service: ProductPriceTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductPriceTypeFormService);
  });

  describe('Service methods', () => {
    describe('createProductPriceTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductPriceTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IProductPriceType should create a new form with FormGroup', () => {
        const formGroup = service.createProductPriceTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductPriceType', () => {
      it('should return NewProductPriceType for default ProductPriceType initial value', () => {
        const formGroup = service.createProductPriceTypeFormGroup(sampleWithNewData);

        const productPriceType = service.getProductPriceType(formGroup);

        expect(productPriceType).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductPriceType for empty ProductPriceType initial value', () => {
        const formGroup = service.createProductPriceTypeFormGroup();

        const productPriceType = service.getProductPriceType(formGroup);

        expect(productPriceType).toMatchObject({});
      });

      it('should return IProductPriceType', () => {
        const formGroup = service.createProductPriceTypeFormGroup(sampleWithRequiredData);

        const productPriceType = service.getProductPriceType(formGroup);

        expect(productPriceType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductPriceType should not enable id FormControl', () => {
        const formGroup = service.createProductPriceTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductPriceType should disable id FormControl', () => {
        const formGroup = service.createProductPriceTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
