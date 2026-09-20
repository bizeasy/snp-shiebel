import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-type.test-samples';

import { OrderTypeFormService } from './order-type-form.service';

describe('OrderType Form Service', () => {
  let service: OrderTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderTypeFormService);
  });

  describe('Service methods', () => {
    describe('createOrderTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IOrderType should create a new form with FormGroup', () => {
        const formGroup = service.createOrderTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderType', () => {
      it('should return NewOrderType for default OrderType initial value', () => {
        const formGroup = service.createOrderTypeFormGroup(sampleWithNewData);

        const orderType = service.getOrderType(formGroup);

        expect(orderType).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderType for empty OrderType initial value', () => {
        const formGroup = service.createOrderTypeFormGroup();

        const orderType = service.getOrderType(formGroup);

        expect(orderType).toMatchObject({});
      });

      it('should return IOrderType', () => {
        const formGroup = service.createOrderTypeFormGroup(sampleWithRequiredData);

        const orderType = service.getOrderType(formGroup);

        expect(orderType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderType should not enable id FormControl', () => {
        const formGroup = service.createOrderTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderType should disable id FormControl', () => {
        const formGroup = service.createOrderTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
