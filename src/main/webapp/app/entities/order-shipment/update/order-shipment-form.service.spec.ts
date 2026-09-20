import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-shipment.test-samples';

import { OrderShipmentFormService } from './order-shipment-form.service';

describe('OrderShipment Form Service', () => {
  let service: OrderShipmentFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderShipmentFormService);
  });

  describe('Service methods', () => {
    describe('createOrderShipmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderShipmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            shipment: expect.any(Object),
            shipmentItem: expect.any(Object),
          }),
        );
      });

      it('passing IOrderShipment should create a new form with FormGroup', () => {
        const formGroup = service.createOrderShipmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            shipment: expect.any(Object),
            shipmentItem: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderShipment', () => {
      it('should return NewOrderShipment for default OrderShipment initial value', () => {
        const formGroup = service.createOrderShipmentFormGroup(sampleWithNewData);

        const orderShipment = service.getOrderShipment(formGroup);

        expect(orderShipment).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderShipment for empty OrderShipment initial value', () => {
        const formGroup = service.createOrderShipmentFormGroup();

        const orderShipment = service.getOrderShipment(formGroup);

        expect(orderShipment).toMatchObject({});
      });

      it('should return IOrderShipment', () => {
        const formGroup = service.createOrderShipmentFormGroup(sampleWithRequiredData);

        const orderShipment = service.getOrderShipment(formGroup);

        expect(orderShipment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderShipment should not enable id FormControl', () => {
        const formGroup = service.createOrderShipmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderShipment should disable id FormControl', () => {
        const formGroup = service.createOrderShipmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
