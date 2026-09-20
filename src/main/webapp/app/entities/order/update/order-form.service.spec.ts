import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order.test-samples';

import { OrderFormService } from './order-form.service';

describe('Order Form Service', () => {
  let service: OrderFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderFormService);
  });

  describe('Service methods', () => {
    describe('createOrderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            orderRefNo: expect.any(Object),
            externalId: expect.any(Object),
            orderDate: expect.any(Object),
            entryDate: expect.any(Object),
            deliveryDate: expect.any(Object),
            priority: expect.any(Object),
            isRushOrder: expect.any(Object),
            needsInventoryIssuance: expect.any(Object),
            remainingSubTotal: expect.any(Object),
            grandTotal: expect.any(Object),
            projectName: expect.any(Object),
            version: expect.any(Object),
            versionRef: expect.any(Object),
            description: expect.any(Object),
            remarks: expect.any(Object),
            paymentTerms: expect.any(Object),
            deliveryTerms: expect.any(Object),
            info: expect.any(Object),
            attr1: expect.any(Object),
            attr2: expect.any(Object),
            attr3: expect.any(Object),
            orderType: expect.any(Object),
            salesChannel: expect.any(Object),
            party: expect.any(Object),
            supplier: expect.any(Object),
            status: expect.any(Object),
            currencyUom: expect.any(Object),
            currencyConversion: expect.any(Object),
            quote: expect.any(Object),
            facility: expect.any(Object),
            productStore: expect.any(Object),
            department: expect.any(Object),
            customTimePeriod: expect.any(Object),
          }),
        );
      });

      it('passing IOrder should create a new form with FormGroup', () => {
        const formGroup = service.createOrderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            orderRefNo: expect.any(Object),
            externalId: expect.any(Object),
            orderDate: expect.any(Object),
            entryDate: expect.any(Object),
            deliveryDate: expect.any(Object),
            priority: expect.any(Object),
            isRushOrder: expect.any(Object),
            needsInventoryIssuance: expect.any(Object),
            remainingSubTotal: expect.any(Object),
            grandTotal: expect.any(Object),
            projectName: expect.any(Object),
            version: expect.any(Object),
            versionRef: expect.any(Object),
            description: expect.any(Object),
            remarks: expect.any(Object),
            paymentTerms: expect.any(Object),
            deliveryTerms: expect.any(Object),
            info: expect.any(Object),
            attr1: expect.any(Object),
            attr2: expect.any(Object),
            attr3: expect.any(Object),
            orderType: expect.any(Object),
            salesChannel: expect.any(Object),
            party: expect.any(Object),
            supplier: expect.any(Object),
            status: expect.any(Object),
            currencyUom: expect.any(Object),
            currencyConversion: expect.any(Object),
            quote: expect.any(Object),
            facility: expect.any(Object),
            productStore: expect.any(Object),
            department: expect.any(Object),
            customTimePeriod: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrder', () => {
      it('should return NewOrder for default Order initial value', () => {
        const formGroup = service.createOrderFormGroup(sampleWithNewData);

        const order = service.getOrder(formGroup);

        expect(order).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrder for empty Order initial value', () => {
        const formGroup = service.createOrderFormGroup();

        const order = service.getOrder(formGroup);

        expect(order).toMatchObject({});
      });

      it('should return IOrder', () => {
        const formGroup = service.createOrderFormGroup(sampleWithRequiredData);

        const order = service.getOrder(formGroup);

        expect(order).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrder should not enable id FormControl', () => {
        const formGroup = service.createOrderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrder should disable id FormControl', () => {
        const formGroup = service.createOrderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
