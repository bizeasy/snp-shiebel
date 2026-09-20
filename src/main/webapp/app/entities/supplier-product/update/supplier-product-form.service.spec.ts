import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../supplier-product.test-samples';

import { SupplierProductFormService } from './supplier-product-form.service';

describe('SupplierProduct Form Service', () => {
  let service: SupplierProductFormService;

  beforeEach(() => {
    service = TestBed.inject(SupplierProductFormService);
  });

  describe('Service methods', () => {
    describe('createSupplierProductFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSupplierProductFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            supplierProductId: expect.any(Object),
            minOrderQty: expect.any(Object),
            lastPrice: expect.any(Object),
            shippingPrice: expect.any(Object),
            cgst: expect.any(Object),
            igst: expect.any(Object),
            sgst: expect.any(Object),
            totalPrice: expect.any(Object),
            comments: expect.any(Object),
            leadTimeDays: expect.any(Object),
            unitsIncluded: expect.any(Object),
            canDropShip: expect.any(Object),
            product: expect.any(Object),
            party: expect.any(Object),
            quantityUom: expect.any(Object),
            currencyUom: expect.any(Object),
          }),
        );
      });

      it('passing ISupplierProduct should create a new form with FormGroup', () => {
        const formGroup = service.createSupplierProductFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            supplierProductId: expect.any(Object),
            minOrderQty: expect.any(Object),
            lastPrice: expect.any(Object),
            shippingPrice: expect.any(Object),
            cgst: expect.any(Object),
            igst: expect.any(Object),
            sgst: expect.any(Object),
            totalPrice: expect.any(Object),
            comments: expect.any(Object),
            leadTimeDays: expect.any(Object),
            unitsIncluded: expect.any(Object),
            canDropShip: expect.any(Object),
            product: expect.any(Object),
            party: expect.any(Object),
            quantityUom: expect.any(Object),
            currencyUom: expect.any(Object),
          }),
        );
      });
    });

    describe('getSupplierProduct', () => {
      it('should return NewSupplierProduct for default SupplierProduct initial value', () => {
        const formGroup = service.createSupplierProductFormGroup(sampleWithNewData);

        const supplierProduct = service.getSupplierProduct(formGroup);

        expect(supplierProduct).toMatchObject(sampleWithNewData);
      });

      it('should return NewSupplierProduct for empty SupplierProduct initial value', () => {
        const formGroup = service.createSupplierProductFormGroup();

        const supplierProduct = service.getSupplierProduct(formGroup);

        expect(supplierProduct).toMatchObject({});
      });

      it('should return ISupplierProduct', () => {
        const formGroup = service.createSupplierProductFormGroup(sampleWithRequiredData);

        const supplierProduct = service.getSupplierProduct(formGroup);

        expect(supplierProduct).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISupplierProduct should not enable id FormControl', () => {
        const formGroup = service.createSupplierProductFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSupplierProduct should disable id FormControl', () => {
        const formGroup = service.createSupplierProductFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
