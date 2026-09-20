import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../shipment.test-samples';

import { ShipmentFormService } from './shipment-form.service';

describe('Shipment Form Service', () => {
  let service: ShipmentFormService;

  beforeEach(() => {
    service = TestBed.inject(ShipmentFormService);
  });

  describe('Service methods', () => {
    describe('createShipmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShipmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            estimatedShipDate: expect.any(Object),
            estimatedReadyDate: expect.any(Object),
            estimatedArrivalDate: expect.any(Object),
            estimatedShipCost: expect.any(Object),
            shipmentType: expect.any(Object),
            shipmentMethodType: expect.any(Object),
            status: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            originFacility: expect.any(Object),
            destinationFacility: expect.any(Object),
            fromParty: expect.any(Object),
            toParty: expect.any(Object),
            carrierParty: expect.any(Object),
            contactMech: expect.any(Object),
          }),
        );
      });

      it('passing IShipment should create a new form with FormGroup', () => {
        const formGroup = service.createShipmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            estimatedShipDate: expect.any(Object),
            estimatedReadyDate: expect.any(Object),
            estimatedArrivalDate: expect.any(Object),
            estimatedShipCost: expect.any(Object),
            shipmentType: expect.any(Object),
            shipmentMethodType: expect.any(Object),
            status: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            originFacility: expect.any(Object),
            destinationFacility: expect.any(Object),
            fromParty: expect.any(Object),
            toParty: expect.any(Object),
            carrierParty: expect.any(Object),
            contactMech: expect.any(Object),
          }),
        );
      });
    });

    describe('getShipment', () => {
      it('should return NewShipment for default Shipment initial value', () => {
        const formGroup = service.createShipmentFormGroup(sampleWithNewData);

        const shipment = service.getShipment(formGroup);

        expect(shipment).toMatchObject(sampleWithNewData);
      });

      it('should return NewShipment for empty Shipment initial value', () => {
        const formGroup = service.createShipmentFormGroup();

        const shipment = service.getShipment(formGroup);

        expect(shipment).toMatchObject({});
      });

      it('should return IShipment', () => {
        const formGroup = service.createShipmentFormGroup(sampleWithRequiredData);

        const shipment = service.getShipment(formGroup);

        expect(shipment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShipment should not enable id FormControl', () => {
        const formGroup = service.createShipmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShipment should disable id FormControl', () => {
        const formGroup = service.createShipmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
