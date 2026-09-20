import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-store.test-samples';

import { ProductStoreFormService } from './product-store-form.service';

describe('ProductStore Form Service', () => {
  let service: ProductStoreFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductStoreFormService);
  });

  describe('Service methods', () => {
    describe('createProductStoreFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductStoreFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            title: expect.any(Object),
            postalAddress: expect.any(Object),
            owner: expect.any(Object),
            admin: expect.any(Object),
          }),
        );
      });

      it('passing IProductStore should create a new form with FormGroup', () => {
        const formGroup = service.createProductStoreFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            title: expect.any(Object),
            postalAddress: expect.any(Object),
            owner: expect.any(Object),
            admin: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductStore', () => {
      it('should return NewProductStore for default ProductStore initial value', () => {
        const formGroup = service.createProductStoreFormGroup(sampleWithNewData);

        const productStore = service.getProductStore(formGroup);

        expect(productStore).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductStore for empty ProductStore initial value', () => {
        const formGroup = service.createProductStoreFormGroup();

        const productStore = service.getProductStore(formGroup);

        expect(productStore).toMatchObject({});
      });

      it('should return IProductStore', () => {
        const formGroup = service.createProductStoreFormGroup(sampleWithRequiredData);

        const productStore = service.getProductStore(formGroup);

        expect(productStore).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductStore should not enable id FormControl', () => {
        const formGroup = service.createProductStoreFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductStore should disable id FormControl', () => {
        const formGroup = service.createProductStoreFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
