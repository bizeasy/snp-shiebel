import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-contact-mech.test-samples';

import { OrderContactMechFormService } from './order-contact-mech-form.service';

describe('OrderContactMech Form Service', () => {
  let service: OrderContactMechFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderContactMechFormService);
  });

  describe('Service methods', () => {
    describe('createOrderContactMechFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderContactMechFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            order: expect.any(Object),
            contactMech: expect.any(Object),
            contactMechPurpose: expect.any(Object),
          }),
        );
      });

      it('passing IOrderContactMech should create a new form with FormGroup', () => {
        const formGroup = service.createOrderContactMechFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            order: expect.any(Object),
            contactMech: expect.any(Object),
            contactMechPurpose: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderContactMech', () => {
      it('should return NewOrderContactMech for default OrderContactMech initial value', () => {
        const formGroup = service.createOrderContactMechFormGroup(sampleWithNewData);

        const orderContactMech = service.getOrderContactMech(formGroup);

        expect(orderContactMech).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderContactMech for empty OrderContactMech initial value', () => {
        const formGroup = service.createOrderContactMechFormGroup();

        const orderContactMech = service.getOrderContactMech(formGroup);

        expect(orderContactMech).toMatchObject({});
      });

      it('should return IOrderContactMech', () => {
        const formGroup = service.createOrderContactMechFormGroup(sampleWithRequiredData);

        const orderContactMech = service.getOrderContactMech(formGroup);

        expect(orderContactMech).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderContactMech should not enable id FormControl', () => {
        const formGroup = service.createOrderContactMechFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderContactMech should disable id FormControl', () => {
        const formGroup = service.createOrderContactMechFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
