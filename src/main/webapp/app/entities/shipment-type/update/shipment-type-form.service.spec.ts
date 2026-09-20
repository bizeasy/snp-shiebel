import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../shipment-type.test-samples';

import { ShipmentTypeFormService } from './shipment-type-form.service';

describe('ShipmentType Form Service', () => {
  let service: ShipmentTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ShipmentTypeFormService);
  });

  describe('Service methods', () => {
    describe('createShipmentTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShipmentTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IShipmentType should create a new form with FormGroup', () => {
        const formGroup = service.createShipmentTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getShipmentType', () => {
      it('should return NewShipmentType for default ShipmentType initial value', () => {
        const formGroup = service.createShipmentTypeFormGroup(sampleWithNewData);

        const shipmentType = service.getShipmentType(formGroup);

        expect(shipmentType).toMatchObject(sampleWithNewData);
      });

      it('should return NewShipmentType for empty ShipmentType initial value', () => {
        const formGroup = service.createShipmentTypeFormGroup();

        const shipmentType = service.getShipmentType(formGroup);

        expect(shipmentType).toMatchObject({});
      });

      it('should return IShipmentType', () => {
        const formGroup = service.createShipmentTypeFormGroup(sampleWithRequiredData);

        const shipmentType = service.getShipmentType(formGroup);

        expect(shipmentType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShipmentType should not enable id FormControl', () => {
        const formGroup = service.createShipmentTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShipmentType should disable id FormControl', () => {
        const formGroup = service.createShipmentTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
