import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-keyword.test-samples';

import { ProductKeywordFormService } from './product-keyword-form.service';

describe('ProductKeyword Form Service', () => {
  let service: ProductKeywordFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductKeywordFormService);
  });

  describe('Service methods', () => {
    describe('createProductKeywordFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductKeywordFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            keyword: expect.any(Object),
            relevancyWeight: expect.any(Object),
            product: expect.any(Object),
            keywordType: expect.any(Object),
          }),
        );
      });

      it('passing IProductKeyword should create a new form with FormGroup', () => {
        const formGroup = service.createProductKeywordFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            keyword: expect.any(Object),
            relevancyWeight: expect.any(Object),
            product: expect.any(Object),
            keywordType: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductKeyword', () => {
      it('should return NewProductKeyword for default ProductKeyword initial value', () => {
        const formGroup = service.createProductKeywordFormGroup(sampleWithNewData);

        const productKeyword = service.getProductKeyword(formGroup);

        expect(productKeyword).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductKeyword for empty ProductKeyword initial value', () => {
        const formGroup = service.createProductKeywordFormGroup();

        const productKeyword = service.getProductKeyword(formGroup);

        expect(productKeyword).toMatchObject({});
      });

      it('should return IProductKeyword', () => {
        const formGroup = service.createProductKeywordFormGroup(sampleWithRequiredData);

        const productKeyword = service.getProductKeyword(formGroup);

        expect(productKeyword).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductKeyword should not enable id FormControl', () => {
        const formGroup = service.createProductKeywordFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductKeyword should disable id FormControl', () => {
        const formGroup = service.createProductKeywordFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
