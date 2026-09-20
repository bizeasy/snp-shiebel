import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-price-purpose.test-samples';

import { ProductPricePurposeFormService } from './product-price-purpose-form.service';

describe('ProductPricePurpose Form Service', () => {
  let service: ProductPricePurposeFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductPricePurposeFormService);
  });

  describe('Service methods', () => {
    describe('createProductPricePurposeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductPricePurposeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IProductPricePurpose should create a new form with FormGroup', () => {
        const formGroup = service.createProductPricePurposeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductPricePurpose', () => {
      it('should return NewProductPricePurpose for default ProductPricePurpose initial value', () => {
        const formGroup = service.createProductPricePurposeFormGroup(sampleWithNewData);

        const productPricePurpose = service.getProductPricePurpose(formGroup);

        expect(productPricePurpose).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductPricePurpose for empty ProductPricePurpose initial value', () => {
        const formGroup = service.createProductPricePurposeFormGroup();

        const productPricePurpose = service.getProductPricePurpose(formGroup);

        expect(productPricePurpose).toMatchObject({});
      });

      it('should return IProductPricePurpose', () => {
        const formGroup = service.createProductPricePurposeFormGroup(sampleWithRequiredData);

        const productPricePurpose = service.getProductPricePurpose(formGroup);

        expect(productPricePurpose).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductPricePurpose should not enable id FormControl', () => {
        const formGroup = service.createProductPricePurposeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductPricePurpose should disable id FormControl', () => {
        const formGroup = service.createProductPricePurposeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
