import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../shipment-status.test-samples';

import { ShipmentStatusFormService } from './shipment-status-form.service';

describe('ShipmentStatus Form Service', () => {
  let service: ShipmentStatusFormService;

  beforeEach(() => {
    service = TestBed.inject(ShipmentStatusFormService);
  });

  describe('Service methods', () => {
    describe('createShipmentStatusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShipmentStatusFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statusDateTime: expect.any(Object),
            sequenceNo: expect.any(Object),
            shipment: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });

      it('passing IShipmentStatus should create a new form with FormGroup', () => {
        const formGroup = service.createShipmentStatusFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statusDateTime: expect.any(Object),
            sequenceNo: expect.any(Object),
            shipment: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });
    });

    describe('getShipmentStatus', () => {
      it('should return NewShipmentStatus for default ShipmentStatus initial value', () => {
        const formGroup = service.createShipmentStatusFormGroup(sampleWithNewData);

        const shipmentStatus = service.getShipmentStatus(formGroup);

        expect(shipmentStatus).toMatchObject(sampleWithNewData);
      });

      it('should return NewShipmentStatus for empty ShipmentStatus initial value', () => {
        const formGroup = service.createShipmentStatusFormGroup();

        const shipmentStatus = service.getShipmentStatus(formGroup);

        expect(shipmentStatus).toMatchObject({});
      });

      it('should return IShipmentStatus', () => {
        const formGroup = service.createShipmentStatusFormGroup(sampleWithRequiredData);

        const shipmentStatus = service.getShipmentStatus(formGroup);

        expect(shipmentStatus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShipmentStatus should not enable id FormControl', () => {
        const formGroup = service.createShipmentStatusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShipmentStatus should disable id FormControl', () => {
        const formGroup = service.createShipmentStatusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
