import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-adjustment-type.test-samples';

import { OrderAdjustmentTypeFormService } from './order-adjustment-type-form.service';

describe('OrderAdjustmentType Form Service', () => {
  let service: OrderAdjustmentTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderAdjustmentTypeFormService);
  });

  describe('Service methods', () => {
    describe('createOrderAdjustmentTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderAdjustmentTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });

      it('passing IOrderAdjustmentType should create a new form with FormGroup', () => {
        const formGroup = service.createOrderAdjustmentTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderAdjustmentType', () => {
      it('should return NewOrderAdjustmentType for default OrderAdjustmentType initial value', () => {
        const formGroup = service.createOrderAdjustmentTypeFormGroup(sampleWithNewData);

        const orderAdjustmentType = service.getOrderAdjustmentType(formGroup);

        expect(orderAdjustmentType).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderAdjustmentType for empty OrderAdjustmentType initial value', () => {
        const formGroup = service.createOrderAdjustmentTypeFormGroup();

        const orderAdjustmentType = service.getOrderAdjustmentType(formGroup);

        expect(orderAdjustmentType).toMatchObject({});
      });

      it('should return IOrderAdjustmentType', () => {
        const formGroup = service.createOrderAdjustmentTypeFormGroup(sampleWithRequiredData);

        const orderAdjustmentType = service.getOrderAdjustmentType(formGroup);

        expect(orderAdjustmentType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderAdjustmentType should not enable id FormControl', () => {
        const formGroup = service.createOrderAdjustmentTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderAdjustmentType should disable id FormControl', () => {
        const formGroup = service.createOrderAdjustmentTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
