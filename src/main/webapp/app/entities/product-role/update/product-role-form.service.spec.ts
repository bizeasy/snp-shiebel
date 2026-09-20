import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-role.test-samples';

import { ProductRoleFormService } from './product-role-form.service';

describe('ProductRole Form Service', () => {
  let service: ProductRoleFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductRoleFormService);
  });

  describe('Service methods', () => {
    describe('createProductRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            product: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });

      it('passing IProductRole should create a new form with FormGroup', () => {
        const formGroup = service.createProductRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            product: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductRole', () => {
      it('should return NewProductRole for default ProductRole initial value', () => {
        const formGroup = service.createProductRoleFormGroup(sampleWithNewData);

        const productRole = service.getProductRole(formGroup);

        expect(productRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductRole for empty ProductRole initial value', () => {
        const formGroup = service.createProductRoleFormGroup();

        const productRole = service.getProductRole(formGroup);

        expect(productRole).toMatchObject({});
      });

      it('should return IProductRole', () => {
        const formGroup = service.createProductRoleFormGroup(sampleWithRequiredData);

        const productRole = service.getProductRole(formGroup);

        expect(productRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductRole should not enable id FormControl', () => {
        const formGroup = service.createProductRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductRole should disable id FormControl', () => {
        const formGroup = service.createProductRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
