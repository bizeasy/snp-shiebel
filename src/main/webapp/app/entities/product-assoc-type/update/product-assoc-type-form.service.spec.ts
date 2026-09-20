import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-assoc-type.test-samples';

import { ProductAssocTypeFormService } from './product-assoc-type-form.service';

describe('ProductAssocType Form Service', () => {
  let service: ProductAssocTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductAssocTypeFormService);
  });

  describe('Service methods', () => {
    describe('createProductAssocTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductAssocTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IProductAssocType should create a new form with FormGroup', () => {
        const formGroup = service.createProductAssocTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductAssocType', () => {
      it('should return NewProductAssocType for default ProductAssocType initial value', () => {
        const formGroup = service.createProductAssocTypeFormGroup(sampleWithNewData);

        const productAssocType = service.getProductAssocType(formGroup);

        expect(productAssocType).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductAssocType for empty ProductAssocType initial value', () => {
        const formGroup = service.createProductAssocTypeFormGroup();

        const productAssocType = service.getProductAssocType(formGroup);

        expect(productAssocType).toMatchObject({});
      });

      it('should return IProductAssocType', () => {
        const formGroup = service.createProductAssocTypeFormGroup(sampleWithRequiredData);

        const productAssocType = service.getProductAssocType(formGroup);

        expect(productAssocType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductAssocType should not enable id FormControl', () => {
        const formGroup = service.createProductAssocTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductAssocType should disable id FormControl', () => {
        const formGroup = service.createProductAssocTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
