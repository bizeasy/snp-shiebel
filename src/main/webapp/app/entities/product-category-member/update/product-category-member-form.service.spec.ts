import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-category-member.test-samples';

import { ProductCategoryMemberFormService } from './product-category-member-form.service';

describe('ProductCategoryMember Form Service', () => {
  let service: ProductCategoryMemberFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductCategoryMemberFormService);
  });

  describe('Service methods', () => {
    describe('createProductCategoryMemberFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductCategoryMemberFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            sequenceNo: expect.any(Object),
            product: expect.any(Object),
            productCategory: expect.any(Object),
          }),
        );
      });

      it('passing IProductCategoryMember should create a new form with FormGroup', () => {
        const formGroup = service.createProductCategoryMemberFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            sequenceNo: expect.any(Object),
            product: expect.any(Object),
            productCategory: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductCategoryMember', () => {
      it('should return NewProductCategoryMember for default ProductCategoryMember initial value', () => {
        const formGroup = service.createProductCategoryMemberFormGroup(sampleWithNewData);

        const productCategoryMember = service.getProductCategoryMember(formGroup);

        expect(productCategoryMember).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductCategoryMember for empty ProductCategoryMember initial value', () => {
        const formGroup = service.createProductCategoryMemberFormGroup();

        const productCategoryMember = service.getProductCategoryMember(formGroup);

        expect(productCategoryMember).toMatchObject({});
      });

      it('should return IProductCategoryMember', () => {
        const formGroup = service.createProductCategoryMemberFormGroup(sampleWithRequiredData);

        const productCategoryMember = service.getProductCategoryMember(formGroup);

        expect(productCategoryMember).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductCategoryMember should not enable id FormControl', () => {
        const formGroup = service.createProductCategoryMemberFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductCategoryMember should disable id FormControl', () => {
        const formGroup = service.createProductCategoryMemberFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
