import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../shipment-method-type.test-samples';

import { ShipmentMethodTypeFormService } from './shipment-method-type-form.service';

describe('ShipmentMethodType Form Service', () => {
  let service: ShipmentMethodTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ShipmentMethodTypeFormService);
  });

  describe('Service methods', () => {
    describe('createShipmentMethodTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShipmentMethodTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            sequenceNo: expect.any(Object),
          }),
        );
      });

      it('passing IShipmentMethodType should create a new form with FormGroup', () => {
        const formGroup = service.createShipmentMethodTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            sequenceNo: expect.any(Object),
          }),
        );
      });
    });

    describe('getShipmentMethodType', () => {
      it('should return NewShipmentMethodType for default ShipmentMethodType initial value', () => {
        const formGroup = service.createShipmentMethodTypeFormGroup(sampleWithNewData);

        const shipmentMethodType = service.getShipmentMethodType(formGroup);

        expect(shipmentMethodType).toMatchObject(sampleWithNewData);
      });

      it('should return NewShipmentMethodType for empty ShipmentMethodType initial value', () => {
        const formGroup = service.createShipmentMethodTypeFormGroup();

        const shipmentMethodType = service.getShipmentMethodType(formGroup);

        expect(shipmentMethodType).toMatchObject({});
      });

      it('should return IShipmentMethodType', () => {
        const formGroup = service.createShipmentMethodTypeFormGroup(sampleWithRequiredData);

        const shipmentMethodType = service.getShipmentMethodType(formGroup);

        expect(shipmentMethodType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShipmentMethodType should not enable id FormControl', () => {
        const formGroup = service.createShipmentMethodTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShipmentMethodType should disable id FormControl', () => {
        const formGroup = service.createShipmentMethodTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
