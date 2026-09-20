import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-adjustment.test-samples';

import { OrderAdjustmentFormService } from './order-adjustment-form.service';

describe('OrderAdjustment Form Service', () => {
  let service: OrderAdjustmentFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderAdjustmentFormService);
  });

  describe('Service methods', () => {
    describe('createOrderAdjustmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderAdjustmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            comments: expect.any(Object),
            amount: expect.any(Object),
            sourcePercentage: expect.any(Object),
            sourceRefId: expect.any(Object),
            exemptAmount: expect.any(Object),
            includeInTax: expect.any(Object),
            includeInShipping: expect.any(Object),
            includedInPrice: expect.any(Object),
            entryDate: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            adjustmentType: expect.any(Object),
            product: expect.any(Object),
            productFeature: expect.any(Object),
            taxAuthority: expect.any(Object),
          }),
        );
      });

      it('passing IOrderAdjustment should create a new form with FormGroup', () => {
        const formGroup = service.createOrderAdjustmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            comments: expect.any(Object),
            amount: expect.any(Object),
            sourcePercentage: expect.any(Object),
            sourceRefId: expect.any(Object),
            exemptAmount: expect.any(Object),
            includeInTax: expect.any(Object),
            includeInShipping: expect.any(Object),
            includedInPrice: expect.any(Object),
            entryDate: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            adjustmentType: expect.any(Object),
            product: expect.any(Object),
            productFeature: expect.any(Object),
            taxAuthority: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderAdjustment', () => {
      it('should return NewOrderAdjustment for default OrderAdjustment initial value', () => {
        const formGroup = service.createOrderAdjustmentFormGroup(sampleWithNewData);

        const orderAdjustment = service.getOrderAdjustment(formGroup);

        expect(orderAdjustment).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderAdjustment for empty OrderAdjustment initial value', () => {
        const formGroup = service.createOrderAdjustmentFormGroup();

        const orderAdjustment = service.getOrderAdjustment(formGroup);

        expect(orderAdjustment).toMatchObject({});
      });

      it('should return IOrderAdjustment', () => {
        const formGroup = service.createOrderAdjustmentFormGroup(sampleWithRequiredData);

        const orderAdjustment = service.getOrderAdjustment(formGroup);

        expect(orderAdjustment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderAdjustment should not enable id FormControl', () => {
        const formGroup = service.createOrderAdjustmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderAdjustment should disable id FormControl', () => {
        const formGroup = service.createOrderAdjustmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
