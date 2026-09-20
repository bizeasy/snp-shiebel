import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../shipment-receipt.test-samples';

import { ShipmentReceiptFormService } from './shipment-receipt-form.service';

describe('ShipmentReceipt Form Service', () => {
  let service: ShipmentReceiptFormService;

  beforeEach(() => {
    service = TestBed.inject(ShipmentReceiptFormService);
  });

  describe('Service methods', () => {
    describe('createShipmentReceiptFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShipmentReceiptFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            quantityAccepted: expect.any(Object),
            quantityRejected: expect.any(Object),
            receivedDate: expect.any(Object),
            message: expect.any(Object),
            shipment: expect.any(Object),
            shipmentItem: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            inventoryItem: expect.any(Object),
            product: expect.any(Object),
            receivedBy: expect.any(Object),
          }),
        );
      });

      it('passing IShipmentReceipt should create a new form with FormGroup', () => {
        const formGroup = service.createShipmentReceiptFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            quantityAccepted: expect.any(Object),
            quantityRejected: expect.any(Object),
            receivedDate: expect.any(Object),
            message: expect.any(Object),
            shipment: expect.any(Object),
            shipmentItem: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            inventoryItem: expect.any(Object),
            product: expect.any(Object),
            receivedBy: expect.any(Object),
          }),
        );
      });
    });

    describe('getShipmentReceipt', () => {
      it('should return NewShipmentReceipt for default ShipmentReceipt initial value', () => {
        const formGroup = service.createShipmentReceiptFormGroup(sampleWithNewData);

        const shipmentReceipt = service.getShipmentReceipt(formGroup);

        expect(shipmentReceipt).toMatchObject(sampleWithNewData);
      });

      it('should return NewShipmentReceipt for empty ShipmentReceipt initial value', () => {
        const formGroup = service.createShipmentReceiptFormGroup();

        const shipmentReceipt = service.getShipmentReceipt(formGroup);

        expect(shipmentReceipt).toMatchObject({});
      });

      it('should return IShipmentReceipt', () => {
        const formGroup = service.createShipmentReceiptFormGroup(sampleWithRequiredData);

        const shipmentReceipt = service.getShipmentReceipt(formGroup);

        expect(shipmentReceipt).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShipmentReceipt should not enable id FormControl', () => {
        const formGroup = service.createShipmentReceiptFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShipmentReceipt should disable id FormControl', () => {
        const formGroup = service.createShipmentReceiptFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
