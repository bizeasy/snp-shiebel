import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../shipment-item.test-samples';

import { ShipmentItemFormService } from './shipment-item-form.service';

describe('ShipmentItem Form Service', () => {
  let service: ShipmentItemFormService;

  beforeEach(() => {
    service = TestBed.inject(ShipmentItemFormService);
  });

  describe('Service methods', () => {
    describe('createShipmentItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShipmentItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            message: expect.any(Object),
            shipment: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });

      it('passing IShipmentItem should create a new form with FormGroup', () => {
        const formGroup = service.createShipmentItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            message: expect.any(Object),
            shipment: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });
    });

    describe('getShipmentItem', () => {
      it('should return NewShipmentItem for default ShipmentItem initial value', () => {
        const formGroup = service.createShipmentItemFormGroup(sampleWithNewData);

        const shipmentItem = service.getShipmentItem(formGroup);

        expect(shipmentItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewShipmentItem for empty ShipmentItem initial value', () => {
        const formGroup = service.createShipmentItemFormGroup();

        const shipmentItem = service.getShipmentItem(formGroup);

        expect(shipmentItem).toMatchObject({});
      });

      it('should return IShipmentItem', () => {
        const formGroup = service.createShipmentItemFormGroup(sampleWithRequiredData);

        const shipmentItem = service.getShipmentItem(formGroup);

        expect(shipmentItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShipmentItem should not enable id FormControl', () => {
        const formGroup = service.createShipmentItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShipmentItem should disable id FormControl', () => {
        const formGroup = service.createShipmentItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
