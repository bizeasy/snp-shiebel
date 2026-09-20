import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../shipment-attachment.test-samples';

import { ShipmentAttachmentFormService } from './shipment-attachment-form.service';

describe('ShipmentAttachment Form Service', () => {
  let service: ShipmentAttachmentFormService;

  beforeEach(() => {
    service = TestBed.inject(ShipmentAttachmentFormService);
  });

  describe('Service methods', () => {
    describe('createShipmentAttachmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShipmentAttachmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            shipment: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });

      it('passing IShipmentAttachment should create a new form with FormGroup', () => {
        const formGroup = service.createShipmentAttachmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            shipment: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });
    });

    describe('getShipmentAttachment', () => {
      it('should return NewShipmentAttachment for default ShipmentAttachment initial value', () => {
        const formGroup = service.createShipmentAttachmentFormGroup(sampleWithNewData);

        const shipmentAttachment = service.getShipmentAttachment(formGroup);

        expect(shipmentAttachment).toMatchObject(sampleWithNewData);
      });

      it('should return NewShipmentAttachment for empty ShipmentAttachment initial value', () => {
        const formGroup = service.createShipmentAttachmentFormGroup();

        const shipmentAttachment = service.getShipmentAttachment(formGroup);

        expect(shipmentAttachment).toMatchObject({});
      });

      it('should return IShipmentAttachment', () => {
        const formGroup = service.createShipmentAttachmentFormGroup(sampleWithRequiredData);

        const shipmentAttachment = service.getShipmentAttachment(formGroup);

        expect(shipmentAttachment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShipmentAttachment should not enable id FormControl', () => {
        const formGroup = service.createShipmentAttachmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShipmentAttachment should disable id FormControl', () => {
        const formGroup = service.createShipmentAttachmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
