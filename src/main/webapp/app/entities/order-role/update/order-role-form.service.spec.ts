import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-role.test-samples';

import { OrderRoleFormService } from './order-role-form.service';

describe('OrderRole Form Service', () => {
  let service: OrderRoleFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderRoleFormService);
  });

  describe('Service methods', () => {
    describe('createOrderRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });

      it('passing IOrderRole should create a new form with FormGroup', () => {
        const formGroup = service.createOrderRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderRole', () => {
      it('should return NewOrderRole for default OrderRole initial value', () => {
        const formGroup = service.createOrderRoleFormGroup(sampleWithNewData);

        const orderRole = service.getOrderRole(formGroup);

        expect(orderRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderRole for empty OrderRole initial value', () => {
        const formGroup = service.createOrderRoleFormGroup();

        const orderRole = service.getOrderRole(formGroup);

        expect(orderRole).toMatchObject({});
      });

      it('should return IOrderRole', () => {
        const formGroup = service.createOrderRoleFormGroup(sampleWithRequiredData);

        const orderRole = service.getOrderRole(formGroup);

        expect(orderRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderRole should not enable id FormControl', () => {
        const formGroup = service.createOrderRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderRole should disable id FormControl', () => {
        const formGroup = service.createOrderRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
