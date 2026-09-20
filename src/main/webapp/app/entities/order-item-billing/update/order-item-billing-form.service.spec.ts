import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-item-billing.test-samples';

import { OrderItemBillingFormService } from './order-item-billing-form.service';

describe('OrderItemBilling Form Service', () => {
  let service: OrderItemBillingFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderItemBillingFormService);
  });

  describe('Service methods', () => {
    describe('createOrderItemBillingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderItemBillingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            amount: expect.any(Object),
            orderItem: expect.any(Object),
            invoiceItem: expect.any(Object),
            shipmentReceipt: expect.any(Object),
            itemIssuance: expect.any(Object),
          }),
        );
      });

      it('passing IOrderItemBilling should create a new form with FormGroup', () => {
        const formGroup = service.createOrderItemBillingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            amount: expect.any(Object),
            orderItem: expect.any(Object),
            invoiceItem: expect.any(Object),
            shipmentReceipt: expect.any(Object),
            itemIssuance: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderItemBilling', () => {
      it('should return NewOrderItemBilling for default OrderItemBilling initial value', () => {
        const formGroup = service.createOrderItemBillingFormGroup(sampleWithNewData);

        const orderItemBilling = service.getOrderItemBilling(formGroup);

        expect(orderItemBilling).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderItemBilling for empty OrderItemBilling initial value', () => {
        const formGroup = service.createOrderItemBillingFormGroup();

        const orderItemBilling = service.getOrderItemBilling(formGroup);

        expect(orderItemBilling).toMatchObject({});
      });

      it('should return IOrderItemBilling', () => {
        const formGroup = service.createOrderItemBillingFormGroup(sampleWithRequiredData);

        const orderItemBilling = service.getOrderItemBilling(formGroup);

        expect(orderItemBilling).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderItemBilling should not enable id FormControl', () => {
        const formGroup = service.createOrderItemBillingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderItemBilling should disable id FormControl', () => {
        const formGroup = service.createOrderItemBillingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
