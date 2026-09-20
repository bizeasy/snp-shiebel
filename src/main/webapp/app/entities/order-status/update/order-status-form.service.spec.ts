import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-status.test-samples';

import { OrderStatusFormService } from './order-status-form.service';

describe('OrderStatus Form Service', () => {
  let service: OrderStatusFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderStatusFormService);
  });

  describe('Service methods', () => {
    describe('createOrderStatusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderStatusFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statusDateTime: expect.any(Object),
            sequenceNo: expect.any(Object),
            order: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });

      it('passing IOrderStatus should create a new form with FormGroup', () => {
        const formGroup = service.createOrderStatusFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statusDateTime: expect.any(Object),
            sequenceNo: expect.any(Object),
            order: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderStatus', () => {
      it('should return NewOrderStatus for default OrderStatus initial value', () => {
        const formGroup = service.createOrderStatusFormGroup(sampleWithNewData);

        const orderStatus = service.getOrderStatus(formGroup);

        expect(orderStatus).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderStatus for empty OrderStatus initial value', () => {
        const formGroup = service.createOrderStatusFormGroup();

        const orderStatus = service.getOrderStatus(formGroup);

        expect(orderStatus).toMatchObject({});
      });

      it('should return IOrderStatus', () => {
        const formGroup = service.createOrderStatusFormGroup(sampleWithRequiredData);

        const orderStatus = service.getOrderStatus(formGroup);

        expect(orderStatus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderStatus should not enable id FormControl', () => {
        const formGroup = service.createOrderStatusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderStatus should disable id FormControl', () => {
        const formGroup = service.createOrderStatusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
