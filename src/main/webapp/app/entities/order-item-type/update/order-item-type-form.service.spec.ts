import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-item-type.test-samples';

import { OrderItemTypeFormService } from './order-item-type-form.service';

describe('OrderItemType Form Service', () => {
  let service: OrderItemTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderItemTypeFormService);
  });

  describe('Service methods', () => {
    describe('createOrderItemTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderItemTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IOrderItemType should create a new form with FormGroup', () => {
        const formGroup = service.createOrderItemTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderItemType', () => {
      it('should return NewOrderItemType for default OrderItemType initial value', () => {
        const formGroup = service.createOrderItemTypeFormGroup(sampleWithNewData);

        const orderItemType = service.getOrderItemType(formGroup);

        expect(orderItemType).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderItemType for empty OrderItemType initial value', () => {
        const formGroup = service.createOrderItemTypeFormGroup();

        const orderItemType = service.getOrderItemType(formGroup);

        expect(orderItemType).toMatchObject({});
      });

      it('should return IOrderItemType', () => {
        const formGroup = service.createOrderItemTypeFormGroup(sampleWithRequiredData);

        const orderItemType = service.getOrderItemType(formGroup);

        expect(orderItemType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderItemType should not enable id FormControl', () => {
        const formGroup = service.createOrderItemTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderItemType should disable id FormControl', () => {
        const formGroup = service.createOrderItemTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
