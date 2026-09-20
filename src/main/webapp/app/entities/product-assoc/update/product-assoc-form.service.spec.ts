import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-assoc.test-samples';

import { ProductAssocFormService } from './product-assoc-form.service';

describe('ProductAssoc Form Service', () => {
  let service: ProductAssocFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductAssocFormService);
  });

  describe('Service methods', () => {
    describe('createProductAssocFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductAssocFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            product: expect.any(Object),
            productTo: expect.any(Object),
            productAssocType: expect.any(Object),
          }),
        );
      });

      it('passing IProductAssoc should create a new form with FormGroup', () => {
        const formGroup = service.createProductAssocFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            product: expect.any(Object),
            productTo: expect.any(Object),
            productAssocType: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductAssoc', () => {
      it('should return NewProductAssoc for default ProductAssoc initial value', () => {
        const formGroup = service.createProductAssocFormGroup(sampleWithNewData);

        const productAssoc = service.getProductAssoc(formGroup);

        expect(productAssoc).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductAssoc for empty ProductAssoc initial value', () => {
        const formGroup = service.createProductAssocFormGroup();

        const productAssoc = service.getProductAssoc(formGroup);

        expect(productAssoc).toMatchObject({});
      });

      it('should return IProductAssoc', () => {
        const formGroup = service.createProductAssocFormGroup(sampleWithRequiredData);

        const productAssoc = service.getProductAssoc(formGroup);

        expect(productAssoc).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductAssoc should not enable id FormControl', () => {
        const formGroup = service.createProductAssocFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductAssoc should disable id FormControl', () => {
        const formGroup = service.createProductAssocFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
